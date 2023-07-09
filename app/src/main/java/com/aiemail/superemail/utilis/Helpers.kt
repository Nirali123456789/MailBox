package com.aiemail.superemail.utilis


import android.app.Activity
import android.app.AlertDialog
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aiemail.superemail.Fragments.SettingSubcomponentFragment
import com.aiemail.superemail.R
import com.aiemail.superemail.Receivers.YourDeviceAdminReceiver
import com.aiemail.superemail.prefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Draft
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.ModifyMessageRequest
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.mail.MessagingException


class Helpers {

    companion object {
        private val TIMEOUT = 1500
        public fun SetUpFullScreen(window: Window) {
            val windowInsetsController =
                WindowCompat.getInsetsController(window, window.decorView)
            // Configure the behavior of the hidden system bars.
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Add a listener to update the behavior of the toggle fullscreen button when
            // the system bars are hidden or revealed.
            window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
                // You can hide the caption bar even when the other system bars are visible.
                // To account for this, explicitly check the visibility of navigationBars()
                // and statusBars() rather than checking the visibility of systemBars().
                if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                    || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
                ) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

                } else {
                }
                view.onApplyWindowInsets(windowInsets)
            }
        }


        fun moveEmailToImportant(activity: Activity, emailId: String, label: String) {
            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val jsonFactory: JacksonFactory? = JacksonFactory.getDefaultInstance()


            // Load the credentials from your client_secret.json file
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            val credentials = GoogleAccountCredential.usingOAuth2(
                activity, (listOf("https://www.googleapis.com/auth/gmail.modify"))
            ).setSelectedAccountName(username)
            try {
                val gmail = Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                    .setApplicationName(activity.getString(R.string.app_name))
                    .build()
                val modifyRequest = ModifyMessageRequest().setAddLabelIds(listOf(label))
                    .setRemoveLabelIds(listOf("INBOX"))
                gmail.users().messages().modify("me", emailId, modifyRequest).execute()
                Handler(Looper.myLooper()!!).postDelayed({ activity.finish() }, 600)

            } catch (e: UserRecoverableAuthIOException) {
                val authorizationIntent: Intent = e.getIntent()
                activity.startActivityForResult(authorizationIntent, 101)
            }
        }

        @JvmStatic
        fun MoveFolder(activity: Activity, sender: String, label: String) {

            Thread {
                moveEmailToImportant(activity, sender, label)
            }.start()
        }

        fun DeleteMail(activity: Activity, messageId: String) {
            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            val gmail = Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                .setApplicationName(activity.getString(R.string.app_name))
                .build()
            val response = gmail.users().messages().trash("me", messageId).execute();

            if (response != null) {

                activity.runOnUiThread {
                    Toast.makeText(
                        activity,
                        "Email Deleted",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    activity.finish()
                }

            } else {
                // Email sending failed
                activity.runOnUiThread {
                    Toast.makeText(
                        activity,
                        "Email Failed to Delete",
                        Toast.LENGTH_LONG
                    )
                        .show()

                }

            }
        }

        fun MuteMail(activity: Activity, conversationId: String) {
            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            val gmail = Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                .setApplicationName(activity.getString(R.string.app_name))
                .build()


            var label = Label().setName("MUTED_LABEL").setLabelListVisibility("labelHide")
                .setMessageListVisibility("hide");
            label = gmail.users().labels().create("me", label).execute()
            val labelIdsToAdd = listOf(label.id)
            val request = ModifyMessageRequest().setAddLabelIds(labelIdsToAdd)
            gmail.users().messages().modify("me", conversationId, request).execute()

            val addLabelIds: MutableList<String> = ArrayList()
            addLabelIds.add("MUTED")
            try {
//                val modifyRequest = ModifyThreadRequest()
//                    .setAddLabelIds(addLabelIds)
//
//                gmail.users().threads().modify("me", conversationId, modifyRequest).execute()
            } catch (e: UserRecoverableAuthIOException) {
                val authorizationIntent: Intent = e.getIntent()
                activity.startActivityForResult(authorizationIntent, 101)
            }
        }

        //Print
        public fun print(context: Context, webView: WebView) {
            // Get a PrintManager instance
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            val jobName: String = context.getString(R.string.app_name) + " Document"

            // Get a print adapter instance
            val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName, printAdapter,
                PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            )
        }

        fun SaveAsPdf(htmlString: WebView, activity: Activity) {
            val printAttrs = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build()

            val pdfDocument = PrintedPdfDocument(activity, printAttrs)
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            htmlString?.let { view ->
                val bitmap = createBitmapFromWebView(view)
                canvas.drawBitmap(bitmap, 0f, 0f, null)

                pdfDocument.finishPage(page)

                val outputFile = File(activity.getExternalFilesDir(null), htmlString.title + ".pdf")
                try {
                    val outputStream = FileOutputStream(outputFile)
                    pdfDocument.writeTo(outputStream)
                    outputStream.close()

                    // PDF conversion completed successfully
                    // The generated PDF file is stored in `outputFile`

                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    pdfDocument.close()
                }
            }

        }

        private fun createBitmapFromWebView(webView: WebView): Bitmap {
            val picture = webView.capturePicture()
            val width = picture.width
            val height = picture.height

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            picture.draw(canvas)

            return bitmap
        }

        private var devicePolicyManager: DevicePolicyManager? = null
        private var deviceAdminReceiver: ComponentName? = null

        fun BlockSender(activity: Activity, conversationId: String) {
            // Assuming you have set up the necessary credentials and authenticated the user

// Create the Gmail service object
            // Assuming you have set up the necessary credentials and authenticated the user

            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)

// Create the Gmail service object
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            val gmail = Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                .setApplicationName(activity.getString(R.string.app_name))
                .build()

// Specify the sender's email address you want to block

//// Specify the sender's email address you want to block
//            val senderEmail = "sender@example.com"
//
//// Create the Filter object
//
//// Create the Filter object
//            val filter = Filter()
//            val criteria = FilterCriteria()
//            criteria.from = senderEmail
//            filter.setCriteria(criteria)
//
//            val action = FilterAction()
//            action.removeLabelIds =
//                listOf("INBOX") // Move the email to Trash or a designated folder
//
//            filter.setAction(action)

// Create the FilterRequest object

// Create the FilterRequest object
            //val request = Createf()
            // request.setAddFilter(filter)

// Create the filter

// Create the filter
//            gmail.users().settings().filters().create("me", request).execute()
            // Initialize DevicePolicyManager and ComponentName
            // Initialize DevicePolicyManager and ComponentName
            devicePolicyManager =
                activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
            deviceAdminReceiver = ComponentName(activity, YourDeviceAdminReceiver::class.java)


            // Block the email sender

            // Block the email sender
            blockEmailSender(conversationId)

        }

        private fun blockEmailSender(emailSender: String) {
            // Check if the app has device administrator privileges
            if (devicePolicyManager!!.isAdminActive(deviceAdminReceiver!!)) {
                // Block the email sender using DevicePolicyManager
                devicePolicyManager!!.setAccountManagementDisabled(
                    deviceAdminReceiver!!,
                    emailSender,
                    true
                )
            } else {
                // Request device administrator privileges
                // You can start an Intent to ask the user to activate device administrator for your app
                // See the DevicePolicyManager documentation for more information
            }
        }

        fun GetTimeZone(): TimeZone {

// Get the default timezone
            val timeZone = TimeZone.getDefault()

            // Get the timezone ID
            val timeZoneId = timeZone.id

            // Get the display name of the timezone
            val timeZoneDisplayName = timeZone.displayName
            return timeZone

        }

        fun SetUpSnooze() {

        }


        fun GmailService(activity: Activity): Gmail {
            // Create the Gmail service object
            // Assuming you have set up the necessary credentials and authenticated the user

            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)

// Create the Gmail service object
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            return Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                .setApplicationName(activity.getString(R.string.app_name))
                .build()
        }

        @Throws(IOException::class, MessagingException::class)
        fun createDraftEmail(activity: Activity, message: Message): Draft {
            // Create the Gmail service object
            // Assuming you have set up the necessary credentials and authenticated the user

            var myPref = activity.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
            var username = myPref.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)

// Create the Gmail service object
            val httpTransport =
                NetHttpTransport()
                    .createRequestFactory { request ->
                        request.connectTimeout = TIMEOUT
                        request.readTimeout = TIMEOUT
                    }.transport
            val credential1 = GoogleAccountCredential.usingOAuth2(
                activity, setOf<String>("https://www.googleapis.com/auth/gmail.modify")
            ).setSelectedAccountName(username)
            val gmail = Gmail.Builder(httpTransport, JacksonFactory(), credential1)
                .setApplicationName(activity.getString(R.string.app_name))
                .build()
            val message = message
            val draft = Draft().setMessage(message)
            return gmail.users().drafts().create("me", draft).execute()
        }

        fun ChangeFragment(activity: AppCompatActivity, layout: Int) {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingSubcomponentFragment.newInstance(layout))
                .commitNow()
        }

        fun getUserProfile(emailList: List<String>, apiKey: String){


            for (email in emailList) {
                val encodedEmail = java.net.URLEncoder.encode(email, "UTF-8")
                val urlStr = "http://picasaweb.google.com/data/entry/api/user/<nehavaidya95@gmail.com>?alt=json"
                val url = URL(urlStr)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    reader.close()
                    connection.disconnect()

                    // Process the response JSON to extract the photo URL
                    val photoUrl = extractPhotoUrl(response.toString())

                    println("Email: $email, Photo URL: $photoUrl")
                } else {
                    println("Request failed for email: $email. Response Code: $responseCode")
                }
            }





        }


        val extractRecipientIdFromAddress: (String, String) -> String =
            { address, domain ->
                if (address.isEmpty() || domain.isEmpty() ) {
                    address
                } else {
                    address.substring(0, address.length - (domain.length + 1))
                }
            }


        fun extractPhotoUrl(responseJson: String): String {
            // Parse the response JSON and extract the photo URL
            // Modify this method according to the structure of the response JSON
            // For example, if the photo URL is nested under "photos[0].url", you can use a JSON parser to extract it
            // and return the URL as a string.
            return "" // Replace this with your code to extract the photo URL
        }

        fun showDialog(activity: Activity, list: ArrayList<String>) {
            val array: ArrayList<Int> = arrayListOf()
            array.add(1)
            array.add(2)
            array.add(3)

            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle("Pick one")
            val displayValues = ArrayList<Int>()
            for (entity in array) {
                displayValues.add(entity)
            }
            val adapter: ArrayAdapter<Int> = ArrayAdapter<Int>(
                activity,
                android.R.layout.simple_list_item_1,
                array
            )


            builder.setSingleChoiceItems(adapter, 0,
                DialogInterface.OnClickListener { dialog, which ->
                    val selectedItem: Int = array[which]
                    prefs.NoOfLine = selectedItem
                    dialog.dismiss()
                })
            builder.show()
        }

        fun Haptics(activity: Activity, view: EditText) {
            var myView: EditText = view
            lateinit var myVib: Vibrator


            myVib = activity.getSystemService(VIBRATOR_SERVICE) as Vibrator
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                myVib.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                );
            } else {
                //deprecated in API 26
                myVib.vibrate(500);
            }


        }

        fun Sound(activity: Context): AudioAttributes? {


            return AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        }

        fun SwitchToggle(
            preferenceManager: PreferenceManager,
            switch: SwitchCompat,
            saved: String,
            isBoolean: Boolean,
            nameString: String
        ) {
            if (isBoolean) {
                switch.isChecked = preferenceManager.getBoolean(saved)
            }
            switch.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state

                if (isBoolean) {
                    if (isChecked) {

                        preferenceManager.SetBoolean(saved, true)
                        // Switch is checked
                        // Do something
                    } else {
                        preferenceManager.SetBoolean(saved, false)
                        // Switch is unchecked
                        // Do something else
                    }

                } else {
                    if (isChecked) {

                        preferenceManager.SetString(saved, nameString)
                        // Switch is checked
                        // Do something
                    } else {
                        preferenceManager.SetString(saved, nameString)
                        // Switch is unchecked
                        // Do something else
                    }
                }

            }
        }

        fun Visibility(view: View, preferenceManager: PreferenceManager, value: String) {

            if (preferenceManager.getBoolean(value)) view.visibility = View.VISIBLE
            else view.visibility = View.GONE


        }

        fun getAppVersion(context: Context): String {
            var version: String = ""
            try {
                val manager: PackageManager = context.getPackageManager()
                val info: PackageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0
                )
                version = info.versionName
                Log.d("TAG", "checkVersion.DEBUG: App version: $version")
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return version

        }

        fun setLocal(activity: Context, languageCode: String) {
            Log.d("TAG", "onCreateView: "+languageCode)
            val languageCode = languageCode // The desired language code (e.g., "es" for Spanish)
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(locale)
                config.setLocales(localeList)
                activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
            } else {
                activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
            }
        }
        fun OpenLink(activity: Activity) {
            val url = activity.resources.getString(R.string.privacy_policy)

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // This snippet hides the system bars.
        public fun hideSystemUI(mDecorView:View) {
            // Set the IMMERSIVE flag.
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.
            mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
            )
        }









        fun getDisplayableTime(delta: Long): String {
            val sdf = "EEE MMM dd "
            val formatter = SimpleDateFormat(sdf)
            val difference: Long
            val mDate = System.currentTimeMillis()

            if (mDate > delta) {
                difference = mDate - delta
                val seconds = TimeUnit.MILLISECONDS.toSeconds(difference)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)
                val hours = TimeUnit.MILLISECONDS.toHours(difference)
                val days = TimeUnit.MILLISECONDS.toDays(difference)
                val months = days / 31
                val years = days / 365

                if (seconds < 0) {
                    return "not yet"
                } else if (seconds < 60) {
                    return if (seconds == 1L) {
                        "one second ago"
                    } else {
                        "$seconds seconds ago"
                    }
                } else if (seconds < 120) {
                    return "a minute ago"
                } else if (seconds < 2700) { // 45 * 60
                    return "$minutes minutes ago"
                } else if (seconds < 5400) { // 90 * 60
                    return "an hour ago"
                } else if (seconds < 86400) { // 24 * 60 * 60
                    return "$hours hours ago"
                } else if (seconds < 172800) { // 48 * 60 * 60
                    return "yesterday"
                } else if (seconds < 2592000) { // 30 * 24 * 60 * 60
                    return  formatter.format(delta)
                } else if (seconds < 31104000) { // 12 * 30 * 24 * 60 * 60
                    return  formatter.format(delta)
                } else {
                    return if (years <= 1) {
                        "one year ago"
                    } else {
                        "$years years ago"
                    }
                }
            }
            return ""
        }





    }


}