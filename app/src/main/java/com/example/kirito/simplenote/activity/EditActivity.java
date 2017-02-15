package com.example.kirito.simplenote.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kirito.simplenote.R;
import com.example.kirito.simplenote.clock.ClockReceiver;
import com.example.kirito.simplenote.db.DbNote;
import com.example.kirito.simplenote.entity.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private int set_hour;
    private int set_minute;
    private AlarmManager am;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clock,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //下面这些代码必须放在 super.onBackPressed();的上面，若放在下面则出错
        //因为super.onBackPressed(); 会调用finish（）方法
        String content = et.getText().toString();
        Intent intent = new Intent();
        //intent.putExtra("content",content);
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

        if (item.getItemId() == R.id.menu_clock){
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent i = new Intent(EditActivity.this, ClockReceiver.class);
                    i.putExtra("note",et.getText().toString());
                    PendingIntent pi = PendingIntent.getBroadcast(EditActivity.this,0,i,0);
                    calendar.set(year,month,day,hourOfDay,minute,0);
                    Log.e(TAG, "onTimeSet: hour---"+hourOfDay );
                    Log.e(TAG, "onTimeSet: minute---"+minute );
                    am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
                    Toast.makeText(EditActivity.this,"该消息已经设置提醒！",Toast.LENGTH_SHORT).show();
                    /*set_hour = hourOfDay;
                    set_minute = minute;*/
                }
            },hour,minute,true).show();
        }
        return true;
    }
}
