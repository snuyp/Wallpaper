package com.example.dima.wallpaper;

import android.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dima.wallpaper.common.Common;
import com.example.dima.wallpaper.helper.SaveImageHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class ViewWallpaper extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFloatingActionButton;
    private FloatingActionButton mFabDownload;
    private CoordinatorLayout mCoordinatorLayout;
    private ImageView mImageView;

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(mCoordinatorLayout,"Wallpaper was set",Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mCoordinatorLayout = findViewById(R.id.root_layout);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        mCollapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);

        mImageView = findViewById(R.id.imageThumb);
        Picasso.with(this)
                .load(Common.sWallpaperItem.getImageUrl())
                .into(mImageView);

        mFloatingActionButton = findViewById(R.id.fabWallpaper);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.sWallpaperItem.getImageUrl())
                        .into(mTarget);
            }
        });
        mFabDownload = findViewById(R.id.fabDownload);
        mFabDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(ViewWallpaper.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSIONS_REQUEST_CODE);
                }
                else
                {
                    saveImage();
                }

            }
        });
    }
    private void saveImage()
    {
        AlertDialog dialog = new SpotsDialog(ViewWallpaper.this);
        dialog.show();
        dialog.setMessage("Please waiting...");

        String fileName = UUID.randomUUID().toString()+"png.";
        Picasso.with(getBaseContext())
                .load(Common.sWallpaperItem.getImageUrl())
                .into(new SaveImageHelper(getBaseContext(),
                        dialog,
                        getApplicationContext().getContentResolver(),
                        fileName,
                        "Live Wallpaper Image"));
    }
    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(mTarget);
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ShowToast")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Common.PERMISSIONS_REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    saveImage();

                }else{
                    Toast.makeText(this,"You need accept permission to download image",Toast.LENGTH_SHORT);
                }
            }

        }
    }
}
