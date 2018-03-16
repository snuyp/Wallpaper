package com.example.dima.wallpaper.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dima.wallpaper.Interface.ItemClickListener;
import com.example.dima.wallpaper.R;
import com.example.dima.wallpaper.common.Common;
import com.example.dima.wallpaper.model.CategoryItem;
import com.example.dima.wallpaper.viewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {
    private static CategoryFragment sCategoryFragment = null;
    private FirebaseDatabase database;
    private DatabaseReference categoryBackground;

    private FirebaseRecyclerOptions<CategoryItem> options;
    private FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;

    private RecyclerView mRecyclerView;

    public CategoryFragment() {
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.CATEGORY_BACKGROUND);
        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(categoryBackground, CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final CategoryItem model) {

                Picasso.with(getActivity())
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.backgroundImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if error
                                Picasso.with(getActivity())
                                        .load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.backgroundImage, new Callback() {
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

                holder.categoryName.setText(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //For detail category
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_item, parent, false);

                return new CategoryViewHolder(view);
            }
        };
    }

    public static CategoryFragment getInstance() {
        if (sCategoryFragment == null) {
            sCategoryFragment = new CategoryFragment();
        }
        return sCategoryFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_category);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setCategory();
        return view;
    }

    private void setCategory() {
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
        {
            adapter.startListening();
        }
    }
}
