package com.example.dima.wallpaper.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.wallpaper.Interface.ItemClickListener;
import com.example.dima.wallpaper.R;

/**
 * Created by Dima on 14.03.2018.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView categoryName;
    public ImageView backgroundImage;

    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);
        backgroundImage = itemView.findViewById(R.id.image);
        categoryName = itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
