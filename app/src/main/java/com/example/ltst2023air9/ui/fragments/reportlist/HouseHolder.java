package com.example.ltst2023air9.ui.fragments.reportlist;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmHouse;

public class HouseHolder extends RecyclerView.ViewHolder
{


    public static final String TAG = HouseHolder.class.getSimpleName();

    private final TextView mTextView;

    private ReportListAdapter mAdapter;

    private RealmHouse realmHouse;

    public HouseHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.li_house_text);
        itemView.findViewById(R.id.li_house_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AppDelegate appDelegate = (AppDelegate) v.getContext().getApplicationContext();
                appDelegate.setCurrentRealmHouseId(realmHouse.getId());
                mAdapter.onRecyclerViewItemClick(getAdapterPosition());
            }
        });
    }

    public void bind(RealmHouse house)
    {
//        Log.i(TAG, "bind: " + getAdapterPosition());
        this.realmHouse = house;

        mTextView.setText(house.getName());

    }

    public HouseHolder builder(ReportListAdapter adapter)
    {
        mAdapter = adapter;
        return this;
    }

}
