package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    SQLiteDatabase user_db;
    SQLiteDatabase list_db;
    UserInfoDBHelper user_helper;
    ListDBHelper list_helper;

    ListView listView;
    ListItemAdapter adapter;
    ArrayList<ListItem> listItemArrayList;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);

        listItemArrayList = new ArrayList<ListItem>();
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ListItemAdapter(getContext(),listItemArrayList);

        user_helper = new UserInfoDBHelper(getContext());
        list_helper = new ListDBHelper(getContext());

        listView.setAdapter(adapter);
        //listItemArrayList.add(new ListItem());

        load_values();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("");
                builder.setMessage("글을 삭제하시겠습니까?");
                final int p = position;

                // 글 삭제하고 나가기
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_values(p);
                        Toast.makeText(getContext(),"삭제",Toast.LENGTH_SHORT).show();
                    }
                });

                // 기존 상세 내용 창으로 돌아가기
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                //Toast.makeText(getContext(),"dddd",Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });

        setHasOptionsMenu(true);

        //load_values();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_plus) {
            Intent intent = new Intent(getContext(),WriteActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void load_values() {

        //user_db = user_helper.getReadableDatabase();
        int recordCount = -1;

        /*
        if(user_db != null) {
            Cursor c = user_db.rawQuery(UserInfoContractDB.SQL_SELECT,null);
            recordCount = c.getCount();
            listItemArrayList.clear();
            for(int i = 0; i<recordCount; i++) {
                c.moveToNext();
                String profileStr = c.getString(1);
                String nameStr = c.getString(2);
                listItemArrayList.add(new ListItem(profileStr,nameStr));
            }
            c.close();
            adapter.notifyDataSetChanged();
        }*/

        list_db = list_helper.getReadableDatabase();
        if(list_db != null) {
            Cursor c = list_db.rawQuery(ListContractDB.SQL_SELECT,null);
            recordCount = c.getCount();
            listItemArrayList.clear();
            for(int i = 0; i<recordCount; i++) {
                c.moveToNext();
                String dateStr = c.getString(1);
                String textStr = c.getString(2);
                String imageStr = c.getString(3);
                listItemArrayList.add(new ListItem(dateStr,textStr,imageStr));
            }
            c.close();
            adapter.notifyDataSetChanged();
        }
    }

    public void delete_values(int p) {
        list_db = list_helper.getWritableDatabase();
        String sqlSelect = ListContractDB.SQL_SELECT;
        Cursor c = list_db.rawQuery(sqlSelect,null);
        c.moveToPosition(p);
        int id = c.getInt(0);
        String sqlDelete = ListContractDB.SQL_DELETE + id;
        list_db.execSQL(sqlDelete);
        adapter.notifyDataSetChanged();
        c.close();//
        list_db.close();
    }
}
