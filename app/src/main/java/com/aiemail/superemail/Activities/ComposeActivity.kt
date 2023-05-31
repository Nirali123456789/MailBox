package com.aiemail.superemail.Activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Dialog.MyCommonDialog
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.LayoutComposeBinding
import com.aiemail.superemail.feature.viewmodel.CategoryViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class ComposeActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener {

    lateinit var wv1: WebView
    lateinit var wv2: WebView
    lateinit var back: ImageView
    lateinit var bottomNavigationView: BottomAppBar
    lateinit var prem: ImageView
    lateinit var contact: ImageView
    lateinit var pin: ImageView
    lateinit var binding: LayoutComposeBinding
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wv1 = findViewById(R.id.webview)
        wv2=binding.webview1
        back = findViewById(R.id.back)
        bottomNavigationView = findViewById<BottomAppBar>(R.id.bottombar)
        prem = findViewById(R.id.premium)
        contact = findViewById(R.id.contact)
        pin = findViewById(R.id.pin)
        id = intent.getIntExtra("id", 0)

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
            var bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogcustom);
            bottomSheetDialog.setContentView(R.layout.layout_attachment);
            //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
            bottomSheetDialog.show()
        }

        binding.more.setOnClickListener {
            showBottomSheet()
            // Handle search icon press

        }
        binding.home.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.anim_slide_stay,
                R.anim.slide_out_up
            );
            // Handle more item (inside overflow menu) press

        }
        binding.apply.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.anim_slide_stay,
                R.anim.slide_out_up
            );
        }
        binding.reply.setOnClickListener {
            startActivity(
                Intent(this, DirectComposeActivity::class.java).putExtra(
                    "data",
                    "Replying"
                )
            )
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

        }
        binding.clock.setOnClickListener {
            MyCustomDialog(R.layout.layout_snooze_dialog).show(
                supportFragmentManager,
                "MyCustomFragment"
            )

        }


    }

    var once: Boolean = false

    private fun setupwebview() {


        wv1.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                // If you wnat to open url inside then use
                view.loadUrl(url);

                // if you wanna open outside of app
                /*if (url.contains(URL)) {
                        view.loadUrl(url)
                        return false
                    }else {
                        // Otherwise, give the default behavior (open in browser)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }*/


                return true
            }


        }
        wv1.getSettings().setJavaScriptEnabled(true);


        wv2.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                // If you wnat to open url inside then use
                view.loadUrl(url);

                // if you wanna open outside of app
                /*if (url.contains(URL)) {
                        view.loadUrl(url)
                        return false
                    }else {
                        // Otherwise, give the default behavior (open in browser)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }*/


                return true
            }


        }
        wv2.getSettings().setJavaScriptEnabled(true);
        wv2.getSettings().useWideViewPort = true
        wv2.getSettings().loadWithOverviewMode = true





        val model: CategoryViewModel by viewModels() {
            CategoryViewModel.Factory(
                (application as MyApplication),
                (application as MyApplication).repository
            )
        }
        //  model.insertbody()
        model.maildata.observe(this)
        {
            if (!once) {
                once = true
              //  val rawEmailString = it.get(id).description

                wv2.loadDataWithBaseURL(null,it.get(id).description!!, "text/html", "UTF-8",null);
                Log.d("setupwebview", it.get(id).description!!)


//                Log.d("setupwebview", rawEmailString!!)
//                val endingChar = '~'
//
//                val spannableString = SpannableString(rawEmailString)
//                Linkify.addLinks(spannableString, Linkify.WEB_URLS)
//               // binding.compose.setText(rawEmailString)
//                val pattern =  "\\b(?:https?://|www\\.)\\S+\\b"
//                val regex = Regex(pattern)
//                val matches = regex.findAll(rawEmailString.toString())
//               // Linkify.addLinks(binding.compose, matches, "");
//                val lparams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                lparams.setMargins(10,10,10,10)
////             /  / val matcher = pattern.matcher(rawEmailString)
//                for (matchResult in matches) {
//                    val url = matchResult.value
//                    // Process the link
//                    val webView = WebView(this)
//
//                    webView.webViewClient = WebViewClient()
//                    webView.loadUrl(url);
//                    webView.setLayoutParams(lparams);
//                    //binding.contentView.addView(webView);
//
//                        //matcher.replaceFirst("")
//                }




                //  wv1.loadDataWithBaseURL(null, rawEmailString!!, "text/html", "utf-8", null)
                //wv1.loadUrl(rawEmailString!!)
//                if (rawEmailString!!.startsWith("<")) {
//                    // Load HTML content into the WebView
//                      wv2.loadDataWithBaseURL(null, rawEmailString, "text/html", "UTF-8", null)
//                } else {
//                    // Convert plain text to HTML and load it into the WebView
//
//
//                        val htmlContent: String = convertPlainTextToHtml(rawEmailString)!!
//                    wv2.loadDataWithBaseURL(
//                        null,
//                        rawEmailString,
//                        "text/html",
//                        "UTF-8",
//                        null
//                    )
//
//
//                    val language = it.get(id).getimagedata()
//                    for (item in language.indices) {
//                        var im = ImageView(this)
//                        im.setImageURI(Uri.parse(language.get(item)))
//
////                        Glide.with(this)
////                            .asBitmap()
////                            .load(language.get(item))
////                            .into(object : BitmapImageViewTarget(im) {
////                                override fun onResourceReady(
////                                    resource: Bitmap,
////                                    transition: Transition<in Bitmap>?
////                                ) {
////                                    super.onResourceReady(resource, transition)
////                                    Log.d("TAG", "onResourceReady: "+"reso")
////                                }
////
////                                override fun onLoadFailed(errorDrawable: Drawable?) {
////                                    super.onLoadFailed(errorDrawable)
////                                    Log.d("TAG", "onResourceReady: "+"failed")
////                                }
////                            })
////                        binding.container.addView(im)
//
//
//                    }
//
//                }
            }

////        val htmlContent = ((((("<html><head><link rel=\\\"stylesheet\\\" type=\\\"text/css\\\" href=\\\"file:///android_asset/styles.css\\\"></head><body>"
////                + "<h1>" + rawEmailString) + "</h1>"
////                + "<div class=\"sender\">" + "me") + "</div>"
////                + "<div class=\"date\">" + "me") + "</div>"
////                + "<div class=\"content\">" + rawEmailString) + "</div>"
////                + "</body></html>")
//            // Display image URLs
//
//
//
//
//// or
//
//
//            //  wv1.loadUrl("https://mail.google.com/mail/u/0/#inbox/FMfcgzGsmhWhPRfKWrxjjdfhpcfVNxGZ")
//            //Log.d("TAG", "setupwebview: "+rawEmailString)
//
//
        }
    }



    val imageUrls = ArrayList<Bitmap>()

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            Log.d("TAG", "convertByteArrayToBitmap: " + bitmap)
        } catch (e: java.lang.Exception) {
            Log.d("TAG", "convertByteArrayToBitmap: " + e.message)
            e.printStackTrace()
            // Handle the exception if the byte array cannot be decoded into a bitmap
        }
        return bitmap
    }

    private fun convertPlainTextToHtml(plainText: String): String? {
        Log.d("TAG", "URLlink: "+"")
        val pattern: Pattern = Pattern.compile("\\\\b((http|https)://\\\\S+\\\\b)")




// Matcher to find URLs in the text

// Matcher to find URLs in the text
        val matcher: Matcher = pattern.matcher(plainText)
        val match: Matcher = pattern.matcher(plainText);
// Iterate through the matches and extract the URLs
        var urls: ArrayList<String> = arrayListOf()
        val textWithoutUrls = matcher.replaceAll("")
        val htmlBuilder = java.lang.StringBuilder()
        htmlBuilder.append("<html><body><pre>")

        // Iterate through the matches and extract the URLs


        // Convert plain text to HTML format


//            .append("<a href=\"").append(urls).append("\">").append(linkText).append("</a><br>")


//        htmlBuilder
//            .append("<span style='font-family: Arial; font-size: 13px; color: #000000;'>")
//            .append(textWithoutUrls)
//            .append("</span><br>")
//
//        val lparams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        lparams.setMargins(10,10,10,10)
//
//        while (match.find()) {
//            val url = match.group()
//            // Process the extracted URL as needed
//            Log.d("URLlink: ", url)
//            urls.add(url)
//            matcher.replaceFirst("")

//            htmlBuilder.append("<a href=\"").append(url).append("\">").append(url)
//                .append("</a><br>");

           // binding.webview1.loadUrl(url)
//
//
//            val webView = WebView(this)
//            webView.webViewClient = WebViewClient()
//            webView.loadUrl(url);
//            webView.setLayoutParams(lparams);
//            binding.contentView.addView(webView);

//            break
//
//        }

//        runOnUiThread {
//            if (urls.size!=0 && urls.size >= 0) {
//              //  Log.d("TAG", "setupwebview: " + urls.get(0))
//            }
//
//        }

        htmlBuilder.append("</pre>")
            .append("</body></html>");
        return htmlBuilder.toString()
    }

    private fun extractTextContent(decodedMessage: String): String {
        val document: Document = Jsoup.parse(decodedMessage)
        document.outputSettings(Document.OutputSettings().prettyPrint(false))

        // Extract the text from the parsed document
        return document.text()
    }

    fun showBottomSheet() {

        var bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogcustom);
        bottomSheetDialog.setContentView(R.layout.bottom_option);
        //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        bottomSheetDialog.show()

//        val addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance(R.layout.bottom_option)
//        addPhotoBottomDialogFragment.show(
//            supportFragmentManager,
//            ActionBottomDialogFragment.TAG)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.wv1.canGoBack()) {
            this.wv1.goBack()
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


}