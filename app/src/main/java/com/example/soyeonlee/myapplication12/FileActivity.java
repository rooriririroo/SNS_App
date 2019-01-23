package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class FileActivity extends AppCompatActivity {

    ArrayList<FileListItem> fileListItemArrayList;
    FileListItemAdapter adapter;
    ListView listView;
    String state = Environment.getExternalStorageState();
    File[] files;
    ArrayList<String> filePath = new ArrayList<String>();
    String rootPath;

    boolean[] selection;
    int total = 0;
    int count = 0;
    ArrayList<String> arrPath = new ArrayList<String>();
    TextView badgeNum;

    boolean isSelected = false;

    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("파일");

        Log.d("[File]=>","onCreate" + getExternalMounts().toString());

        fileListItemArrayList = new ArrayList<FileListItem>();
        listView = findViewById(R.id.file_list);
        adapter = new FileListItemAdapter(this,fileListItemArrayList);
        listView.setAdapter(adapter);

        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File fileRoot = new File(rootPath);
        initFileList(fileRoot);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = fileListItemArrayList.get(position).getFileName();
                String path = fileListItemArrayList.get(position).getFilePath();
                //Toast.makeText(getApplicationContext(),String.valueOf(position) + fileListItemArrayList.get(position).getFileName(),Toast.LENGTH_SHORT).show();
                File file = new File(path);

                if(file.isDirectory()) {
                    Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
                    movePath(path);
                }
                if(file.isFile())
                {
                    if(selection[position]) {
                        isSelected = false;
                        selection[position] = false;
                        arrPath.remove(path);
                        total--;
                        fileListItemArrayList.get(position).setIsSelected(isSelected);

                        if(total == 0)
                            badgeNum.setVisibility(View.GONE);
                        badgeNum.setText(String.valueOf(total));
                    }
                    else {
                        isSelected = true;
                        selection[position] = true;
                        arrPath.add(path);
                        total++;
                        fileListItemArrayList.get(position).setIsSelected(isSelected);

                        if(total >= 1)
                            badgeNum.setVisibility(View.VISIBLE);
                        badgeNum.setText(String.valueOf(total));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        String[] externalArray;
        String secondaryStorage = System.getenv("SECONDARY_STORAGE");
        if(secondaryStorage != null)
            externalArray = secondaryStorage.split(":");
        else
            externalArray = new String[0];
        for(int i = 0; i<externalArray.length; i++)
            Log.d("Secondary Storage=>",externalArray[i]);
    }

    public void initFileList(File directory) {
        File[] fileList = directory.listFiles();
        fileListItemArrayList.clear();
        //fileListItemArrayList.add(new FileListItem("상위 폴더로 이동",exP.getAbsolutePath(),true,false));
        //fileListItemArrayList.add(new FileListItem("상위 폴더로 이동","/storage/0000-0000",true,false));//

        if(fileList != null)  {
            for(File file:fileList) {
                if(!file.isHidden() && !file.getName().equals("Android") && !file.getName().equals("cache")) {
                    if(file.isDirectory()) {
                        fileListItemArrayList.add(new FileListItem(file.getName(),file.getAbsolutePath(),true,false));
                    }
                    else if(file.isFile()){
                        count++;
                        fileListItemArrayList.add(new FileListItem(file.getName(),file.getAbsolutePath(),false,false));
                    }
                }
            }
        }
        selection = new boolean[count+50];
        adapter.notifyDataSetChanged();
    }

    public void updateFileList(File directory) {
        File[] fileList = directory.listFiles();
        fileListItemArrayList.clear();
        fileListItemArrayList.add(new FileListItem("상위 폴더로 이동",directory.getParent(),true,false));

        if(fileList != null)  {
            for(File file:fileList) {
                if(!file.isHidden() && !file.getName().equals("Android") && !file.getName().equals("cache")) {
                    if(file.isDirectory()) {
                        fileListItemArrayList.add(new FileListItem(file.getName(),file.getAbsolutePath(),true,false));
                    }
                    else if(file.isFile()){
                        count++;
                        fileListItemArrayList.add(new FileListItem(file.getName(),file.getAbsolutePath(),false,false));
                    }
                }
            }
        }
        selection = new boolean[count+50];
        adapter.notifyDataSetChanged();
    }

    public void movePath(String str) {
        File directory = new File(str);
        if(directory.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
            initFileList(directory);
        else
            updateFileList(directory);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[File]=>","onPause");
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
                Intent intent = new Intent(FileActivity.this,WriteActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("files",arrPath);
                setResult(RESULT_OK,intent);
                finish();
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
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //어댑터
    public class FileListItemAdapter extends BaseAdapter {

        Context context;
        ArrayList<FileListItem> fileListItemArrayList = new ArrayList<FileListItem>();
        ViewHolder viewHolder;

        class ViewHolder {
            TextView fileName;
            int id;
        }

        public FileListItemAdapter(Context context, ArrayList<FileListItem> fileListItemArrayList) {
            this.context = context;
            this.fileListItemArrayList = fileListItemArrayList;
        }

        @Override
        public int getCount() {
            return fileListItemArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileListItemArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fileitem, null);
                viewHolder = new ViewHolder();
                viewHolder.fileName = convertView.findViewById(R.id.file_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 폴더일경우 폴더 아이콘 삽입
            if (fileListItemArrayList.get(position).getIsDirectory()) {
                // 상위 폴더일 경우 화살표 아이콘 삽입
                if(fileListItemArrayList.get(position).getFileName().equals("상위 폴더로 이동")) {
                    viewHolder.fileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_subdirectory_arrow_left_black_18dp_rotate,0,0,0);
                    viewHolder.fileName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(198, 198, 198)));
                    viewHolder.fileName.setText(fileListItemArrayList.get(position).getFileName());
                    viewHolder.fileName.setTextColor(Color.rgb(198,198,198));
                }
                else {
                    viewHolder.fileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_folder_black_18dp, 0, 0, 0);
                    viewHolder.fileName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(198, 198, 198)));
                    viewHolder.fileName.setCompoundDrawablePadding(20);
                    viewHolder.fileName.setText(fileListItemArrayList.get(position).getFileName());
                    viewHolder.fileName.setTextColor(Color.BLACK);
                }
            }

            // 파일일 경우 체크 아이콘 삽입
            else if (!fileListItemArrayList.get(position).getIsDirectory()) {
                viewHolder.fileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_check_circle_outline_white_18dp, 0);
                viewHolder.fileName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(229, 229, 229)));
                viewHolder.fileName.setText(fileListItemArrayList.get(position).getFileName());
                viewHolder.fileName.setTextColor(Color.BLACK);

                if (fileListItemArrayList.get(position).getIsSelected()) {
                    viewHolder.fileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_circle_black_18dp, 0);
                    viewHolder.fileName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(255, 180, 113)));
                    viewHolder.fileName.setTextColor(Color.BLACK);
                }
                else if (!fileListItemArrayList.get(position).getIsSelected()) {
                    viewHolder.fileName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_check_circle_outline_white_18dp, 0);
                    viewHolder.fileName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(229, 229, 229)));
                    viewHolder.fileName.setTextColor(Color.BLACK);
                }
            }
            return convertView;
        }
    }

    static public HashSet<String> getExternalMounts(){
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount").redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
    }
}
