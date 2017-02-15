package com.example.kirito.simplenote.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kirito.simplenote.R;
import com.example.kirito.simplenote.entity.Item;

import java.util.List;

/**
 * Created by kirito on 2017.02.14.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<Item> items;
    private LayoutInflater mLayoutInflater;
    private onItemClickListener listener;
    private onItemLongClickListener longClickListener;

    public RecyclerAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void addAll(List<Item> itemList){
        items = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder)holder).bindHolder(items.get(position));
        holder.itemView.setTag(items.get(position));
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onItemClick(v,(Item) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null){
            longClickListener.onItemLongClick(v,(Item)v.getTag());
        }
        return true;
    }

    public interface onItemClickListener{
        void onItemClick(View view,Item item);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public interface onItemLongClickListener{
        void onItemLongClick(View view,Item item);
    }

    public void setOnItemLongClickListener(onItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
