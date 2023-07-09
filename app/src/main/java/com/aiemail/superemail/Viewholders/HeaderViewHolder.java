package com.aiemail.superemail.Viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.R;
import com.aiemail.superemail.Adapters.SectionAdapter.HeadlineAdapter;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

	public final TextView txtSourceName;
	//private final RecyclerView rvHeadline;

	public HeaderViewHolder(@NonNull View itemView) {
		super(itemView);

		txtSourceName = itemView.findViewById(R.id.txt_source_name);
//		rvHeadline = itemView.findViewById(R.id.rv_headline);
	}

}