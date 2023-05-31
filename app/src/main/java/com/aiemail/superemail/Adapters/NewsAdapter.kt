package com.aiemail.superemail.Adapters


import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.Activities.ComposeActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.feature.Models.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar


class NewsAdapter(
    context: Activity,
    sourceList: ArrayList<Article>,
    var showselect: (Boolean) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isEnable = false
    private var fromlongclick = false
    private val itemselectedList = mutableListOf<Article>()
    private var checkedPosition = 0
    val CITY_TYPE = 0
    val COUNTRY_TYPE = 1
    private var context: Activity
    private var sourceList: ArrayList<Article> = arrayListOf()
    private var sourceListselect: ArrayList<Article> = arrayListOf()
    private lateinit var mRecentlyDeletedItem: Article
    var mRecentlyDeletedItemPosition = 0

    init {

        Log.d("TAG", "NewsAdapter: " + sourceList.size)
        this.context = context
        this.sourceList = sourceList

    }

    fun deleteItem(position: Int, a: Activity) {
        mRecentlyDeletedItem = sourceList!![position]
        mRecentlyDeletedItemPosition = position
        sourceList.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar(a)
    }

    var isselectall: Boolean = false
    fun getAllList(select: Boolean) {
        isselectall = select
        showselect(select)
        isEnable = select
        notifyDataSetChanged()

    }

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
        sourceList.add(
            mRecentlyDeletedItemPosition,
            mRecentlyDeletedItem
        )
        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
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
        }
        //  return null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_layout, parent, false)
        return SectionItemVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val `object` = sourceList!![position]

        if (`object` != null) {
            when (`object`.type) {
                CITY_TYPE -> (holder as SectionHeaderVH?)!!.tvHeader.text = "" + `object`.title
                COUNTRY_TYPE -> {
                    sourceListselect.add(`object`)
                    (holder as SectionItemVH?)!!.textView.text =spiltString(`object`.title!!)
                    (holder as SectionItemVH?)!!.txtSourceName.text = `object`.content
                    (holder as SectionItemVH?)!!.txtsubtitle.text = `object`.author
                    (holder as SectionItemVH?)!!.txtDate.text = `object`.date
                    Glide.with(context)
                        .load(`object`.url)
                        .centerCrop()
                        .placeholder(R.drawable.pic4)
                        .apply(RequestOptions().override(100, 100))
                        .into((holder as SectionItemVH?)!!.img_thumbnail)
                   // viewBinderHelper.bind(holder.swipe,  sourceList.get(position).title);
                   // viewBinderHelper.setOpenOnlyOne(true)
                   // viewBinderHelper.closeLayout( sourceList.get(position).title)

                    (holder as SectionItemVH).outer.setOnLongClickListener {

                        selectItem(holder, `object`, position)
                        var index = getCategoryPos(`object`)


                        showselect(true)
                        holder.selection.visibility = View.VISIBLE
                        isEnable = true
                        fromlongclick = true
                        notifyDataSetChanged()
                        true

                    }
                    (holder as SectionItemVH).outer.setOnClickListener {
                        Log.d(
                            "TAG",
                            "onBindViewHolder: " + itemselectedList.size + ">" + ">" +position+ ">>" + `object`.title)
                        var index = getCategoryPos(`object`)
                        if (itemselectedList.size != 0)
                            Log.d(
                                "TAG",
                                "onBindViewHolder: " + itemselectedList.size + ">" + ">" + index + ">>" + `object`.title)

                        if (itemselectedList.contains(`object`)) {
                            itemselectedList.removeAt(index)
                            holder.selection.visibility = View.GONE

                            `object`.isselected = false
                            if (itemselectedList.isEmpty()) {
                                showselect(false)
                                isEnable = false
                            }
                            Log.d(
                                "TAG",
                                "onBindViewHolder:itemselectedlist " + itemselectedList.size
                            )
                            if (itemselectedList.size == 0) {
                                notifyDataSetChanged()
                            }


                        } else {
                            if (isEnable) {
                                isEnable = false

                                selectItem(holder, `object`, position)
                                holder.selection.visibility = View.VISIBLE
                                showselect(true)
                            } else {
                                context.startActivity(Intent(context, ComposeActivity::class.java).putExtra("id",position))
                                context.overridePendingTransition(
                                    R.anim.slide_in_up,
                                    R.anim.slide_out_up
                                )
                            }


                        }
                    }
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

        }


    }

    private fun getCategoryPos(category: Article): Int {
        return itemselectedList.indexOf(category)
    }

    private fun selectItem(holder: SectionItemVH, article: Article, position: Int) {
        isEnable = true
        itemselectedList.add(article)
        article.isselected = true
        holder.selection.visibility = View.VISIBLE
        Log.d("TAG", "onBindViewHolder: " + article.id)


    }

    override fun getItemCount(): Int {
        return sourceList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        if (sourceList != null) {
            val `object` = sourceList[position]
            if (`object` != null) {
                return `object`.type
            }
        }
        return 0
    }

    fun adddata(articles: MutableList<Article>?) {
        sourceList= articles as ArrayList<Article>
        //notifyItemInserted((sourceList.size-1))
        notifyDataSetChanged()

    }
fun spiltString(someText:String):String
{
   var spiltString=  someText.split("<")
    Log.d("TAG", "spiltString: "+spiltString)
    for (substring in spiltString) {
        if (!substring.isEmpty()) {
          return substring
        }
    }
    return ""
}
    internal inner class SectionItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            txtSourceName = itemView.findViewById(R.id.txt_source_name_sub)
            txtsubtitle = itemView.findViewById(R.id.txt_source_name)
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
}