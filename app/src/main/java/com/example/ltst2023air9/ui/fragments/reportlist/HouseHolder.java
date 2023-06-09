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
//    private static AtomicInteger sCurrentItemCounter = new AtomicInteger(0);
//
//    synchronized public static int nextItem()
//    {
//        return sCurrentItemCounter.getAndIncrement();
//    }

    public static final String TAG = HouseHolder.class.getSimpleName();

//
//    private final ImageView mImageView;
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

                Log.i("List onClick", "house id " + realmHouse.getId());
                Log.i("List onClick", "house name " + realmHouse.getName());

                mAdapter.onRecyclerViewItemClick(getAdapterPosition());
                // getLayoutPosition() - позиция на вьюшке?
                // getAdapterPosition() - позиция в списке?
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
