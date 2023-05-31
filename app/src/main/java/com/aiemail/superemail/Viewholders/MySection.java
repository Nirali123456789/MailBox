package com.aiemail.superemail.Viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.aiemail.superemail.R;
import com.aiemail.superemail.feature.Models.Article;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class MySection extends Section {
    ArrayList<Article> itemList = new ArrayList<>();

    public MySection(ArrayList<Article> articles) {

        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.outer_layout)
                .headerResourceId(R.layout.title_layout)
                .build());
        itemList=articles;

    }

    @Override
    public int getContentItemsTotal() {
        return itemList.size(); // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

        // bind your view here
        itemHolder.tvItem.setText(itemList.get(position).getTitle());
    }
    
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        NewsViewHolder itemHolder = (NewsViewHolder) holder;
        itemHolder.txtSourceName.setText(getHeaderResourceId());
    }
}