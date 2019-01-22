package com.example.soyeonlee.myapplication12;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class VideoGridActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<GridItem> gridItemArrayList;
    VideoGridItemAdapter adapter;
    int count = 0;
    boolean[] selection;
    int total = 0;
    TextView badgeNum;
    ArrayList<String> arrPath;
    ArrayList<String> uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = findViewById(R.id.grid);
        gridItemArrayList = new ArrayList<GridItem>();
        adapter = new VideoGridItemAdapter(this,gridItemArrayList);
        gridView.setAdapter(adapter);
        arrPath = new ArrayList<String>();
        uri = new ArrayList<String>();

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
            if(file.getName().endsWith(".mp4")) {
                //gridItemArrayList.add(new GridItem(getUriFromPath(file.getAbsolutePath()).toString()));
                gridItemArrayList.add(new GridItem(file.getAbsolutePath()));
                count++;
            }
        }

        selection = new boolean[count];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_attach,menu);

        RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.menu_attach_button).getActionView();
        badgeNum = relativeLayout.findViewById(R.id.attach_num);
        badgeNum.setVisibility(View.GONE);
        //textView.setText("10");

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoGridActivity.this,WriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("videos",arrPath);
                //intent.putExtra("videos",uri);
                //setResult(RESULT_FROM_GRID,intent);
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
                Toast.makeText(getApplicationContext(),String.valueOf(total),Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // 어댑터
    public class VideoGridItemAdapter extends BaseAdapter {

        Context context;
        ArrayList<GridItem> gridItemArrayList = new ArrayList<GridItem>();
        ViewHolder viewHolder;

        class ViewHolder {
            ImageView gridImage;
            CheckBox gridCheck;
            int id;
        }

        public VideoGridItemAdapter(Context context, ArrayList<GridItem> gridItemArrayList) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_video_grid_item,null);
                viewHolder = new ViewHolder();
                viewHolder.gridImage = convertView.findViewById(R.id.grid_video);
                viewHolder.gridCheck = convertView.findViewById(R.id.grid_video_check);
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
                    Uri videoUri = Uri.parse(gridItemArrayList.get(id).getGridImage());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(videoUri,"video/*");
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_SHORT).show();
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
                        //uri.remove(gridItemArrayList.get(id).getGridImage());
                        arrPath.remove(gridItemArrayList.get(id).getGridImage());

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
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,"_data = '"+filePath+"'",null,null);
        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id);
        return uri;
    }
}
