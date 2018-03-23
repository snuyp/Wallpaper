package com.example.dima.wallpaper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.wallpaper.Interface.ItemClickListener;
import com.example.dima.wallpaper.R;
import com.example.dima.wallpaper.ViewWallpaper;
import com.example.dima.wallpaper.common.Common;
import com.example.dima.wallpaper.database.Recents;
import com.example.dima.wallpaper.model.WallpaperItem;
import com.example.dima.wallpaper.viewHolder.ListWallpaperViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dima on 22.03.2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {
    private Context context;
    private List<Recents> recents;

    public RecyclerAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallpaper_item, parent, false);

        int height = parent.getMeasuredHeight() / 2;
        itemView.setMinimumHeight(height);
        return new ListWallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListWallpaperViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Picasso.with(context)
                .load(recents.get(position).getImageLink())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(recents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.wallpaper, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.e("ERROR", "Couldt't fetch image");
                                    }
                                });
                    }
                });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, ViewWallpaper.class);
                WallpaperItem wallpaperItem = new WallpaperItem();
                wallpaperItem.setCategoryId(recents.get(position).getCategoryId());
                wallpaperItem.setImageUrl(recents.get(position).getImageLink());
                Common.sWallpaperItem = wallpaperItem;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
