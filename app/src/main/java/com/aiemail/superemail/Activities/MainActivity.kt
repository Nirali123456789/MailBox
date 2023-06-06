/*
 * Created by Nirali Pandya on 26/04/2023, 8:00 AM
 *     Copyright (c) 2022.
 *     All rights reserved.
 */

package com.aiemail.superemail.Activities


import android.app.ActivityOptions
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.Activities.LoginActivity.Companion.newsList1
import com.aiemail.superemail.Adapters.ListEmailAdapter
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Fragments.SearchFragment
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivityMain1Binding
import com.aiemail.superemail.feature.Models.Email

import com.aiemail.superemail.feature.Models.Source
import com.aiemail.superemail.feature.utilis.Constant
import com.aiemail.superemail.feature.viewmodel.EmailViewmodel
import com.ashraf007.expandableselectionview.ExpandableSingleSelectionView
import com.ashraf007.expandableselectionview.adapter.BasicStringAdapter
import com.ashraf007.expandableselectionview.view.ExpandableSelectionView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.ibrajix.nftapp.utilis.Utility.setTransparentStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


//@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener {

    private lateinit var navigation_view: NavigationView
    lateinit var drawer: DrawerLayout
    private lateinit var binding: ActivityMain1Binding

    private var listofmail: ArrayList<Email> = arrayListOf()

    private lateinit var listAdapter: ListEmailAdapter

    lateinit var groupList: ArrayList<String>
    lateinit var childList: ArrayList<String>
    lateinit var childListicon: ArrayList<Int>
    var mobileCollection: Map<String, List<String>> = hashMapOf()
    var searchFragment: SearchFragment = SearchFragment()
    private var mActive = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var p = Paint()

    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Constant.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        binding = ActivityMain1Binding.inflate(layoutInflater)
        SetUp()


    }

    private fun SetUp() {

        drawer = binding.drawer
        setContentView(binding.root)
        if (!isNotificationServiceEnabled()) {
            showPermissionDialog()
        }
        setSupportActionBar(findViewById(R.id.toolbar))

        createGroupList();
        createCollection();
        onclick()
        ExpandableUi()

//        listofmail= arrayListOf()
        fetchData()
        Register()
        UISetup()

    }

    private fun ExpandableUi() {

//       * Array for Expandable icons .
//        *SetUp ExpandableAdapter when List CLick It will Expand

        var icons = arrayListOf<Int>()
        icons = arrayListOf(
            R.drawable.ic_draft,
            R.drawable.ic_send,
            R.drawable.ic_delete,
            R.drawable.ic_send,
            R.drawable.ic_archive,
            R.drawable.ic_imp
        )

        var expandableListAdapter = BasicStringAdapter(childList, icons, "nehavaidya86@gmail.com")
        binding.root.findViewById<ExpandableSelectionView>(R.id.expandableListViewdrawer)
            .setAdapter(expandableListAdapter)
        var expandableSelectionView: ExpandableSingleSelectionView =
            binding.root.findViewById(R.id.expandableListViewdrawer)
        expandableSelectionView.selectionListener = { index: Int? ->
            binding.Title.visibility = View.VISIBLE
            binding.spinner.visibility = View.GONE
            binding.drop.visibility = View.GONE
            drawer.closeDrawers()
            when (index) {

                0 -> {

                    TypesData(5)
                    binding.Title.setText("Drafts")
                }

                1 -> {
                    TypesData(1)
                    binding.Title.setText("Sent")
                }

                2 -> {
                    TypesData(4)
                    binding.Title.setText("Trash")
                }

                3 -> {
                    TypesData(6)
                    binding.Title.setText("Spam")
                }

                4 -> {
                    TypesData(1)
                    binding.Title.setText("Archive")
                }

                5 -> {
                    TypesData(3)
                    binding.Title.setText("Important")
                }

                else -> {
                    TypesData(0)
                    binding.Title.setText("Smart")
                }


            }

        }

//        *Set EmailList Adapter To Recyclerview

        binding.list.apply {

            listAdapter = ListEmailAdapter(this@MainActivity, listofmail) { select ->
                visibleSelectall(select)
            }
            adapter = listAdapter
        }
    }

    private fun TypesData(index: Int) {
        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");
        var acc = GoogleSignIn.getLastSignedInAccount(this)
        model.DeleteMails()
        model.insertMail()
        (application as MyApplication).repository.fetchMails(
            this,
            username!!,
            username!!,
            acc!!,
            index
        )
    }

    private fun UISetup() {
        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpAdapterAndGetData()
        val popup = PopupMenu(this, binding.more)
        // popup.setOnMenuItemClickListener(this@MainActivity1)
        popup.inflate(R.menu.popup_menu)
        setspinner()

    }

    private fun Register() {
        var nReceiver = NotificationReceiver()
        val filter = IntentFilter()
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE")
        registerReceiver(nReceiver, filter)
    }

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat: String = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun onclick() {
        binding.more.setOnClickListener {
            setupmenu()
        }
        binding.compose.setOnClickListener {
            startActivity(Intent(this, DirectComposeActivity::class.java))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left);


        }
        binding.included.setting.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            updateUi()
            binding.Title.visibility = View.GONE
            binding.included.setting.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
            val options = ActivityOptions
                .makeSceneTransitionAnimation(this, binding.root, "robot")
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        binding.included.inbox.setOnClickListener {
            binding.Title.visibility = View.GONE
            binding.trashBin.visibility = View.GONE
            // binding.spinner.visibility=View.GONE
            binding.spinner.visibility = View.VISIBLE
            // binding.Title.setText("Sent")
            updateUi()
            binding.included.inbox.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
            var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(this)
            model.DeleteMails()
            model.insertMail()
            (application as MyApplication).repository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                2
            )

        }
        binding.included.sent.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.drop.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.Title.setText("Sent")
            updateUi()
            binding.included.sent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
            var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(this)
            model.DeleteMails()
            model.insertMail()
            (application as MyApplication).repository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                1
            )
        }
        binding.included.draft.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.drop.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.Title.setText("Draft")
            updateUi()
            binding.included.draft.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)

            var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(this)
            model.DeleteMails()
            model.insertMail()
            (application as MyApplication).repository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                5
            )
        }
        binding.included.pins.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Pins")
            updateUi()
            binding.included.pins.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)


        }
        binding.included.archive.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Archived")
            updateUi()
            binding.included.archive.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
        }
        binding.included.trash.setOnClickListener {
            binding.trashBin.visibility = View.VISIBLE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Trash")
            updateUi()
            binding.included.trash.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            drawer.closeDrawer(GravityCompat.START)
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(this)
            model.DeleteMails()
            model.insertMail()
            (application as MyApplication).repository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                4
            )
        }
        binding.included.snoozelayout.setOnClickListener {
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.Title.setText("Snoozed")
            updateUi()
            binding.included.snoozelayout.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    fun updateUi() {
        binding.included.setting.background = null
        binding.included.inbox.background = null
        binding.included.sent.background = null
        binding.included.draft.background = null
        binding.included.pins.background = null
        binding.included.archive.background = null
        binding.included.trash.background = null
        binding.included.snoozelayout.background = null
    }

    private fun fetchData() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = model.getEmailList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {

                    Log.d("TAG", "fetchAndProcess: " + it)
                    listofmail = arrayListOf()
                    listofmail.addAll(it)
                    listAdapter.adddata(listofmail)

                }
                // Update UI with the fetched data

            }
        }
    }

    private fun createCollection() {
        val samsungModels = arrayOf(
            "Drafts", "Sent",
            "Trash", "Spam", "Archive", "Important"
        )
        val samsungModelsicon = arrayOf(
            R.drawable.ic_folder, R.drawable.ic_send,
            R.drawable.ic_bin, R.drawable.ic_send, R.drawable.ic_archive, R.drawable.ic_clock
        )
        val googleModels = arrayOf(
            "Pixel 4 XL", "Pixel 3a", "Pixel 3 XL", "Pixel 3a XL",
            "Pixel 2", "Pixel 3"
        )
        val redmiModels = arrayOf("Redmi 9i", "Redmi Note 9 Pro Max", "Redmi Note 9 Pro")
        val vivoModels = arrayOf("Vivo V20", "Vivo S1 Pro", "Vivo Y91i", "Vivo Y12")
        val nokiaModels = arrayOf("Nokia 5.3", "Nokia 2.3", "Nokia 3.1 Plus")
        val motorolaModels = arrayOf("Motorola One Fusion+", "Motorola E7 Plus", "Motorola G9")
        mobileCollection = HashMap()
        for (group in groupList!!) {
            if (group == "nehavaidya96@gmail.com") {
                loadChild(samsungModels, samsungModelsicon)
            } else if (group == "Google") loadChild(
                googleModels,
                samsungModelsicon
            ) else if (group == "Redmi") loadChild(
                redmiModels,
                samsungModelsicon
            ) else if (group == "Vivo") loadChild(
                vivoModels,
                samsungModelsicon
            ) else if (group == "Nokia") loadChild(
                nokiaModels,
                samsungModelsicon
            ) else loadChild(motorolaModels, samsungModelsicon)
            (mobileCollection as HashMap<String, List<String>>).put(group, childList!!)
        }
    }

    private fun loadChild(mobileModels: Array<String>, samsungModelsicon: Array<Int>) {
        childList = ArrayList()
        childListicon = ArrayList()
        for (model in mobileModels) {
            childList.add(model)

        }
        for (model in samsungModelsicon) {
            childListicon.add(model)

        }
    }


    private fun createGroupList() {
        groupList = ArrayList()
        groupList.add("nehavaidya96@gmail.com")

    }

    lateinit var fm: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    private fun setspinner() {
        val spin = findViewById<LinearLayout>(R.id.drop_layout)
        val drop = findViewById<ImageView>(R.id.drop)
        val spintextview = findViewById<TextView>(R.id.spinner)
        spin.setOnClickListener {
            var dialog = Dialog(this, R.style.DialogStyle);
            dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_popup);

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_choose);
            var tabLayout = dialog.findViewById(R.id.tabLayout_choose) as TabLayout

            tabLayout.addTab(tabLayout.newTab())
            tabLayout.addTab(tabLayout.newTab())
            tabLayout.addTab(tabLayout.newTab())

            val layout =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            // tabOne.text = "Tab 1"
            var tabOne = layout.findViewById<ImageView>(R.id.image)
            tabOne.setImageResource(R.drawable.smart2_selected)
            var tabOnetext = layout.findViewById<TextView>(R.id.tab)
            tabOnetext.setText("Smart 2.0")
            tabLayout.getTabAt(0)!!.customView = layout

            val layout2 =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            var tabTwo = layout2.findViewById<ImageView>(R.id.image)
            tabTwo.setImageResource(R.drawable.smart)
            var tabTwotext = layout2.findViewById<TextView>(R.id.tab)
            tabTwotext.setText("Smart")

            tabLayout.getTabAt(1)!!.customView = layout2
            val layout3 =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            var tabThree = layout3.findViewById<ImageView>(R.id.image)
            // tabTwo.text = "Tab 2"
            tabThree.setImageResource(R.drawable.classic)
            tabLayout.getTabAt(2)!!.customView = layout3
            var tabThreetext = layout3.findViewById<TextView>(R.id.tab)
            tabThreetext.setText("Classic")


            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {

                        if (tab.position == 0) {


                            tabOne.setImageResource(R.drawable.smart2_selected)

                            tabTwo.setImageResource(R.drawable.smart)
                            tabThree.setImageResource(R.drawable.classic)

                        }
                        if (tab!!.position == 1) {
                            tabOne.setImageResource(R.drawable.smart_2)

                            // tabLayout.getTabAt(0)!!.customView=tabOne
                            tabTwo.setImageResource(R.drawable.smart_selection)
                            tabThree.setImageResource(R.drawable.classic)

                        }
                        if (tab!!.position == 2) {
                            tabOne.setImageResource(R.drawable.smart_2)

                            // tabLayout.getTabAt(0)!!.customView=tabOne
                            tabTwo.setImageResource(R.drawable.smart)
                            tabThree.setImageResource(R.drawable.classic_selection)

                        }
                    }
                }


                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            dialog.getWindow()!!
                .setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show()

            val window: Window? = dialog.getWindow()
            val params = window!!.attributes
            params.gravity = Gravity.TOP
            window.attributes = params
            //ChooseDialog().show(supportFragmentManager, "MyCustomFragment")
        }


    }


    fun showPermissionDialog() {
        applicationContext.startActivity(
            Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun enableSwipe(adapter: ListEmailAdapter, arlist: ArrayList<Email>) {
        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition

                    if (direction == ItemTouchHelper.LEFT) {
                        val deletedModel = arlist!![position]
                        adapter!!.deleteItem(position, this@MainActivity)
                        // showing snack bar with Undo option
                        val snackbar = Snackbar.make(
                            window.decorView.rootView,
                            " removed from Recyclerview!",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("UNDO") {
                            // undo is selected, restore the deleted item
                            adapter!!.showUndoSnackbar(this@MainActivity)
                        }
                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()
                    } else {
                        val deletedModel = arlist!![position]
                        adapter!!.deleteItem(position, this@MainActivity)
                        // showing snack bar with Undo option
                        val snackbar = Snackbar.make(
                            window.decorView.rootView,
                            " removed from Recyclerview!",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("UNDO") {
                            // undo is selected, restore the deleted item
                            adapter!!.showUndoSnackbar(this@MainActivity)
                        }
                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val icon: Bitmap
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.color = Color.parseColor("#6c63ff")
                            val background =
                                RectF(
                                    itemView.left.toFloat(),
                                    itemView.top.toFloat(),
                                    dX,
                                    itemView.bottom.toFloat()
                                )
                            c.drawRect(background, p)
                            icon =
                                BitmapFactory.decodeResource(resources, R.drawable.ic_swipe_circle)
                            val icon_dest = RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        } else {
                            p.color = Color.parseColor("#D32F2F")
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            icon =
                                BitmapFactory.decodeResource(resources, R.drawable.ic_swipe_delete)
                            val icon_dest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        }
                    }
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.list)
    }

    private fun setUpAdapterAndGetData() {

        //set up recycler view
        val gridLayoutManager = LinearLayoutManager(this@MainActivity)
        binding.list.layoutManager = gridLayoutManager
        Thread()
        {
            // binding.rcvNft.setHasFixedSize(true)
            var article: Email = Email()
            var source = Source()
            source.id = "1"
            source.name = "Hey this is to inform you about event!!"

            article.type = 0
            article.title = "Today "
            article.author = "Author1"
            article.description = "This is first email"
            var article2: Email = Email()
            source.id = "2"
            source.name = "Hey this is to inform you about event!!"

            article2.type = 1
            article2.title = "This is a sample news title which has no intent1"
            article2.author = "This is a sample news title which has no intent"
            article2.description = "This is second email"


            var article3: Email = Email()
            source.id = "2"
            source.name = "Hey this is to inform you about event!!"

            article3.type = 1
            article3.title = "This is a sample news title which has no intent2"
            article3.author = "This is a sample news title which has no intent"
            article3.description = "This is second email"
            var arlist: ArrayList<Email> = arrayListOf()
            arlist.add(article)
            arlist.add(article3)
            arlist.add(article2)
            var article4: Email = Email()
            source.id = "2"
            source.name = "Hey this is to inform you about event!!"

            article4.type = 0
            article4.title = "November 2022"
            article4.author = "This is a sample news title which has no intent"
            article4.description = "This is second email"
            arlist.add(article4)

            var article5: Email = Email()
            source.id = "2"
            source.name = "Hey this is to inform you about event!!"

            article5.type = 1
            article5.title = "This is a sample news title which has no intent3"
            article5.author = "This is a sample news title which has no intent"
            article5.description = "This is second email"
            arlist.add(article5)




            arlist.addAll(newsList1)


        }.start()


    }

    var first: Boolean = false
    fun visibleSelectall(visibility: Boolean) {
        if (visibility) {
            binding.select.visibility = View.VISIBLE
            binding.search.visibility = View.GONE
        } else {
            binding.select.visibility = View.INVISIBLE
            binding.search.visibility = View.VISIBLE
        }
        binding.select.setOnClickListener {
            if (!first) {
                binding.select.setText("Deselect all")
                first = !first
                listAdapter.getAllList(first)
            } else {
                binding.select.setText("select all")
                first = !first
                listAdapter.getAllList(first)

            }
        }

    }


    fun setupmenu() {
        val pm = PopupMenu(this@MainActivity, binding.more)
        pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu())
        pm.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.setting -> {
                        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
                        return true
                    }

                    R.id.second -> {
                        showBottomSheet(binding.root)

                        return true
                    }

                    R.id.third -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Clicked Third Menu Item",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return true
                    }
                }
                return true
            }
        })
        pm.show()
    }

    fun showBottomSheet(view: View?) {

        val addPhotoBottomDialogFragment =
            ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG

        )

    }

    override fun onItemClick(item: String) {
        // tvSelectedItem.setText("Selected action item is $item")
    }

    override fun onStop() {
        super.onStop()
        // model.insertbody()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()

    }


    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val temp = """
            ${intent.getStringExtra("notification_event")}
            ${">>"}
            """.trimIndent()
            Log.d("TAG", "onReceive: " + "" + temp)


        }
    }

}




