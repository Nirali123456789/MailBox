package com.aiemail.superemail.feature.utilis

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.R
import com.aiemail.superemail.Adapters.ListEmailAdapter


class SwipeToDeleteCallback(private val mAdapter: ListEmailAdapter, c: Activity) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val icon: Drawable?
    private val readicon: Drawable?
    private val background: ColorDrawable
    private val backgroundread: ColorDrawable
    var acc: Activity

    init {
        icon = ContextCompat.getDrawable(
            c,
            R.drawable.ic_bin
        )
        background = ColorDrawable(Color.RED)
        readicon = ContextCompat.getDrawable(
            c,
            R.drawable.ic_archive
        )
        backgroundread = ColorDrawable(Color.GREEN)
        acc = c
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // used for up and down movements
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        if (direction == ItemTouchHelper.RIGHT) {
        }
    }
}



