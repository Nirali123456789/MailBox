package com.aiemail.superemail.Viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.R;

class MyItemViewHolder extends RecyclerView.ViewHolder {
     final TextView tvItem;

    public MyItemViewHolder(View itemView) {
        super(itemView);

        tvItem = (TextView) itemView.findViewById(R.id.txt_title);
    }
}