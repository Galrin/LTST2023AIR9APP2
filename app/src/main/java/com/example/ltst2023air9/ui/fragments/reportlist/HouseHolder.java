package com.example.ltst2023air9.ui.fragments.reportlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.House;

import java.util.concurrent.atomic.AtomicInteger;

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

    public HouseHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.li_house_text);
        itemView.findViewById(R.id.li_house_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                mAdapter.onRecyclerViewItemClick(getAdapterPosition());
                // getLayoutPosition() - позиция на вьюшке?
                // getAdapterPosition() - позиция в списке?
            }
        });
    }

    public void bind(House house)
    {
//        Log.i(TAG, "bind: " + getAdapterPosition());

        mTextView.setText(house.getName());

    }

    public HouseHolder builder(ReportListAdapter adapter)
    {
        mAdapter = adapter;
        return this;
    }

}
