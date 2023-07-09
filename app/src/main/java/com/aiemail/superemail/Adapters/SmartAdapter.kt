package com.aiemail.superemail.Adapters


import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.R
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.prefs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class SmartAdapter(
    context: Activity,
    private val listName: List<AllMails>,

    var showselect: (Boolean) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isEnable = false
    private var fromlongclick = false
    private val itemselectedList = mutableListOf<AllMails>()
    private var checkedPosition = 0
    val CITY_TYPE = 0
    val COUNTRY_TYPE = 1
    val MORE = 3
    private var context: Activity
    private var sourceList: HashMap<Int, List<AllMails>> = hashMapOf()
     var keys: List<Int> = listOf()
    private var sourceListselect: ArrayList<AllMails> = arrayListOf()
    private lateinit var mRecentlyDeletedItem: AllMails
    var mRecentlyDeletedItemPosition = 0
    var value: ArrayList<AllMails> = arrayListOf()

    init {

        Log.d("TAG", "NewsAdapter: " + sourceList.size)
        this.context = context
        this.sourceList = sourceList
        keys = ArrayList(sourceList!!.keys)

    }


    var isselectall: Boolean = false


    fun showUndoSnackbar(activity: Activity) {
        val view = activity.findViewById<View>(R.id.appbar)
        val snackbar = Snackbar.make(
            view, R.string.snack_bar_text,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.snack_bar_undo) { v: View? -> undoDelete() }
        snackbar.show()
    }

    private fun undoDelete() {
//        sourceList.add(
//            mRecentlyDeletedItemPosition,
//            mRecentlyDeletedItem
//        )
//        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        Log.d("TAG", "onCreateViewHolder: " + viewType)
        when (viewType) {
            CITY_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.title_layout, parent, false)
                return SectionHeaderVH(view)
            }

            COUNTRY_TYPE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.outer_layout, parent, false)
                return SectionItemVH(view)
            }

            MORE -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.show_all_layout, parent, false)
                return SectionMore(view)
            }
        }
        //  return null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_layout, parent, false)
        return SectionItemVH(view)
    }





    fun getKey(position: Int): Int? {
        Log.e("getKey", "getKey: "+position+"??"+keys)
        return keys!!.get(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if (getItemViewType(position) != 4) {
            val key: Int? = getKey(position)
           // Log.e("TAG", "onBindViewHolder: " + sourceList)
            val `object` = sourceList.get(key)
            value = `object` as ArrayList<AllMails>
            when (getKey(position)) {
                CITY_TYPE -> (holder as SectionHeaderVH?)!!.tvHeader.text =
                    "" + value!!.get(key!!).type

                MORE -> (holder as SectionMore?)!!.showall.text = "Show More"
                COUNTRY_TYPE -> {
                    // sourceListselect.add(`object`)

                    (holder as SectionItemVH?)!!.textView.text =
                        spiltString(value!!.get(key!!).title!!)
                    (holder as SectionItemVH?)!!.txtSourceName.text = value!!.get(key!!).content
                    (holder as SectionItemVH?)!!.txtsubtitle.text = value!!.get(key!!).author
                    (holder as SectionItemVH?)!!.txtDate.text = value!!.get(key!!).date
                    Glide.with(context)
                        .load(value!!.get(key!!).url)
                        .centerCrop()
                        .placeholder(R.drawable.pic4)
                        .apply(RequestOptions().override(100, 100))
                        .into((holder as SectionItemVH?)!!.img_thumbnail)
                    // viewBinderHelper.bind(holder.swipe,  sourceList.get(position).title);
                    // viewBinderHelper.setOpenOnlyOne(true)
                    // viewBinderHelper.closeLayout( sourceList.get(position).title)

//                    (holder as SectionItemVH).outer.setOnLongClickListener {
//
//                        selectItem(holder, `object`, position)
//                        var index = getCategoryPos(`object`)
//
//
//                        showselect(true)
//                        holder.selection.visibility = View.VISIBLE
//                        isEnable = true
//                        fromlongclick = true
//                        notifyDataSetChanged()
//                        true
//
//                    }
//                    (holder as SectionItemVH).outer.setOnClickListener {
//                        Log.d(
//                            "TAG",
//                            "onBindViewHolder: " + itemselectedList.size + ">" + ">" + position + ">>" + `object`.title
//                        )
//                        var index = getCategoryPos(`object`)
//                        if (itemselectedList.size != 0)
//                            Log.d(
//                                "TAG",
//                                "onBindViewHolder: " + itemselectedList.size + ">" + ">" + index + ">>" + `object`.title
//                            )
//
//                        if (itemselectedList.contains(`object`)) {
//                            itemselectedList.removeAt(index)
//                            holder.selection.visibility = View.GONE
//
//                            `object`.isselected = false
//                            if (itemselectedList.isEmpty()) {
//                                showselect(false)
//                                isEnable = false
//                            }
//                            Log.d(
//                                "TAG",
//                                "onBindViewHolder:itemselectedlist " + itemselectedList.size
//                            )
//                            if (itemselectedList.size == 0) {
//                                notifyDataSetChanged()
//                            }
//
//
//                        } else {
//                            if (isEnable) {
//                                isEnable = false
//
//                              //  selectItem(holder, `object`, position)
//                                holder.selection.visibility = View.VISIBLE
//                                showselect(true)
//                            } else {
//                                context.startActivity(
//                                    Intent(
//                                        context,
//                                        ComposeActivity::class.java
//                                    ).putExtra("id", position)
//                                )
//                                context.overridePendingTransition(
//                                    R.anim.slide_in_up,
//                                    R.anim.slide_out_up
//                                )
//                            }
//
//
//                        }
//                    }
                    if (isEnable && isselectall) {
                        holder.selection.visibility = View.VISIBLE
                        holder.img_thumbnail.setImageResource(R.drawable.ic_unselect)

                    } else {
                        if (isEnable) {
                            //holder.selection.visibility = View.VISIBLE
                            // holder.img_thumbnail.setImageResource(R.drawable.ic_unselect)
                            if (fromlongclick) {
                                holder.img_thumbnail.setImageResource(R.drawable.ic_unselect)
                            }
                        } else {
                            holder.selection.visibility = View.GONE
                            holder.img_thumbnail.visibility = View.VISIBLE
                        }

                    }


                }


            }

        }else
        Log.e("TAG", "onBindViewHolder: "+"44")


    }

    private fun getCategoryPos(category: AllMails): Int {
        return itemselectedList.indexOf(category)
    }

    private fun selectItem(holder: SectionItemVH, article: AllMails, position: Int) {
        isEnable = true
        itemselectedList.add(article)
        article.isselected = true
        holder.selection.visibility = View.VISIBLE
        Log.d("TAG", "onBindViewHolder: " + article.id)


    }

    override fun getItemCount(): Int {
        return sourceList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (keys.size!=0) {
            return getKey(position)!!
        }

           return 4

    }

    fun adddata(articles: HashMap<Int, List<AllMails>>) {
        keys = ArrayList(sourceList!!.keys)
        Log.d("fetchAllData", "fetchAllData>>: " + articles.size)
        sourceList = articles
        //notifyItemInserted((sourceList.size-1))
     notifyItemInserted(sourceList.size-1)

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

    internal inner class SectionItemVH(itemView: View) :  RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var img_thumbnail: ImageView
        var selection: ImageView
        val txtSourceName: TextView
        val txtsubtitle: TextView
        val txtDate: TextView
        val outer: LinearLayout
        //  lateinit var swipe: SwipeRevealLayout

        init {

            outer = itemView.findViewById(R.id.outer_layout)
            textView = itemView.findViewById(R.id.txt_title)
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail)
            selection = itemView.findViewById(R.id.selection)
            txtSourceName = itemView.findViewById(R.id.sender)
            txtsubtitle = itemView.findViewById(R.id.txt_source_name)
            txtsubtitle.minLines= prefs.NoOfLine!!
            txtDate = itemView.findViewById(R.id.txt_date)
            // swipe = itemView.findViewById(R.id.swipe_layoutmain)
            itemView.setLongClickable(true);


            itemView.setOnClickListener {

                if (checkedPosition !== adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }
        }




    }


    internal inner class SectionHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvHeader: TextView

        init {
            tvHeader = itemView.findViewById(R.id.txt_source_name)
        }
    }

    internal inner class SectionMore(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var showall: TextView

        init {
            showall = itemView.findViewById(R.id.showall)
        }
    }
}