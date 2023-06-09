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
//
//    public static final int SHREK_HOLDER = 0x00;
//    public static final int KITTEN_HOLDER = 0x01;
    ReportListFragment fragment;
    //private final ArrayList<House> mHolderList;
    private List<RealmHouse> houseList = Collections.emptyList();// = new ArrayList<>();;

    public ReportListAdapter(ReportListFragment fragment) {
//, List<House> houses
        this.fragment = fragment;
        //this.houseList = houses;
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

//        Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));

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

        RealmHouse viewHolder = houseList.get(position);

        //return viewHolder.getType();


        return -1;
    }
//
//    public void addItem(int viewType)
//    {
//        houseList.add(new House());
//        notifyItemInserted(houseList.size()-1);
//    }
//
//    public void removeItem(int position)
//    {
////        Log.i(TAG, "removeItem; size: "
////                + mHolderList.size()
////                + " position: "
////                + position);
//
//        if(houseList.isEmpty() || position < 0)
//        {
////            Log.i(TAG, "removeItem; isEmpty!;");
//            return;
//        }
//
//        houseList.remove(position);
//        notifyItemRemoved(position);
//    }


    public void onRecyclerViewItemClick(int position) {
        fragment.onRecyclerViewItemClick(position);
    }

}
