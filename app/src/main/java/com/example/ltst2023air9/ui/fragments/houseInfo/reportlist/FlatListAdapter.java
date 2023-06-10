package com.example.ltst2023air9.ui.fragments.houseInfo.reportlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.HouseInfoFragment;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmFlat;

import java.util.Collections;
import java.util.List;

public class FlatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = FlatListAdapter.class.getSimpleName();

    HouseInfoFragment fragment;
    //private final ArrayList<House> mHolderList;
    private List<RealmFlat> flatList = Collections.emptyList();// = new ArrayList<>();;

    public FlatListAdapter(HouseInfoFragment fragment) {
        this.fragment = fragment;
    }

    public void setData(List<RealmFlat> details) {
        if (details == null) {
            details = Collections.emptyList();
        }
        this.flatList = details;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

//        Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));

        return new FlatHolder(
                inflater.inflate(R.layout.li_flat, parent, false)
        ).builder(this);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FlatHolder flatHolder = (FlatHolder) holder;
        flatHolder.bind(flatList.get(position));
    }

    @Override
    public int getItemCount() {
        return flatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    public void onRecyclerViewItemClick(int position) {
        fragment.onRecyclerViewItemClick(position);
    }

}
