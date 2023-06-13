package com.example.ltst2023air9.ui.fragments.reportlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmHouse;

import java.util.Collections;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = ReportListAdapter.class.getSimpleName();

    ReportListFragment fragment;
    private List<RealmHouse> houseList = Collections.emptyList();// = new ArrayList<>();;

    public ReportListAdapter(ReportListFragment fragment) {
        this.fragment = fragment;
    }

    public void setData(List<RealmHouse> details) {
        if (details == null) {
            details = Collections.emptyList();
        }
        this.houseList = details;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new HouseHolder(
                inflater.inflate(R.layout.li_house, parent, false)
        ).builder(this);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HouseHolder houseHolder = (HouseHolder) holder;
        houseHolder.bind(houseList.get(position));
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }



    public void onRecyclerViewItemClick(int position) {
        fragment.onRecyclerViewItemClick(position);
    }

}
