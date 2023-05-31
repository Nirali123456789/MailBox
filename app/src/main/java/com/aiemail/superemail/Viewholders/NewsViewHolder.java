package com.aiemail.superemail.Viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.R;
import com.aiemail.superemail.Adapters.SectionAdapter.HeadlineAdapter;

public class NewsViewHolder extends RecyclerView.ViewHolder {

	public final TextView txtSourceName;
	//private final RecyclerView rvHeadline;

	public NewsViewHolder(@NonNull View itemView) {
		super(itemView);

		txtSourceName = itemView.findViewById(R.id.txt_source_name);
//		rvHeadline = itemView.findViewById(R.id.rv_headline);
	}

	public void setSourceName(String sourceName) {
		this.txtSourceName.setText(sourceName);
	}

	public void setupRecyclerView(Context context, HeadlineAdapter adapter) {
		//rvHeadline.setLayoutManager(new LinearLayoutManager(context));
		//rvHeadline.setAdapter(adapter);
	}
}