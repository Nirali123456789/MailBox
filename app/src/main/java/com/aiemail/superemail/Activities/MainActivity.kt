/*
 * Created by Nirali Pandya on 26/04/2023, 8:00 AM
 *     Copyright (c) 2022.
 *     All rights reserved.
 * 3 different views with observer and roomdatabase
 */

package com.aiemail.superemail.Activities


import android.accounts.Account
import android.app.ActivityOptions
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
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
import com.aiemail.superemail.Adapters.PinAdapter
import com.aiemail.superemail.Adapters.ScrollAdapter
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Fragments.AsideFragment
import com.aiemail.superemail.Fragments.FragmentChanger
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Models.Source
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.Repository.DraftRepository
import com.aiemail.superemail.Repository.SentRepository
import com.aiemail.superemail.Repository.SpamRepository
import com.aiemail.superemail.Repository.TrashRepository
import com.aiemail.superemail.Service.BadgeIntentService
import com.aiemail.superemail.Viewholders.MailSection
import com.aiemail.superemail.databinding.ActivityMain1Binding
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PeopleHelper
import com.aiemail.superemail.utilis.PeopleHelper.setUp
import com.aiemail.superemail.utilis.PreferenceManager
import com.aiemail.superemail.viewmodel.AllEmailViewmodel
import com.aiemail.superemail.viewmodel.EmailViewmodel
import com.aiemail.superemail.viewmodel.PinViewmodel
import com.ashraf007.expandableselectionview.adapter.BasicStringAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.Person
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase
import com.ibrajix.nftapp.utilis.Utility.setTransparentStatusBar
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.leolin.shortcutbadger.ShortcutBadger


//@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener,
    MailSection.ClickListener, ListEmailAdapter.ClickListenerData, PinAdapter.ClickListenerPin,ScrollAdapter.ClickListenerData {

    private var position: Int = 0
    lateinit var drawer: DrawerLayout
    private lateinit var binding: ActivityMain1Binding
    private var listofmail: ArrayList<Email> = arrayListOf()
    private var listofunread: ArrayList<Email> = arrayListOf()
    private var asideList: ArrayList<Email> = arrayListOf()
    private var pinlist: ArrayList<Pin> = arrayListOf()
    private lateinit var listAdapter: ListEmailAdapter
    private  lateinit var scrollAdapter: ScrollAdapter
    private lateinit var pinAdapter: PinAdapter
    lateinit var groupList: ArrayList<String>
    var childList: ArrayList<String> = arrayListOf()
    lateinit var childListicon: ArrayList<Int>
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var p = Paint()
    val sectionAdapter = SectionedRecyclerViewAdapter()
    var inbox: Boolean = true
    lateinit var preferenceManager: PreferenceManager

    companion object {
        var adapter_set: Boolean = false
    }


    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }
    val Allmodel: AllEmailViewmodel by viewModels() {
        AllEmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).allrepository
        )
    }

    val pinmodel: PinViewmodel by viewModels() {
        PinViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).pinrepository
        )
    }
    var sentRepository: SentRepository? = null
    var spamRepository: SpamRepository? = null
    var draftRepository: DraftRepository? = null
    var trashRepository: TrashRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Helpers.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        binding = ActivityMain1Binding.inflate(layoutInflater)
        SetUp()
        DynamicLink()


    }

    private fun SetUp() {
        spamRepository = (application as MyApplication).spamRepository
        sentRepository = (application as MyApplication).sentRepository
        draftRepository = (application as MyApplication).draftRepository
        trashRepository = (application as MyApplication).trashRepository

        preferenceManager = PreferenceManager(this)
        preferenceManager.SetUpPreference()

        drawer = binding.drawer
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent,200)
            }
        }else{
            if (!isNotificationServiceEnabled()) {
                showPermissionDialog()
            }
        }
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(
            resources.getDrawable(
                R.drawable.ic_menu,
                null
            )
        )
        setSupportActionBar(toolbar);


        createGroupList();
        onclick()
        ExpandableUi()
        fetchData()
        Register()
        UISetup()
        fetchasideListData()
        fetchDataSent()
        fetchDataSpam()
        fetchDataDraft()
        fetchTrash()

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

        childList.add(resources.getString(R.string.draft))
        childList.add(resources.getString(R.string.sent))
        childList.add(resources.getString(R.string.trash))
        childList.add(resources.getString(R.string.spam))
        childList.add(resources.getString(R.string.archive))
        childList.add(resources.getString(R.string.important))

        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");
        var expandableListAdapter = BasicStringAdapter(childList, icons, username)
        binding.included.expandableListViewdrawer
            .setAdapter(expandableListAdapter)

        binding.included.expandableListViewdrawer.selectionListener = { index: Int? ->
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
            layoutManager = LinearLayoutManager(this@MainActivity)
            listAdapter =
                ListEmailAdapter(this@MainActivity, listofmail, this@MainActivity) { select ->
                    visibleSelectall(select)
                }
            adapter = listAdapter
        }


    }

    var arlist: HashMap<Int, List<AllMails>> = hashMapOf()
    fun SetUpAdapter() {


// Add your Sections

// Add your Sections
        //sectionAdapter.addSection(MailSection(arrayListOf(), "", this, this))

// Set up your RecyclerView with the SectionedRecyclerViewAdapter

// Set up your RecyclerView with the SectionedRecyclerViewAdapter

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = sectionAdapter

//        binding.list.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//
//            smartAdapter = MainAdapter(smartlist)
//            adapter = smartAdapter
//        }
    }

    private fun TypesData(index: Int) {
        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");
        var acc = GoogleSignIn.getLastSignedInAccount(this)
//        model.DeleteMails()
//        model.insertMail()
        if (index == 1) {

            sentRepository!!.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                index
            )
        }
        if (index == 6) {
            spamRepository!!.deletedata()
            spamRepository!!.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                index
            )
        }
        if (index == 5) {
            draftRepository!!.deletedata()
            draftRepository!!.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                index
            )
        }
        if (index == 3) {
            (application as MyApplication).importantRepository.deletedata()
            (application as MyApplication).importantRepository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                index
            )
        }
        if (index == 4) {
            trashRepository!!.deletedata()
            (application as MyApplication).trashRepository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                index
            )
        }

    }

    private fun UISetup() {
        enableSwipe(listAdapter, listofmail)
        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpAdapterAndGetData()
        val popup = PopupMenu(this, binding.more)
        // popup.setOnMenuItemClickListener(this@MainActivity1)
        popup.inflate(R.menu.popup_menu)
        setspinner()
        val apiKey = getString(R.string.Api_Key)
        val emailList = "swainfo.nirali@gmail.com" // Add your email addresses here


    }




    fun SetUpPinAdapter() {
        binding.list.apply {

            pinAdapter = PinAdapter(this@MainActivity, pinlist, this@MainActivity) { select ->
                visibleSelectall(select)
            }
            adapter = pinAdapter
        }
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
            SetMainAdapter()
            binding.included.inbox.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
            var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(this)
//            model.DeleteMails()
//            model.insertMail()
            coroutineScope.launch(Dispatchers.Main) {

                model.getEmailList().observe(this@MainActivity)
                {

                    listofmail = arrayListOf()
                    listofmail.addAll(it)
                    listAdapter.adddata(listofmail)
                    binding.included.count.setText(" " + listofmail.size)
                }
            }

        }
        binding.included.sent.setOnClickListener {
            binding.container.visibility = View.GONE
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

            (application as MyApplication).sentRepository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                1
            )
        }
        binding.included.draft.setOnClickListener {
            binding.container.visibility = View.GONE
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

            (application as MyApplication).draftRepository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                5
            )
        }
        binding.included.pins.setOnClickListener {
            binding.container.visibility = View.GONE
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Pins")
            updateUi()
            SetUpPinAdapter()
            binding.included.pins.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread
                val pin = pinmodel.FetchPinList()

                // Perform additional background fetch or processing
                // ...

                withContext(Dispatchers.Main) {

                    pin.observe(this@MainActivity)
                    {
                        Log.d("TAG", "onclick: " + it)
//                        pinlist.add(it)
                        pinlist.addAll(it)
                        pinAdapter.adddata(pinlist)
                    }
                }


            }
        }
        binding.included.archive.setOnClickListener {
            binding.container.visibility = View.GONE
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Archived")
            updateUi()
            binding.included.archive.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)

            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread
                val pin = pinmodel.getArchive()

                // Perform additional background fetch or processing
                // ...

                withContext(Dispatchers.Main) {

                    pin.observe(this@MainActivity)
                    {
                        Log.d("TAG", "onclick: " + it)
//                        pinlist.add(it)
                        pinlist.addAll(it)
                        pinAdapter.adddata(pinlist)
                    }
                }


            }
        }
        binding.included.trash.setOnClickListener {
            binding.container.visibility = View.GONE
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
            trashRepository!!.deletedata()

            (application as MyApplication).trashRepository.fetchMails(
                this,
                username!!,
                username!!,
                acc!!,
                4
            )
        }
        binding.included.snoozelayout.setOnClickListener {
            binding.container.visibility = View.GONE
            binding.trashBin.visibility = View.GONE
            binding.spinner.visibility = View.GONE
            binding.Title.visibility = View.VISIBLE
            binding.drop.visibility = View.GONE
            binding.Title.setText("Snoozed")
            updateUi()
            binding.included.snoozelayout.background =
                ContextCompat.getDrawable(this, R.drawable.bg_drawer_select)
            drawer.closeDrawer(GravityCompat.START)
        }
        binding.aside.setOnClickListener {
            binding.container.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up, R.anim.slide_out_up,
                    R.anim.slide_out_up, R.anim.slide_in_up
                )
                .replace(R.id.container, AsideFragment())
                .commit()
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


//        binding.container.visibility = View.VISIBLE
//        supportFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                R.anim.slide_in_up, R.anim.slide_out_up,
//                R.anim.slide_out_up, R.anim.slide_in_up
//            )
//            .replace(R.id.container, FragmentChanger())
//            .commit()
        fetchunread()
        setupData()
        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = model.getEmailList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {


                    listofmail = arrayListOf()
                    listofmail.addAll(it)
                    listAdapter.adddata(listofmail)

                    if (inbox) {
                        inbox = false

                        if (preferenceManager.getBoolean("Allmessage") && listofmail.size != 0) {
                            //  getBadgeCount(this@MainActivity, listofmail.size)


                        }
                        binding.included.count.setText(" " + listofmail.size)
                        ShortcutBadger.applyCount(this@MainActivity, listofmail.size)
                    }


                }
                // Update UI with the fetched data

            }
        }

        //setBadgeCount(this@MainActivity,5)


        startService(
            Intent(this@MainActivity, BadgeIntentService::class.java).putExtra(
                "badgeCount",
                listofmail.size
            )
        )

//        ShortcutBadger.with(applicationContext).count(badgeCount) //for 1.1.3


    }
    private fun setupData() {

//        *Set EmailList Adapter To Recyclerview

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            scrollAdapter = ScrollAdapter(this@MainActivity!!, listofunread, this@MainActivity) { select ->
//                visibleSelectall(select)
            }
            adapter = scrollAdapter
        }
    }

    private fun fetchunread() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = model.getUnreadEmailList()


            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {

                    userList.observe(this@MainActivity)
                    {
                        Log.d("TAG", "fetchunread: "+it.size)
//                    if (it.size < 6) {

                        listofunread = arrayListOf()
                        listofunread.addAll(it)
                        scrollAdapter.adddata(listofunread)
                        //}


                    }
                    // Update UI with the fetched data


            }
            // Access the LifecycleOwner of the View as needed
        }

    }

    fun getBadgeCount(context: Context, count: Int) {
        ShortcutBadger.applyCount(context, count)
    }

    var smartlist: java.util.HashMap<String, List<AllMails>> = hashMapOf()
    var array: ArrayList<AllMails> = arrayListOf()
    var value: ArrayList<AllMails> = arrayListOf()
    private fun fetchAllData() {
        val userList = Allmodel.maildata
        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        drawer.closeDrawer(GravityCompat.START)
        var username = myPref.getString("username", "");
        var acc = GoogleSignIn.getLastSignedInAccount(this)
        SetUpAdapter()

        if (!adapter_set) {
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread


                withContext(Dispatchers.Main) {
                    userList.observe(this@MainActivity)
                    {
                        // smartlist = hashMapOf()
                        // smartlist.putAll(it)

                        for (key in it.keys) {

                            value = arrayListOf()
                            value = it.get(key) as ArrayList<AllMails>


//                        if(value.size<=15) {


                            if (!value.isEmpty() && !adapter_set) {
                                //Log.d("TAG", "ITERSTAOT: "+key+">>"+value)
                                var mailsection = MailSection(
                                    value,
                                    key,
                                    this@MainActivity,
                                    this@MainActivity, {
                                        visibleSelectall(it)

                                    }

                                )
                                sectionAdapter.addSection(mailsection)
                                binding.list.adapter = sectionAdapter
                            }
                        }
                        // }
                        //SetUpAdapter()
                        //  smartAdapter.adddata(smartlist)


                    }
                    // Update UI with the fetched data

                    // }
                }
            }
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

    private fun DynamicLink() {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://superemail.page.link/mailbox"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri
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

            var myPref =
                getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);

            var tabLayout = dialog.findViewById(R.id.tabLayout_choose) as TabLayout
            val layout =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val layout2 =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val layout3 =
                LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
//            var tabOne = layout.findViewById<ImageView>(R.id.image)
//            var tabTwo = layout2.findViewById<ImageView>(R.id.image)
//            var tabThree = layout3.findViewById<ImageView>(R.id.image)
//            Log.d("setspinner", "setspinner: " + myPref.getInt("position", 0))
//            when (myPref.getInt("position", 0)) {
//                0 -> tabOne.setImageResource(R.drawable.smart2_selected)
//                1 -> tabTwo.setImageResource(R.drawable.smart_selection)
//                2 -> tabThree.setImageResource(R.drawable.classic_selection)
//            }


            dialog.getWindow()!!
                .setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
            dialog.show()

            val window: Window? = dialog.getWindow()
            val params = window!!.attributes
            params.gravity = Gravity.TOP
            window.attributes = params
            //ChooseDialog().show(supportFragmentMa nager, "MyCustomFragment")


            // Add tabs with custom views
            addTab(getString(R.string.smart2), R.drawable.smart_2, tabLayout)
            addTab(getString(R.string.Smart), R.drawable.smart, tabLayout)
            addTab(getString(R.string.Classic), R.drawable.classic, tabLayout)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    dialog.dismiss()


                    // Handle tab selection
                    val tabView = tab.customView
                    val tabIcon = tabView?.findViewById<ImageView>(R.id.image)
                    val tabText = tabView?.findViewById<TextView>(R.id.tab)
                    if (tab.position == 0) {

                        binding.container.visibility = View.VISIBLE

                        binding.accountLayout.visibility = View.GONE
                        tabIcon!!.setImageResource(R.drawable.smart2_selected)
                        tabText?.setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.colorPurple)
                        )


//                        var username = myPref.getString("username", "");
//                        var acc = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
//
//                        model.DeleteMails()
//                        model.insertMail()
//                        (application as MyApplication).repository.fetchMails(
//                            this@MainActivity,
//                            username!!,
//                            username!!,
//                            acc!!,
//                            2
//                        )

                        fetchData()
                    }
                    if (tab!!.position == 1) {
                        binding.container.visibility = View.GONE
                        binding.accountLayout.visibility = View.GONE
                        tabIcon!!.setImageResource(R.drawable.smart_selection)
                        tabText?.setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.colorPurple)
                        )
                        // SetUpAdapter(Allmaillist)

                        fetchAllData()
                        Allmodel.AllInBox(this@MainActivity)


                    }
                    if (tab!!.position == 2) {
                        binding.container.visibility = View.GONE
                        binding.accountLayout.visibility = View.VISIBLE
                        tabIcon!!.setImageResource(R.drawable.classic_selection)
                        tabText?.setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.colorPurple)
                        )

                        SetMainAdapter()
                        model.AllInBox(this@MainActivity)


                    }

                    val editor: SharedPreferences.Editor =
                        applicationContext.getSharedPreferences(
                            "APP_SHARED_PREF",
                            Context.MODE_PRIVATE
                        )
                            .edit()
                    editor.putInt("position", tab.position)
                    editor.commit()
//

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // Handle tab unselection
                    val tabView = tab.customView
                    val tabIcon = tabView?.findViewById<ImageView>(R.id.image)
                    val tabText = tabView?.findViewById<TextView>(R.id.tab)
                    if (tab.position == 0) {
                        // Reset the tab's appearance when unselected
                        tabIcon!!.setImageResource(R.drawable.smart_2)
                    }
                    if (tab.position == 1) {
                        // Reset the tab's appearance when unselected
                        tabIcon!!.setImageResource(R.drawable.smart)
                    }
                    if (tab.position == 2) {
                        // Reset the tab's appearance when unselected
                        tabIcon!!.setImageResource(R.drawable.classic)
                    }

                    tabText?.setTextColor(
                        ContextCompat.getColor(applicationContext, R.color.darkgrey)
                    )
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // Handle tab selection
                    dialog.dismiss()
                    val tabView = tab.customView
                    val tabIcon = tabView?.findViewById<ImageView>(R.id.image)
                    val tabText = tabView?.findViewById<TextView>(R.id.tab)
                    if (tab != null) {
                        if (tab.position == 0) {

                            binding.container.visibility = View.VISIBLE

                            binding.accountLayout.visibility = View.GONE
                            tabIcon!!.setImageResource(R.drawable.smart2_selected)
                            tabText?.setTextColor(
                                ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorPurple
                                )
                            )


                            var username = myPref.getString("username", "");
                            var acc =
                                GoogleSignIn.getLastSignedInAccount(this@MainActivity)

                            model.DeleteMails()
                            model.insertMail()
                            (application as MyApplication).repository.fetchMails(
                                this@MainActivity,
                                username!!,
                                username!!,
                                acc!!,
                                2
                            )
                        }
                    }
                    if (tab!!.position == 1) {
                        binding.container.visibility = View.GONE
                        binding.accountLayout.visibility = View.GONE
                        tabIcon!!.setImageResource(R.drawable.smart_selection)
                        tabText?.setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.colorPurple)
                        )
                        // SetUpAdapter(Allmaillist)

                        fetchAllData()
                        Allmodel.AllInBox(this@MainActivity)


                    }
                    if (tab!!.position == 2) {
                        binding.container.visibility = View.GONE
                        binding.accountLayout.visibility = View.VISIBLE
                        tabIcon!!.setImageResource(R.drawable.classic_selection)
                        tabText?.setTextColor(
                            ContextCompat.getColor(applicationContext, R.color.colorPurple)
                        )

                        SetMainAdapter()
                        model.AllInBox(this@MainActivity)


                    }
                }
            })
        }


    }

    private fun addTab(title: String, iconResId: Int, tabLayout: TabLayout) {
        val tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)

        val tabIcon = tabView.findViewById<ImageView>(R.id.image)
        val tabText = tabView.findViewById<TextView>(R.id.tab)

        tabIcon.setImageResource(iconResId)
        tabText.text = title



        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView))
    }

    private fun SetMainAdapter() {
        binding.list.apply {

            listAdapter =
                ListEmailAdapter(this@MainActivity, listofmail, this@MainActivity) { select ->
                    visibleSelectall(select)
                }
            adapter = listAdapter
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

//                    if (direction == ItemTouchHelper.LEFT) {
//                        val deletedModel = arlist!![position]
//                        adapter!!.deleteItem(position, this@MainActivity)
//                        // showing snack bar with Undo option
//                        val snackbar = Snackbar.make(
//                            window.decorView.rootView,
//                            " removed from Recyclerview!",
//                            Snackbar.LENGTH_LONG
//                        )
//                        snackbar.setAction("UNDO") {
//                            // undo is selected, restore the deleted item
//                            adapter!!.showUndoSnackbar(this@MainActivity)
//                        }
//                        snackbar.setActionTextColor(Color.YELLOW)
//                        snackbar.show()
//                    } else {
//                        val deletedModel = arlist!![position]
//                        adapter!!.deleteItem(position, this@MainActivity)
//                        // showing snack bar with Undo option
//                        val snackbar = Snackbar.make(
//                            window.decorView.rootView,
//                            " removed from Recyclerview!",
//                            Snackbar.LENGTH_LONG
//                        )
//                        snackbar.setAction("UNDO") {
//                            // undo is selected, restore the deleted item
//                            adapter!!.showUndoSnackbar(this@MainActivity)
//                        }
//                        snackbar.setActionTextColor(Color.YELLOW)
//                        snackbar.show()
//                    }


                    try {
                        val position = viewHolder.adapterPosition
                        val item: Email = listAdapter.removeItem(position)!!
                        val snackbar = Snackbar.make(
                            viewHolder.itemView,
                            "Item " + (if (R.string.direction == ItemTouchHelper.RIGHT) "deleted" else "archived") + ".",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction(android.R.string.cancel) {
                            try {
                                listAdapter.addItem(item, position)
                            } catch (e: Exception) {
                                Log.e("MainActivity", e.message!!)
                            }
                        }
                        snackbar.show()
                    } catch (e: Exception) {
                        Log.e("MainActivity", e.message!!)
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
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.colorPurple
                            )
                        )
                        .addSwipeLeftActionIcon(R.drawable.ic_swipe_archive)
                        .addSwipeRightBackgroundColor(Color.RED)
                        .addSwipeRightActionIcon(R.drawable.ic_trash)
                        .addSwipeRightLabel(getString(R.string.action_delete))
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel(getString(R.string.archive))
                        .setSwipeLeftLabelColor(Color.WHITE)
                        //.addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
                        //.addPadding(TypedValue.COMPLEX_UNIT_DIP, 8, 16, 8)
                        .create()
                        .decorate()

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
            binding.bottom.visibility = View.VISIBLE
            binding.select.visibility = View.VISIBLE
            binding.search.visibility = View.GONE
            binding.compose.visibility = View.GONE
        } else {
            binding.compose.visibility = View.VISIBLE
            binding.bottom.visibility = View.GONE
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
        binding.archive.setOnClickListener {

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
        model.cancelJobs()

    }

    override fun onResume() {
        super.onResume()
//        if (listofmail.size!=0 && position<listofmail.size && ComposeActivity.isFromCompose)
//        model.MailObject.postValue(listofmail.get(position+1))

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

    override fun onItemRootViewClicked(section: AllMails, itemAdapterPosition: Int) {
        // what ever object you need to share between Activities and Fragments, it could be a data class or any object
        Allmodel.allMails.postValue(section)
        adapter_set = true
        startActivity(
            Intent(
                this,
                ComposeActivity::class.java
            ).putExtra("id", itemAdapterPosition).putExtra("from_cat", true)
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )

    }

    override fun onFooterRootViewClicked(section: MailSection, itemAdapterPosition: String) {
        model.SectionObject.postValue(section)
        Log.d("TAG", "onFooterRoot: " + itemAdapterPosition)
        startActivity(
            Intent(
                this,
                FullMailShowActivity::class.java
            ).putExtra("id", itemAdapterPosition)
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }

    override fun onItemRootViewClicked(section: Email, itemAdapterPosition: Int) {
        position = itemAdapterPosition
        model.MailObject.postValue(section)
        startActivity(
            Intent(
                this,
                ComposeActivity::class.java
            ).putExtra("id", itemAdapterPosition)
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )

    }

    override fun onItemRootViewClicked(section: Pin, itemAdapterPosition: Int) {
        // section.isArchived = true
        // pinmodel.insertpin(section)
        startActivity(
            Intent(
                this,
                ComposeActivity::class.java
            ).putExtra("id", itemAdapterPosition)
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }


    private fun fetchasideListData() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = model.FetchAsideList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {

                    Log.d("TAG", "fetchAndProcess: " + it)
                    asideList = arrayListOf()
                    asideList.addAll(it)

                    if (!asideList.isEmpty()) {
                        binding.aside.visibility = View.VISIBLE
                        binding.asideTotal.visibility = View.VISIBLE
                        binding.asideTotal.setText("" + asideList.size)
                    } else {
                        binding.aside.visibility = View.GONE
                        binding.asideTotal.visibility = View.GONE
                    }


                }
                // Update UI with the fetched data

            }
        }
    }


    fun setBadgeCount(context: Context, count: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "badge_channel"
            val channelName = "Badge Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.setShowBadge(true)
            channel.lockscreenVisibility = NotificationManager.IMPORTANCE_DEFAULT
            notificationManager.createNotificationChannel(channel)

            val badgeNotification = Notification.Builder(context, channelId)
                .setContentTitle("New Messages")
                .setContentText("You've received 3 new messages.")
                .setSmallIcon(R.drawable.ic_badge) // Replace with your app's icon
                .setNumber(count)
                .build()

            notificationManager.notify(0, badgeNotification)
        }
    }

    private fun fetchDataSpam() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = spamRepository!!.getSpamList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {

                    listofmail = arrayListOf()
                    for (emailField in it) {
                        var email: Email = Email()
                        email.sender = emailField.sender
                        email.senderEmail = emailField.senderEmail
                        email.body = emailField.body
                        email.content = emailField.content
                        email.description = emailField.description
                        email.author = emailField.author
                        email.sender = emailField.sender
                        listofmail.add(email)
                        listAdapter.adddata(listofmail)
                    }


                }
                // Update UI with the fetched data

            }
        }
    }

    private fun fetchDataSent() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = sentRepository!!.getSentList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {
                    listofmail = arrayListOf()

                    Log.d("TAG", "fetchAndProcess>>>: " + it)
                    for (emailField in it) {
                        var email: Email = Email()
                        email.sender = emailField.sender
                        email.senderEmail = emailField.senderEmail
                        email.body = emailField.body
                        email.content = emailField.content
                        email.description = emailField.description
                        email.author = emailField.author
                        email.sender = emailField.sender
                        listofmail.add(email)
                        listAdapter.adddata(listofmail)
                    }


                }
                // Update UI with the fetched data

            }
        }
    }

    private fun fetchDataDraft() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = draftRepository!!.getDraftList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {
                    listofmail = arrayListOf()

                    Log.d("TAG", "fetchAndProcess>>>: " + it)
                    for (emailField in it) {
                        var email: Email = Email()
                        email.sender = emailField.sender
                        email.senderEmail = emailField.senderEmail
                        email.body = emailField.body
                        email.content = emailField.content
                        email.description = emailField.description
                        email.author = emailField.author
                        email.sender = emailField.sender
                        listofmail.add(email)
                        listAdapter.adddata(listofmail)
                    }


                }
                // Update UI with the fetched data

            }
        }
    }

    private fun fetchTrash() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = trashRepository!!.getSentList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(this@MainActivity)
                {
                    listofmail = arrayListOf()

                    Log.d("TAG", "fetchAndProcess>>>: " + it)
                    for (emailField in it) {
                        var email: Email = Email()
                        email.sender = emailField.sender
                        email.senderEmail = emailField.senderEmail
                        email.body = emailField.body
                        email.content = emailField.content
                        email.description = emailField.description
                        email.author = emailField.author
                        email.sender = emailField.sender
                        listofmail.add(email)
                        listAdapter.adddata(listofmail)
                    }


                }
                // Update UI with the fetched data

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==200)
        {
            if (!isNotificationServiceEnabled()) {
                showPermissionDialog()
            }
        }
    }



}




