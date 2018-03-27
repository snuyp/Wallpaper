package com.example.dima.wallpaper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dima.wallpaper.common.Common;
import com.example.dima.wallpaper.database.Recents;
import com.example.dima.wallpaper.database.dataSource.RecentRepository;
import com.example.dima.wallpaper.database.localDatabase.LocalDatabase;
import com.example.dima.wallpaper.database.localDatabase.RecentsDataSource;
import com.example.dima.wallpaper.helper.SaveImageHelper;
import com.example.dima.wallpaper.model.WallpaperItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpaper extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFloatingActionButton;
    private FloatingActionButton mFabDownload;
    private CoordinatorLayout mCoordinatorLayout;
    private ImageView mImageView;

    private CompositeDisposable mCompositeDisposable;
    private RecentRepository mRecentRepository;

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

        //Init RoomDataBase
        mCompositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(this);
        mRecentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));



        mCoordinatorLayout = findViewById(R.id.root_layout);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        mCollapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);

        mImageView = findViewById(R.id.imageThumb);
        Picasso.with(this)
                .load(Common.sWallpaperItem.getImageUrl())
                .into(mImageView);

        addToRecents();
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


        increaseViewCount();
    }

    private void increaseViewCount() {
        FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .child(Common.SELECT_BACKGROUND_KEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("viewCount"))
                        {
                            WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
                            long count = wallpaperItem.getViewCount() + 1;
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount",count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.SELECT_BACKGROUND_KEY)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaper.this, "Cannot update view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", 1L);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.SELECT_BACKGROUND_KEY)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaper.this, "Cannot set default view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addToRecents() {
        Disposable disposable = Observable.create( new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Recents recents = new Recents(
                        Common.sWallpaperItem.getImageUrl(),
                        Common.sWallpaperItem.getCategoryId(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.SELECT_BACKGROUND_KEY);

                mRecentRepository.insertRecents(recents);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        
                    }
                });
        mCompositeDisposable.add(disposable);
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
        mCompositeDisposable.clear();
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
