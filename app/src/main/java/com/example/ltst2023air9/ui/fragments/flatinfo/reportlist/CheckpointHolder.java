package com.example.ltst2023air9.ui.fragments.flatinfo.reportlist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmCheckpoint;

public class CheckpointHolder extends RecyclerView.ViewHolder {


    public static final String TAG = CheckpointHolder.class.getSimpleName();

    private final TextView mTextView;

    private CheckpointListAdapter mAdapter;

    private RealmCheckpoint realmCheckpoint;

    public CheckpointHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.li_checkpoint_text);
        itemView.findViewById(R.id.li_checkpoint_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onRecyclerViewItemClick(getAdapterPosition(), realmCheckpoint);
            }
        });
    }

    public void bind(RealmCheckpoint realmCheckpoint) {
        this.realmCheckpoint = realmCheckpoint;
        mTextView.setText(String.valueOf(realmCheckpoint.getName()));
    }

    public CheckpointHolder builder(CheckpointListAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

}
