package com.example.soyeonlee.myapplication12;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.soundcloud.android.crop.Crop;

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

        if(getIntent().hasExtra("FromWrite"))
            Log.d("[Grid]=>","From WriteActivity onCreate");
        if(getIntent().hasExtra("FromProfile"))
            Log.d("[Grid]=>","From ProfileChangeActivity onCreate");
        if(getIntent().hasExtra("FromRegister"))
            Log.d("[Grid]=>","From RegisterActivity onCreate");

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
                if(getIntent().hasExtra("FromWrite")) {
                    Intent intent = new Intent(GridActivity.this,WriteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("images",arrPath);
                    startActivity(intent);
                }
                else if(getIntent().hasExtra("FromProfile")) {
                    Uri source = getUriFromPath(arrPath.get(0));
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String folderPath = path + File.separator + "RUSH";
                    String cropFilePath = folderPath + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    File newFolderPath = new File(folderPath);
                    newFolderPath.mkdir();
                    File file = new File(cropFilePath);
                    Uri destination = Uri.fromFile(file);
                    //Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/RUSH/","cropForChange.jpg"));
                    //Uri destination = Uri.fromFile(new File(getExternalCacheDir(),"cropped"));
                    Crop.of(source,destination).asSquare().start(GridActivity.this);
                }
                else if(getIntent().hasExtra("FromRegister")) {
                    Uri source = getUriFromPath(arrPath.get(0));
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String folderPath = path + File.separator + "RUSH";
                    String cropFilePath = folderPath + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    File newFolderPath = new File(folderPath);
                    newFolderPath.mkdir();
                    File file = new File(cropFilePath);
                    Uri destination = Uri.fromFile(file);
                    Crop.of(source,destination).asSquare().start(GridActivity.this);
                }
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
                    intent.putExtra("selection",selection[id]);
                    if(getIntent().hasExtra("FromWrite"))
                        intent.putExtra("FromWrite",0); // WriteActivity
                    else if(getIntent().hasExtra("FromProfile"))
                        intent.putExtra("FromProfile",1); // ProfileChangeActivity
                    else if(getIntent().hasExtra("FromRegister"))
                        intent.putExtra("FromRegister",2); // RegisterActivity
                    context.startActivity(intent);
                }
            });

            viewHolder.gridCheck.setId(position);
            viewHolder.gridCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getIntent().hasExtra("FromWrite")) {
                        CheckBox cb = (CheckBox) v;
                        int id = cb.getId();
                        if(selection[id]) {
                            cb.setChecked(false);
                            selection[id] = false;
                            total--;
                            arrPath.remove(gridItemArrayList.get(id).getGridImage());

                            if(total == 0)
                                badgeNum.setVisibility(View.GONE);
                            badgeNum.setText(String.valueOf(total));
                        }
                        else {
                            cb.setChecked(true);
                            selection[id] = true;
                            total++;
                            arrPath.add(gridItemArrayList.get(id).getGridImage());

                            if(total == 1)
                                badgeNum.setVisibility(View.VISIBLE);
                            badgeNum.setText(String.valueOf(total));
                        }
                    }
                    else {
                        CheckBox cb = (CheckBox) v;
                        int id = cb.getId();
                        if(selection[id]) {
                            cb.setChecked(false);
                            selection[id] = false;
                            total--;
                            arrPath.remove(gridItemArrayList.get(id).getGridImage());

                            if(total == 0)
                                badgeNum.setVisibility(View.GONE);
                            badgeNum.setText(String.valueOf(total));
                        }
                        else {
                            cb.setChecked(true);
                            selection[id] = true;
                            total++;
                            arrPath.add(gridItemArrayList.get(id).getGridImage());

                            if(total == 1)
                                badgeNum.setVisibility(View.VISIBLE);

                            else if(total>1) {
                                cb.setChecked(false);
                                selection[id] = false;
                                total--;
                                arrPath.remove(gridItemArrayList.get(id).getGridImage());

                                AlertDialog.Builder builder = new AlertDialog.Builder(GridActivity.this);
                                builder.setTitle("");
                                builder.setMessage("하나의 이미지만 선택 가능합니다.");

                                // 글 저장 안하고 나가기
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.show();
                            }
                            badgeNum.setText(String.valueOf(total));
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //확인
        if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(data);
            String filePath = uri.toString().substring(7);

            if(getIntent().hasExtra("FromProfile")) {
                Intent intent = new Intent(GridActivity.this,ProfileChangeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("cropImage",filePath);
                startActivity(intent);
            }
            else if(getIntent().hasExtra("FromRegister")) {
                Intent intent = new Intent(GridActivity.this,RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("cropImage",filePath);
                startActivity(intent);
            }
        }
        //취소
        else if(requestCode == Crop.REQUEST_CROP) {
        }
    }
}
