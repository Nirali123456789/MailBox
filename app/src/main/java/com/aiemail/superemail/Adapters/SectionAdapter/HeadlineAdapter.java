package com.aiemail.superemail.Adapters.SectionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aiemail.superemail.R;
import com.aiemail.superemail.Viewholders.HeadlineViewHolder;
import com.aiemail.superemail.Models.Email;

import java.util.List;

public class HeadlineAdapter extends RecyclerView.Adapter<HeadlineViewHolder> {

	private final Context context;
	private final List<Email> articleList;

	private static final String TAG = "HeadlineAdapter";

	public HeadlineAdapter(Context context, List<Email> articleList) {
		this.context = context;
		this.articleList = articleList;
	}

	@NonNull
	@Override
	public HeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.outer_layout, parent, false);
		return new HeadlineViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull HeadlineViewHolder holder, int position) {
		Email article = articleList.get(position);
		//holder.setSourceName(article.getSource().getName());
		holder.setTitle(article.getTitle());
		holder.setDate("Yesterday");

		holder.setThumbnail(context, article.getUrlToImage());
	}

	@Override
	public int getItemCount() {
		return articleList == null ? 0 : articleList.size();
	}
}