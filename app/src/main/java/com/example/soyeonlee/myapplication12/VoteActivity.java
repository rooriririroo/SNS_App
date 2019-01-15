package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

public class VoteActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Switch vote_switch_plural;
    Switch vote_switch_anonymity;
    Switch vote_switch_addition;

    EditText vote_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        linearLayout = (LinearLayout) findViewById(R.id.dynamic_vote);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("투표");

        vote_title = findViewById(R.id.vote_title);

        vote_switch_plural = findViewById(R.id.vote_switch_plural);
        vote_switch_plural.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        vote_switch_anonymity = findViewById(R.id.vote_switch_anonymity);
        vote_switch_anonymity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        vote_switch_addition = findViewById(R.id.vote_switch_addition);
        vote_switch_addition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
            }
        });
    }

    public void moreClick(View v) {
        EditText edit_content = new EditText(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.setMargins(24,8,24,8);
        edit_content.setBackground(getDrawable(R.drawable.edittext_outline));
        edit_content.setPadding(26,30,26,30);
        edit_content.setEms(15);
        edit_content.setHint("항목 입력");
        edit_content.setLayoutParams(layoutParams);
        linearLayout.addView(edit_content);
    }

    public void backClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("지금 나가면 글이 저장되지 않습니다.\n나가시겠습니까?");

        // 글 저장 안하고 나가기
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // 기존 글쓰기 창으로 돌아가기
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void saveClick(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflater.inflate(R.menu.menu_attach, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.menu_save_button :
                Intent intent = new Intent(VoteActivity.this, WriteActivity.class);
                intent.putExtra("VoteTitle",vote_title.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
