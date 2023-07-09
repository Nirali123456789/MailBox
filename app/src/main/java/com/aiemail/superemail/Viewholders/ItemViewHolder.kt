package com.aiemail.superemail.Viewholders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.R
import com.aiemail.superemail.prefs
import com.aiemail.superemail.utilis.Prefs

internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val img_thumbnail: ImageView
    val selection: ImageView
    val sender: TextView
    val subject: TextView
    val txtDate: TextView
    val body: TextView
    val outer: LinearLayout

    init {

        outer = itemView.findViewById(R.id.outer_layout)
        img_thumbnail = itemView.findViewById(R.id.img_thumbnail)
        selection = itemView.findViewById(R.id.selection)
        sender = itemView.findViewById(R.id.sender)
        subject = itemView.findViewById(R.id.subject)
        body = itemView.findViewById(R.id.body)
        body.maxLines= prefs.NoOfLine!!
        txtDate = itemView.findViewById(R.id.txt_date)
    }
}