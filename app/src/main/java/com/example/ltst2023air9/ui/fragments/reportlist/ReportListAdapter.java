package com.example.ltst2023air9.ui.fragments.reportlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.House;

import java.util.ArrayList;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final String TAG = ReportListAdapter.class.getSimpleName();
//
//    public static final int SHREK_HOLDER = 0x00;
//    public static final int KITTEN_HOLDER = 0x01;

    //private final ArrayList<House> mHolderList;
    private final List<House> houseList;// = new ArrayList<>();;

    ReportListFragment fragment;

    public ReportListAdapter(ReportListFragment fragment, List<House> houses) {

        this.fragment = fragment;
        this.houseList = houses;
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
                HouseHolder kittenHolder = (HouseHolder) holder;
                kittenHolder.bind(houseList.get(position));
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    @Override
    public int getItemViewType(int position) {

        House viewHolder = houseList.get(position);

        //return viewHolder.getType();


        return -1;
    }

    public void addItem(int viewType)
    {
        houseList.add(new House());
        notifyItemInserted(houseList.size()-1);
    }

    public void removeItem(int position)
    {
//        Log.i(TAG, "removeItem; size: "
//                + mHolderList.size()
//                + " position: "
//                + position);

        if(houseList.isEmpty() || position < 0)
        {
//            Log.i(TAG, "removeItem; isEmpty!;");
            return;
        }

        houseList.remove(position);
        notifyItemRemoved(position);
    }


    public void onRecyclerViewItemClick(int position) {
        fragment.onRecyclerViewItemClick(position);
    }
}
