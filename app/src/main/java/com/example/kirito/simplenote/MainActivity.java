package com.example.kirito.simplenote;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.kirito.simplenote.activity.EditActivity;
import com.example.kirito.simplenote.db.DbNote;
import com.example.kirito.simplenote.entity.Item;
import com.example.kirito.simplenote.support.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private FloatingActionButton fab;
    private String content;
    private String time;

    private List<Item> items;
    private RecyclerAdapter adpter;
    private boolean isCheck = false;
    private Menu menu;
    private Item delete_item;
    private boolean isLongClick = false;

    private static final String TAG = "MainActivity";
    private ImageButton img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        //记得加上下面这句！
        rv.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //添加笔记
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("flag",0);
                intent.putExtra("id",getCurrentNoteId());
                startActivityForResult(intent,110);
            }
        });

        DbNote dbNote = DbNote.getInstance(this);
        if (dbNote.loadNotes() != null){
            items = dbNote.loadNotes();
        }else {
            items = new ArrayList<>();
        }
        adpter = new RecyclerAdapter(this);
        adpter.addAll(items);
        rv.setAdapter(adpter);

        //单击item查看和修改笔记
        adpter.setOnItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, Item item) {
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                i.putExtra("flag",1);
                i.putExtra("n_time",item.getTime());
                i.putExtra("n_content",item.getTitle());
                i.putExtra("id",item.getId());
                startActivityForResult(i,110);
            }
        });

        //长按item删除笔记
        adpter.setOnItemLongClickListener(new RecyclerAdapter.onItemLongClickListener() {

            @Override
            public void onItemLongClick(View view, final Item item) {
                isLongClick = true; //判断img_btn是否初始化
                img_btn = (ImageButton) view.findViewById(R.id.img_btn);
                img_btn.setVisibility(View.VISIBLE);

                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton ib = (ImageButton) v;
                        if (!isCheck){
                            ib.setImageResource(R.drawable.checked);
                            isCheck = true;
                            menu.findItem(R.id.trash).setVisible(true);
                            delete_item = item;
                        }else if (isCheck){
                            ib.setImageResource(R.drawable.check);
                            isCheck = false;
                            menu.findItem(R.id.trash).setVisible(false);
                            delete_item = null;
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.trash).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trash && delete_item != null){
            DbNote.getInstance(this).deleteNote(delete_item);
            refreshData();
        }
        return true;
    }

    private void hideCheckButton(){
        if(isLongClick){
            isCheck = false;
            img_btn.setImageResource(R.drawable.check);
            img_btn.setVisibility(View.GONE);
        }
    }

    private int getCurrentNoteId(){
        items = DbNote.getInstance(this).loadNotes();
        return items.size();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        content = data.getStringExtra("content");
        time = data.getStringExtra("time");

        if (requestCode == 110 && resultCode == RESULT_OK){//添加笔记的情况
            refreshData();
        }else if (requestCode == 110 && resultCode == 0x111){//修改笔记的情况
            refreshData();
        }
    }

    private void refreshData(){
        menu.findItem(R.id.trash).setVisible(false);
        hideCheckButton();

        resetDataBase();
        items = DbNote.getInstance(this).loadNotes();
        adpter.addAll(items);
        adpter.notifyDataSetChanged();
        isLongClick = false;
    }

    //删除笔记后重置数据库note id
    private void resetDataBase(){
        DbNote dbNote = DbNote.getInstance(this);
        items = dbNote.loadNotes();
        dbNote.clearData(items);
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setId(i);
        }
        dbNote.saveAll(items);
    }
}
