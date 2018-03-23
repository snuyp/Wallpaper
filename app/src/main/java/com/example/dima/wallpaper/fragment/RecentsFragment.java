package com.example.dima.wallpaper.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.wallpaper.R;
import com.example.dima.wallpaper.adapter.RecyclerAdapter;
import com.example.dima.wallpaper.database.Recents;
import com.example.dima.wallpaper.database.dataSource.RecentRepository;
import com.example.dima.wallpaper.database.localDatabase.LocalDatabase;
import com.example.dima.wallpaper.database.localDatabase.RecentsDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment {

    private static RecentsFragment sRecentsFragment = null;

    private RecyclerView mRecyclerView;
    private List<Recents> recentsList;
    private RecyclerAdapter adapter;
    private Context context;

    private CompositeDisposable mCompositeDisposable;
    private RecentRepository mRecentRepository;
    public static RecentsFragment getInstance(Context context)
    {
        if(sRecentsFragment == null)
        {
            sRecentsFragment = new RecentsFragment(context);
        }
        return sRecentsFragment;
    }
    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        this.context = context;

        //Init RoomDataBase
        mCompositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(context);
        mRecentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_recent);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        recentsList = new ArrayList<>();
        adapter = new RecyclerAdapter(context, recentsList);
        mRecyclerView.setAdapter(adapter);

        loadRecents();
        return view;
    }

    private void loadRecents() {
        Disposable disposable = mRecentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentsSuccess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",throwable.getMessage());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void onGetAllRecentsSuccess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}
