package com.aiemail.superemail.Activities



import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Dialog.CustomListViewDialog
import com.aiemail.superemail.Dialog.MyCommonDialog
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.BottomOptionBinding
import com.aiemail.superemail.databinding.LayoutComposeBinding
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PreferenceManager
import com.aiemail.superemail.viewmodel.AllEmailViewmodel
import com.aiemail.superemail.viewmodel.EmailViewmodel
import com.aiemail.superemail.viewmodel.PinViewmodel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.api.services.gmail.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class ComposeActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener,
    MyCustomDialog.DialogListner {


    lateinit var webView: WebView
    lateinit var back: ImageView
    lateinit var bottomNavigationView: BottomAppBar
    lateinit var prem: ImageView
    lateinit var contact: ImageView
    lateinit var pin: ImageView
    lateinit var binding: LayoutComposeBinding
    var once: Boolean = false
    var id: Int = 0
    var from_cat: Boolean = false
    var section: Email = Email()
    var currentObject: Pin = Pin()
    lateinit var preferenceManager: PreferenceManager
    private var listofmail: ArrayList<Email> = arrayListOf()
    private var position: Int = 0
    private var pinlist: ArrayList<Pin> = arrayListOf()
    lateinit var myPref:SharedPreferences
    lateinit  var username :String
    lateinit  var display_name :String


    val Allmodel: AllEmailViewmodel by viewModels() {
        AllEmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).allrepository
        )
    }
    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }
    val pinmodel: PinViewmodel by viewModels() {
        PinViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).pinrepository
        )
    }

    companion object {
        var isFromCompose: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        InitUi()
//        ObserveList()
        ObservePin()


    }

    private fun ObservePin() {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val pinList = pinmodel.FetchPinList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {

                pinList.observe(this@ComposeActivity)
                {
                    Log.d("TAG", "onclick: " + it)
//                        pinlist.add(it)
                    pinlist.addAll(it)
                    if (pinlist.contains(currentObject)) {
                        pin.setImageResource(R.drawable.ic_pin_set)

                    } else {
                        pin.setImageResource(R.drawable.ic_pin_notset)

                    }

                }
            }


        }
    }

//    private fun ObserveList() {
//        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
//            model.getEmailList().observe(this@ComposeActivity)
//            {
//                Log.d("TAG", "onclick: " + it)
//                listofmail = arrayListOf()
//                listofmail.addAll(it)
//
//            }
//        }
//
//
//    }


    private fun InitUi() {
        preferenceManager = PreferenceManager(this)
        preferenceManager.SetUpPreference()
        myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        username = myPref.getString("username", "")!!
        display_name = myPref.getString("account", "")!!;

        webView = binding.webview1
        back = findViewById(R.id.back)

        prem = findViewById(R.id.premium)
        contact = findViewById(R.id.contact)
        pin = findViewById(R.id.pin)
        id = intent.getIntExtra("id", 0)
        position = id
        from_cat = intent.getBooleanExtra("from_cat", false)
        if (from_cat) {
            Allmodel.AllInBox(this@ComposeActivity)
        }

        setupwebview()

        contact.setOnClickListener {
            MyCommonDialog("").show(supportFragmentManager, "MyCustomFragment")
        }
        prem.setOnClickListener {
            showBottomSheetpurchse()
        }

        back.setOnClickListener {
            super.onBackPressed()

            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out
            );
        }
        pin.setOnClickListener {
            Log.d("TAG", "InitUi: "+currentObject+"??"+pinlist.contains(currentObject))
            if (pinlist.contains(currentObject)) {
                pin.setImageResource(R.drawable.ic_pin_notset)
                currentObject.isPinned = false
                pinmodel.delete(currentObject)
            } else {
                pin.setImageResource(R.drawable.ic_pin_set)
                currentObject.isPinned = true
                pinmodel.insertpin(currentObject)
            }


        }

        binding.more.setOnClickListener {
            showBottomSheet()
            // Handle search icon press

        }
        binding.home.setOnClickListener {
            finish()
            if (preferenceManager.getBoolean("opennext")) {
                if (listofmail.size != 0 && position < listofmail.size && isFromCompose) {
                    Log.d(
                        "TAG",
                        "onclick: " + listofmail.size + ">>" + isFromCompose + ">>" + (listofmail.get(
                            position + 1
                        ))
                    )
                    model.MailObject.postValue(listofmail.get(position + 1))
                }
//
                isFromCompose = true
                startActivity(
                    Intent(
                        this,
                        ComposeActivity::class.java
                    )
                )

                overridePendingTransition(
                    R.anim.slide_in_up,
                    R.anim.slide_out_up
                );
            } else {
                isFromCompose = false
                finish()
                overridePendingTransition(
                    R.anim.anim_slide_stay,
                    R.anim.slide_out_up
                );
                // Handle more item (inside overflow menu) press
            }

        }
        binding.apply.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.anim_slide_stay,
                R.anim.slide_out_up
            );
        }
        binding.reply.setOnClickListener {
            model.MailObject.postValue(section)
            startActivity(
                Intent(this, DirectComposeActivity::class.java).putExtra(
                    "data",
                    "Replying"
                )
            )
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

        }
        binding.clock.setOnClickListener {
            MyCustomDialog(R.layout.layout_snooze_dialog, "", this, this, Message()).show(
                supportFragmentManager,
                "MyCustomFragment"
            )

        }
    }


    private fun setupwebview() {

        if (preferenceManager.getBoolean("dark_html") || preferenceManager.getString("Theme").equals("Dark theme") && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.settings, WebSettingsCompat.FORCE_DARK_ON)
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Open the URL in the default browser
                // Open the URL in the default browser

                Log.d("TAG", "shouldOverrideUrlLoading: " + preferenceManager.getBoolean("browser"))
                if (preferenceManager.getBoolean("browser")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    view.context.startActivity(intent)

                } else {

                    // If you wnat to open url inside then use
                    view.loadUrl(url);
                }
                return true
            }


        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().useWideViewPort = true
        webView.getSettings().loadWithOverviewMode = true
        webView.setInitialScale(1);


//From Simple MailList
        if (!from_cat) {
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread
                val userList = model.MailObject

                // Perform additional background fetch or processing
                // ...

                withContext(Dispatchers.Main) {
                    userList.observe(this@ComposeActivity)
                    {
                        section = it
                        binding.title.setText("" + it.author)
                        Log.d("TAG", "setupwebview: " + it.description)
                        if (it.description != null && !once) {
                            once = true
                            //  val rawEmailString = it.get(id).description

                            webView.loadDataWithBaseURL(
                                null,
                                it.description!!,
                                "text/html",
                                "UTF-8",
                                null
                            );


                        }
                        var pin: Pin = Pin()
                        pin.id = it.id
                        pin.msgid=it.msgid
                        pin.author = it.author
                        pin.description = it.description
                        pin.title = it.title
                        pin.content = it.content
                        pin.date = it.date
                        pin.url = it.url
                        pin.thread_id = it.thread_id


                        currentObject = pin
                        // Update UI with the fetched data

                    }


                }
            }
        }


        //From CategoriesList
        else {

            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread
                val userList = Allmodel.allMails


                // Perform additional background fetch or processing
                // ...

                withContext(Dispatchers.Main) {
                    userList.observe(this@ComposeActivity)
                    {
                        binding.title.setText("" + it.author)


                        if (!once) {
                            once = true
                            //  val rawEmailString = it.get(id).description

                            webView.loadDataWithBaseURL(
                                null,
                                it.description!!,
                                "text/html",
                                "UTF-8",
                                null
                            );

                            var pin: Pin = Pin()
                            pin.id = it.id
                            pin.author = it.author
                            pin.description = it.description
                            pin.title = it.title
                            pin.content = it.content
                            pin.date = it.date
                            pin.url = it.url
                            pin.thread_id = it.thread_id
                            currentObject = pin


                        }
                    }
                }
            }

        }


    }


    fun showBottomSheet() {
        //Inflate Dialog
        var bottomSheetDialogbinding = BottomOptionBinding.inflate(layoutInflater)
// Find the root layout of your custom layout
        // Find the root layout of your custom layout
       // bottomSheetDialogbinding.rootLayout.setBackgroundColor(Color.TRANSPARENT)


// Initialize dialog
        var dialog = BottomSheetDialog(this ,R.style.BottomSheetDialogTheme);
        // set background transparent
        val window =  dialog.window
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent)
            val lp = window.attributes
            lp.alpha = 1.0f
            lp.dimAmount = 0.0f
            window.attributes = lp
        }


//        dialog.getWindow()!!.setBackgroundDrawable(
//            ColorDrawable(
//                Color.TRANSPARENT
//            )
//        );
        // set view
        dialog.setContentView(bottomSheetDialogbinding.getRoot())
        if (!isFinishing && dialog != null) {
            runOnUiThread {
                dialog.show()
                BottomSheetClick(bottomSheetDialogbinding, dialog)
            }
        }


    }

    private fun BottomSheetClick(
        bottomSheetDialog: BottomOptionBinding,
        dialog: BottomSheetDialog
    ) {

        Log.d("TAG", "BottomSheetClick: ")

        bottomSheetDialog.forward.setOnClickListener {
            dialog.dismiss()
            model.MailObject.postValue(section)
            startActivity(
                Intent(this, DirectComposeActivity::class.java).putExtra(
                    "data",
                    "forward"
                )
            )
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

        }

        bottomSheetDialog.move.setOnClickListener {

            SetupMove()

        }
        bottomSheetDialog.delete.setOnClickListener {
            dialog.dismiss()
            Thread {
                Helpers.DeleteMail(this, currentObject.msgid)
            }.start()
        }
        bottomSheetDialog.mute.setOnClickListener {
            dialog.dismiss()
            Thread {
                Helpers.MuteMail(this, currentObject.thread_id)
            }.start()
        }
        bottomSheetDialog.moveSpam.setOnClickListener {
            Helpers.MoveFolder(this, currentObject.msgid, "SPAM")
        }
        bottomSheetDialog.print.setOnClickListener {
            dialog.dismiss()
            Helpers.print(this, webView)

        }
        bottomSheetDialog.pdfConvert.setOnClickListener {
            dialog.dismiss()
            currentObject.description?.let { it1 -> Helpers.SaveAsPdf(webView, this) }
        }
        bottomSheetDialog.sendAgain.setOnClickListener {
            dialog.dismiss()
            model.MailObject.postValue(section)
            startActivity(
                Intent(this, DirectComposeActivity::class.java).putExtra(
                    "data",
                    "send"
                )
            )
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

        }
        bottomSheetDialog.block.setOnClickListener {
            dialog.dismiss()
            Helpers.BlockSender(this, currentObject.msgid)
        }
        bottomSheetDialog.aside.setOnClickListener {
            dialog.dismiss()
            model.adddataAside(section)
        }
        bottomSheetDialog.editToolbar.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(this, EditToolbarActivity::class.java)
            )
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        bottomSheetDialog.detail.setOnClickListener {
            dialog.dismiss()
            var bottomSheetDialog =
                BottomSheetDialog(this, R.style.BottomSheetDialogcustom);
            bottomSheetDialog.setContentView(R.layout.dialog_details);
            bottomSheetDialog.findViewById<TextView>(R.id.date)!!.setText(section.date)
            bottomSheetDialog.findViewById<TextView>(R.id.account)!!.setText(section.sender)
            bottomSheetDialog.findViewById<TextView>(R.id.from_email)!!.setText(section.senderEmail)
            bottomSheetDialog.findViewById<TextView>(R.id.TO)!!.setText(display_name)
            bottomSheetDialog.findViewById<TextView>(R.id.to_email)!!.setText(username)
            bottomSheetDialog.findViewById<TextView>(R.id.subject)!!.setText(section.author)
            bottomSheetDialog.show()
        }
        bottomSheetDialog.tool.setOnClickListener {

        }

    }


    private fun SetupMove() {
        var pin: Email = Email()
        pin.id = currentObject.id
        pin.author = currentObject.author
        pin.description = currentObject.description
        pin.title = currentObject.title
        pin.content = currentObject.content
        pin.date = currentObject.date
        pin.url = currentObject.url
        pin.msgid = currentObject.msgid
        Log.d("TAG", "SetupMove: "+currentObject.msgid)
        var customDialog = CustomListViewDialog(this, pin)

        //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
        customDialog!!.show()
        customDialog!!.setCanceledOnTouchOutside(false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
            this.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun showBottomSheetpurchse() {

        val addPhotoBottomDialogFragment =
            ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG

        )

    }

    override fun onItemClick(item: String?) {

    }

    private fun convertRawToPlainText(rawEmailBody: String): String? {
        try {
            val properties = Properties()
            val session = Session.getDefaultInstance(properties, null)
            val mimeMessage = MimeMessage(session, ByteArrayInputStream(rawEmailBody.toByteArray()))
            val content = mimeMessage.content
            if (content is String) {
                return content
            } else if (content is MimeMultipart) {
                return getTextFromMultipart(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(MessagingException::class, IOException::class)
    private fun getTextFromMultipart(multipart: MimeMultipart): String? {
        val plainText = StringBuilder()
        val count = multipart.count
        for (i in 0 until count) {
            val bodyPart = multipart.getBodyPart(i)
            if (bodyPart.isMimeType("text/plain")) {
                plainText.append(bodyPart.content)
            } else if (bodyPart.isMimeType("multipart/*")) {
                val nestedMultipart = bodyPart.content as MimeMultipart
                plainText.append(getTextFromMultipart(nestedMultipart))
            }
        }
        return plainText.toString()
    }

    override fun dialogClick(sender_id: String, dialog: MyCustomDialog) {
        dialog.dismiss()
    }


}