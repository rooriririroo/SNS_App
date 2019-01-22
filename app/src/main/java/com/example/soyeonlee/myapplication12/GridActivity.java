package com.example.soyeonlee.myapplication12;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.mikepenz.actionitembadge.library.utils.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GridActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<GridItem> gridItemArrayList;
    GridItemAdapter adapter;
    int count = 0;
    boolean[] selection;
    int total = 0;
    ArrayList<String> arrPath;
    ArrayList<String> arrPathAll;
    ArrayList<String> uri;
    ArrayList<String> uriAll;
    TextView badgeNum;

    int RESULT_FROM_GRID = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Log.d("[Grid]=>","onCreate");

        gridView = findViewById(R.id.grid);
        gridItemArrayList = new ArrayList<GridItem>();
        adapter = new GridItemAdapter(this,gridItemArrayList);
        gridView.setAdapter(adapter);
        arrPath = new ArrayList<String>();
        arrPathAll = new ArrayList<String>();
        uri = new ArrayList<String>();
        uriAll = new ArrayList<String>();

        Intent intent = getIntent();
        String path = intent.getStringExtra("folderPath");
        String name = intent.getStringExtra("folderName");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        File filePath = new File(path);
        File[] files = filePath.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(o1.lastModified() > o2.lastModified())
                    return -1;
                if(o1.lastModified() == o2.lastModified())
                    return 0;
                return 1;
            }
        });

        for(File file : files) {
            if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                gridItemArrayList.add(new GridItem(file.getAbsolutePath()));
                //gridItemArrayList.add(new GridItem(getUriFromPath(file.getAbsolutePath()).toString()));
                arrPathAll.add(file.getAbsolutePath());
                //uriAll.add(getUriFromPath(file.getAbsolutePath()).toString());
                count++;
            }
        }

        selection = new boolean[count];
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[Grid]=>","onPause");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_attach,menu);

        RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.menu_attach_button).getActionView();
        badgeNum = relativeLayout.findViewById(R.id.attach_num);
        badgeNum.setVisibility(View.GONE);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GridActivity.this,WriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("images",arrPath);
                //intent.putExtra("images",uri);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.menu_attach_button :
                Intent intent = new Intent(GridActivity.this,WriteActivity.class);
                intent.putExtra("images",arrPath);
                setResult(RESULT_FROM_GRID);
                startActivity(intent);

                /*
                if(total == 1) {
                    //Toast.makeText(getApplicationContext(),arrPathIntent.toString(),Toast.LENGTH_SHORT).show();
                    intent.putExtra("file",arrPath.get(0));
                    startActivity(intent);
                }
                else if(total>1) {
                    intent.putExtra("files",arrPath);
                    setResult(RESULT_OK);
                    startActivity(intent);
                }*/
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // 어댑터
    public class GridItemAdapter extends BaseAdapter {

        Context context;
        ArrayList<GridItem> gridItemArrayList = new ArrayList<GridItem>();
        ViewHolder viewHolder;

        class ViewHolder {
            ImageView gridImage;
            CheckBox gridCheck;
            int id;
        }

        public GridItemAdapter(Context context, ArrayList<GridItem> gridItemArrayList) {
            this.context = context;
            this.gridItemArrayList = gridItemArrayList;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_griditem,null);
                viewHolder = new ViewHolder();
                viewHolder.gridImage = convertView.findViewById(R.id.grid_image);
                viewHolder.gridCheck = convertView.findViewById(R.id.grid_check);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.gridImage.setId(position);
            viewHolder.gridImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id= v.getId();
                    Intent intent = new Intent(context,GalleryDetailActivity.class);
                    intent.putExtra("filePath",gridItemArrayList.get(id).getGridImage()); // 선택한 하나의 이미지 파일 경로
                    intent.putExtra("fileNum",String.valueOf(id+1)); // 현재 폴더내 파일 아이디
                    intent.putExtra("fileTotal",String.valueOf(count)); // 현재 폴더내 모든 이미지 파일 수
                    intent.putExtra("AllFiles",arrPathAll); // 현재 폴더내 모든 이미지 파일 경로 리스트
                    intent.putExtra("filePosition",String.valueOf(id)); // 선택한 이미지 파일 아이디
                    intent.putExtra("fileSelect",String.valueOf(total)); // 현재 폴더내 선택한 이미지 파일 수
                    intent.putExtra("images",arrPath); // 현재 폴더내 선택한 이미지 파일 경로 리스트
                    //intent.putExtra("AllUris",uriAll); // 현재 폴더내 모든 이미지 파일 uri 리스트
                    //intent.putExtra("uris",uri); // 현재 폴더내 선택한 이미지 파일 uri 리스트
                    intent.putExtra("selection",selection[id]);
                    context.startActivity(intent);
                }
            });

            viewHolder.gridCheck.setId(position);
            viewHolder.gridCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if(selection[id]) {
                        cb.setChecked(false);
                        selection[id] = false;
                        total--;
                        arrPath.remove(gridItemArrayList.get(id).getGridImage());
                        //uri.remove(gridItemArrayList.get(id).getGridImage());

                        if(total == 0)
                            badgeNum.setVisibility(View.GONE);
                        badgeNum.setText(String.valueOf(total));
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_SHORT).show();
                        cb.setChecked(true);
                        selection[id] = true;
                        total++;
                        arrPath.add(gridItemArrayList.get(id).getGridImage());
                        //uri.add(gridItemArrayList.get(id).getGridImage());

                        if(total == 1)
                            badgeNum.setVisibility(View.VISIBLE);
                        badgeNum.setText(String.valueOf(total));
                    }
                }
            });

            Glide.with(context).load(gridItemArrayList.get(position).getGridImage()).into(viewHolder.gridImage);
            viewHolder.gridCheck.setChecked(selection[position]);
            viewHolder.id = position;

            return convertView;
        }
    }

    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,"_data = '"+filePath+"'",null,null);
        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id);
        return uri;
    }
}
