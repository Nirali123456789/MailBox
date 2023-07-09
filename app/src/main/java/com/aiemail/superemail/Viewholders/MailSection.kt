package com.aiemail.superemail.Viewholders

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.Activities.ComposeActivity
import com.aiemail.superemail.Adapters.SmartAdapter
import com.aiemail.superemail.R
import com.aiemail.superemail.Models.AllMails
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class MailSection(
    articles: ArrayList<AllMails>,
    key: String,
    activity: Activity,
    clickListener: ClickListener, var showselect: (Boolean) -> Unit
) : Section(
    (SectionParameters.builder()
        .itemResourceId(R.layout.outer_layout)
        .headerResourceId(R.layout.title_layout)
        .footerResourceId(R.layout.show_all_layout)
        .build())
) {
    var itemList = ArrayList<AllMails>()
    val activity = activity

    var key: String = ""
    private var isEnable = false
    private var fromlongclick = false
    private val clickListener: ClickListener = clickListener
    private val itemselectedList = mutableListOf<AllMails>()

    init {

        // call constructor with layout resources for this Section header and items
        itemList = articles
        this.key = key

    }

    override fun getContentItemsTotal(): Int {
        return itemList.size // number of items of this section
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        // return a custom instance of ViewHolder for the items of this section
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder


        // AllMail Single Object

        var value = itemList[position]
        Log.d("TAG", "onBindItemViewHolder: " + value)

        itemHolder.sender.text = spiltString(value.sender!!)
        itemHolder.subject.text = value.author

        itemHolder.body.text = value.content


        itemHolder.txtDate.text = value.date
        Glide.with(activity)
            .load(value.url)
            .centerCrop()
            .placeholder(R.drawable.pic4)
            .apply(RequestOptions().override(100, 100))
            .into((itemHolder.img_thumbnail))



        itemHolder.outer.setOnLongClickListener {

            selectItem(holder, value, position)
            var index = position


            showselect(true)
            holder.selection.visibility = View.VISIBLE
            isEnable = true
            fromlongclick = true

            true

        }
        itemHolder.outer.setOnClickListener {

            var index = position
            if (itemselectedList.size != 0)
                Log.d(
                    "TAG",
                    "onBindViewHolder: " + itemselectedList.size + ">" + ">" + index + ">>" + value.title
                )

            if (itemselectedList.contains(value)) {
                itemselectedList.removeAt(index)
                holder.selection.visibility = View.GONE

                value.isselected = false
                if (itemselectedList.isEmpty()) {
                    showselect(false)
                    isEnable = false
                }
                Log.d(
                    "TAG",
                    "onBindViewHolder:itemselectedlist " + itemselectedList.size
                )
                if (itemselectedList.size == 0) {

                }


            } else {
                if (isEnable) {
                    isEnable = false

                    //  selectItem(holder, `object`, position)
                    holder.selection.visibility = View.VISIBLE
                    showselect(true)
                } else {
                    clickListener.onItemRootViewClicked(value, position)
                    Log.d("TAG", "onBindViewHolder: " + value)

                    // }


                }
            }

        }
    }
    private fun selectItem(holder: ItemViewHolder, article: AllMails, position: Int) {
        isEnable = true
        itemselectedList.add(article)
        article.isselected = true
        holder.selection.visibility = View.VISIBLE
        Log.d("TAG", "onBindViewHolder: " + article.id)


    }

    override fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder) {
        val footerHolder = holder as FooterViewHolder
        footerHolder.rootView.setOnClickListener { v ->
            clickListener.onFooterRootViewClicked(
                this,
                key
            )
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        // return an empty instance of ViewHolder for the headers of this section
        return HeaderViewHolder(view)
    }

    override fun getFooterViewHolder(view: View?): RecyclerView.ViewHolder? {
        return FooterViewHolder(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        Log.d("TAG", "onBindHeaderViewHolder: " + holder)

            val itemHolder = holder as HeaderViewHolder
            if (itemList.size != 0 && holder.absoluteAdapterPosition != -1) {
                if (key.equals("INBOX")) {
                    key = "Personal"
                }
                if (key.equals("CATEGORY_PROMOTIONS")) {
                    key = "Newsletters"
                }
                itemHolder.txtSourceName.text = key
                Log.d("TAG", "onBindHeaderViewHolder: " + key)
            }
        }



    fun spiltString(someText: String): String {
        var spiltString = someText.split("<")
        Log.d("TAG", "spiltString: " + spiltString)
        for (substring in spiltString) {
            if (!substring.isEmpty()) {
                return substring
            }
        }
        return ""
    }

    interface ClickListener {
        fun onItemRootViewClicked(
            section: AllMails,
            itemAdapterPosition: Int
        )

        fun onFooterRootViewClicked(
            section: MailSection,
            itemAdapterPosition: String
        )
    }
}