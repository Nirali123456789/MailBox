package com.aiemail.superemail.Adapters.SectionAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.R;

class MyItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView tvItem;

    public MyItemViewHolder(View itemView) {
        super(itemView);

        tvItem = (TextView) itemView.findViewById(R.id.txt_source_name);
    }
}