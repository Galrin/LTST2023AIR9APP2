package com.example.ltst2023air9.ui.fragments.houseInfo.reportlist;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmFlat;

import java.text.BreakIterator;

public class FlatHolder extends RecyclerView.ViewHolder
{
//    private static AtomicInteger sCurrentItemCounter = new AtomicInteger(0);
//
//    synchronized public static int nextItem()
//    {
//        return sCurrentItemCounter.getAndIncrement();
//    }

    public static final String TAG = FlatHolder.class.getSimpleName();

//
//    private final ImageView mImageView;
    private final TextView mTextView;

    private FlatListAdapter mAdapter;

    private RealmFlat realmFlat;
    private TextView mTextViewFloor;

    public FlatHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.li_flat_text);
        mTextViewFloor = itemView.findViewById(R.id.li_floor_text);
        itemView.findViewById(R.id.li_flat_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                AppDelegate appDelegate = (AppDelegate) v.getContext().getApplicationContext();
                appDelegate.setCurrentRealmHouseId(realmFlat.getId());

                Log.i("List onClick", "flat id " + realmFlat.getId());
                Log.i("List onClick", "flat number " + realmFlat.getNumber());

                mAdapter.onRecyclerViewItemClick(getAdapterPosition());
                // getLayoutPosition() - позиция на вьюшке?
                // getAdapterPosition() - позиция в списке?
            }
        });
    }

    public void bind(RealmFlat flat)
    {
//        Log.i(TAG, "bind: " + getAdapterPosition());
        this.realmFlat = flat;

        mTextView.setText(String.valueOf(flat.getNumber()));
        mTextViewFloor.setText(String.valueOf(flat.getFloor()));

    }

    public FlatHolder builder(FlatListAdapter adapter)
    {
        mAdapter = adapter;
        return this;
    }

}
