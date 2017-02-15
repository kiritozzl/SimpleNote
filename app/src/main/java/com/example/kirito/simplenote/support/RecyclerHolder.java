package com.example.kirito.simplenote.support;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kirito.simplenote.R;
import com.example.kirito.simplenote.entity.Item;

/**
 * Created by kirito on 2017.02.14.
 */

public class RecyclerHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView time;

    public RecyclerHolder(View itemView) {
        super(itemView);

        time = (TextView) itemView.findViewById(R.id.tv_time);
        title = (TextView) itemView.findViewById(R.id.tv_title);
    }

    public void bindHolder(Item item){
        title.setText(item.getTitle());
        time.setText(item.getTime());
    }
}
