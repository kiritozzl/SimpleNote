package com.example.kirito.simplenote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.kirito.simplenote.R;
import com.example.kirito.simplenote.db.DbNote;
import com.example.kirito.simplenote.entity.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kirito on 2017.02.14.
 */

public class EditActivity extends AppCompatActivity {
    private EditText et;
    private String time;
    private int flag;
    private String n_content;
    private int id;
    private static final String TAG = "EditActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        et = (EditText) findViewById(R.id.et);
        flag = getIntent().getIntExtra("flag",0);
        id = getIntent().getIntExtra("id",0);
        if (flag == 0){
            time = getCurrentTime();
        }else if (flag == 1){
            time = getIntent().getStringExtra("n_time");
            n_content = getIntent().getStringExtra("n_content");
            et.setText(n_content);
            et.setSelection(n_content.length());
        }
        setTitle(time);
    }

    private String getCurrentTime(){
        String time;
        time = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA).format(new Date());
        return time;
    }

    @Override
    public void onBackPressed() {
        //下面这些代码必须放在 super.onBackPressed();的上面，若放在下面则出错
        //因为super.onBackPressed(); 会调用finish（）方法
        String content = et.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("content",content);
        if (flag == 0){//新增笔记
            setResult(RESULT_OK,intent);

            if (!content.equals("")){
                Item item = new Item(content,getCurrentTime(),id);
                DbNote.getInstance(this).saveNote(item);
            }
        }

        if (flag == 1){//对内容进行修改的情况
            setResult(0x111,intent);

            if (!content.equals("") && !content.equals(n_content)){
                Item item = new Item(content,getCurrentTime(),id);
                DbNote.getInstance(this).modifyNote(item);
            }
        }

        super.onBackPressed();
    }

    //返回按钮的点击事件，添加效果：返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
