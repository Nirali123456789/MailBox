package com.aiemail.superemail.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.Models.Email;
import com.aiemail.superemail.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> {
    private ArrayList<Email> mDataset = new ArrayList<>();
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(ArrayList<Email> myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
    }

   public void addIteams(ArrayList<Email> val) {
       mDataset=new ArrayList<>();
        mDataset.addAll(val);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_item, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {

        fruitViewHolder.mTextView.setText(mDataset.get(i).getSender());
       fruitViewHolder.body.setText(mDataset.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public TextView body;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);
            body = (TextView) v.findViewById(R.id.body);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset.get(getAbsoluteAdapterPosition()));

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(Email data);
    }
}