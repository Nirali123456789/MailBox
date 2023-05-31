package com.aiemail.superemail.Viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.aiemail.superemail.R;

public class HeadlineViewHolder extends RecyclerView.ViewHolder {

	private final TextView txtTitle;
	private final TextView txtSourceName;
	private final TextView txtDate;
	private final ImageView imgThumbnail;

	public HeadlineViewHolder(@NonNull View itemView) {
		super(itemView);

		txtTitle = itemView.findViewById(R.id.txt_title);
		txtSourceName = itemView.findViewById(R.id.txt_source_name);
		txtDate = itemView.findViewById(R.id.txt_date);
		imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
	}

	public void setTitle(String title) {
		this.txtTitle.setText(title);
	}

	public void setSourceName(String sourceName) {
		this.txtSourceName.setText(sourceName);
	}

	public void setDate(String date) {
		this.txtDate.setText(date);
	}

	public void setThumbnail(Context context, String url) {
		Glide.with(context)
				.load(url)
				.centerCrop()
				.placeholder(R.drawable.pic4)
				.apply(new RequestOptions().override(100, 100))
				.into(this.imgThumbnail);
	}
}
