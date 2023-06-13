package com.example.ltst2023air9.ui.fragments.flatinfo.reportlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.ui.fragments.FlatInfoFragment;

import java.util.Collections;
import java.util.List;

public class CheckpointListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = CheckpointListAdapter.class.getSimpleName();

    FlatInfoFragment fragment;
    private List<RealmCheckpoint> realmCheckpointList = Collections.emptyList();// = new ArrayList<>();;

    public CheckpointListAdapter(FlatInfoFragment fragment) {
        this.fragment = fragment;
    }

    public void setData(List<RealmCheckpoint> details) {
        if (details == null) {
            details = Collections.emptyList();
        }
        this.realmCheckpointList = details;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

//        Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));

        return new CheckpointHolder(
                inflater.inflate(R.layout.li_chckpoint, parent, false)
        ).builder(this);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckpointHolder flatHolder = (CheckpointHolder) holder;
        flatHolder.bind(realmCheckpointList.get(position));
    }

    @Override
    public int getItemCount() {
        return realmCheckpointList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    public void onRecyclerViewItemClick(int position, RealmCheckpoint realmFlat) {
        fragment.onRecyclerViewItemClick(position, realmFlat);
    }

}
