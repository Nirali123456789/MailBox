package com.aiemail.superemail.Activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat.animate
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment.ItemClickListener
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.R
import com.aiemail.superemail.chinalwb.are.Text_Operations.DemoUtil
import com.aiemail.superemail.databinding.ActivityDirectComposeBinding
import com.chinalwb.are.android.inner.Html
import com.chinalwb.are.styles.toolbar.IARE_Toolbar
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Video
import com.chinalwb.are.styles.toolitems.IARE_ToolItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import io.karn.notify.Notify
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource


class DirectComposeActivity : AppCompatActivity(),ItemClickListener {

    private lateinit var binding: ActivityDirectComposeBinding
    private val REQ_WRITE_EXTERNAL_STORAGE = 10000
    private lateinit var mToolbar: IARE_Toolbar
    var click:Boolean=false
    private val TIMEOUT = 1500
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    companion object {
        // Request code for creating a PDF document.

        const val CREATE_FILE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mToolbar=binding.areToolbar

        option1()


        binding.back.setOnClickListener {
         onBackPressed()
        }
        if (intent.extras!=null)
        {
            binding.email.setText(intent.extras!!.getString("data"))
        }else
        binding.email.setText("New message")
        binding.apply.setOnClickListener {
            binding.option.visibility=View.VISIBLE
            binding.tool.visibility = View.GONE
            val html: String = binding.sub.getHtml()
            DemoUtil.saveHtml(this, html)
        }
        binding.bb.setOnClickListener {
            if (!click) {
                click=!click
                binding.group1.visibility = (View.VISIBLE);
                binding.group2.visibility = (View.VISIBLE);
                animate(binding.bb)
                    .alpha(1f)
                    .setDuration(800)
                    .setListener(null);
            }else
            {
                click=!click
                binding.group1.visibility = (View.GONE);
                binding.group2.visibility = (View.GONE);
                animate(binding.bb)
                    .alpha(1f)
                    .setDuration(800)
                    .setListener(null);
            }
        }
        ItemClick()

       binding.bottombar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.clock1->MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment")
                R.id.text1-> {
                    binding.bottombar.visibility=View.GONE
                   binding.tool.visibility = View.VISIBLE
                }
                R.id.folder->showBottomSheetpurchse()
                R.id.attach1->
                {
                   var bottomSheetDialog =  BottomSheetDialog(this,R.style.BottomSheetDialogcustom);
                    bottomSheetDialog.setContentView(R.layout.layout_attachment);
                  //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                    bottomSheetDialog.show()
                }


            }
            true
        }
// MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment")
    }
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




    private fun ItemClick() {
        binding.attach.setOnClickListener {
            var bottomSheetDialog =  BottomSheetDialog(this,R.style.BottomSheetDialogcustom);
            bottomSheetDialog.setContentView(R.layout.layout_attachment);
            bottomSheetDialog.findViewById<ImageView>(R.id.file_attach)!!.setOnClickListener {
                createFile()
            }
            //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
            bottomSheetDialog.show()
            createFile()
        }
        binding.file.setOnClickListener {
            showBottomSheetpurchse()
        }
        binding.text.setOnClickListener {
            binding.option.visibility=View.GONE
            binding.tool.visibility = View.VISIBLE
        }

        binding.clock.setOnClickListener {
            MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment"
            )

        }
        binding.send.setOnClickListener {
            MyCustomDialog(R.layout.layout_send_later).show(supportFragmentManager, "MyCustomFragment")
        }
        binding.add.setOnClickListener {
            MyCustomDialog(R.layout.layout_common_dialog).show(supportFragmentManager, "MyCustomFragment")
        }
        binding.pin.setOnClickListener {
            Thread()
            {
                sendEmail()
            }.start()
        }


    }
    private fun sendEmail() {
        var myPref =  getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username= myPref.getString("username","");
        var subject=binding.subEdit.text.trim()
        var messageBody= Html.fromHtml(Html.fromHtml( binding.sub.html).toString())

        val email1 = createEmail("swainfo.nirali@gmail.com", ""+subject, ""+messageBody)
        val message = createMessageWithEmail(email1)
        // Build a new authorized API client service.
        val httpTransport =
            NetHttpTransport()
                .createRequestFactory { request ->
                    request.connectTimeout = TIMEOUT
                    request.readTimeout = TIMEOUT
                }.transport

        val credential1 = GoogleAccountCredential.usingOAuth2(
            this, setOf<String>(GmailScopes.GMAIL_READONLY)
        ).setSelectedAccountName(username)
        try {
//            credential1.token.refr
        } catch (e: IOException) {
            Log.e("TAG", e.message!!)

        }
        val gmailService = Gmail.Builder(
            httpTransport,
            JacksonFactory(),
            credential1
        )
            .setApplicationName("Spark")
            .build()
        GlobalScope.launch {
            val response =
                gmailService.users().messages().send(username, message).execute()
            if (response != null) {
                // Email sending was successful

                Log.d("TAG", "fetchMails: " + "sent")
            } else {
                // Email sending failed
                System.out.println("Failed to send the email.");

                Log.d("TAG", "fetchMails: " + "failed")
            }
        }

    }




        private fun createFile() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type =
                "*/*" // Set the MIME type of files to be selected (you can set a specific type if required)
            getResult.launch(intent)
        }





    lateinit var fileUriString:Uri

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    // Retrieve the selected file URI
                    val fileUri: Uri = it.data!!.data!!
                    Log.d("TAG", "attachedfile: "+fileUri)
                     fileUriString = fileUri

                    // Use the file URI as needed (e.g., send it in an email)
                    // ...
                }
            }
        }


    private fun createEmail(to: String, subject: String, messageBody: String): MimeMessage {

        val attachmentFilePath =applicationContext.assets.open("pdf.docx")
        var bcc=""+binding.bB.text
        var cc=""+binding.cc.text
        var to=""+ binding.editext.text
        val props = Properties()
        val session = Session.getDefaultInstance(props, null)

        val email = MimeMessage(session)
        email.setFrom(InternetAddress())
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(to))
        if (!bcc.length.equals(0))
        {
            email.addRecipient(javax.mail.Message.RecipientType.BCC, InternetAddress(bcc))

        }
        if(!cc.length.equals(0))
        {
            email.addRecipient(javax.mail.Message.RecipientType.CC, InternetAddress(cc))

        }
        email.subject = subject

        val bodyPart = MimeBodyPart()
        bodyPart.setContent(messageBody, "text/plain")

        val attachmentBodyPart = MimeBodyPart()

        var assetManager:AssetManager=assets
        val attachmentFileName = "pdf.docx"
        val inputStream: InputStream = assetManager.open(attachmentFileName)
        val source: DataSource = ByteArrayDataSource(inputStream, "application/octet-stream")
        attachmentBodyPart.dataHandler = DataHandler(source)
        val pdfDocument = convertUriToPdfDocument(fileUriString)
        attachmentBodyPart.fileName = RetrieveFile(pdfDocument!!).absolutePath

        val multipart: Multipart = MimeMultipart()
        multipart.addBodyPart(bodyPart)
        multipart.addBodyPart(attachmentBodyPart)

        email.setContent(multipart)

        return email
    }
fun RetrieveFile(document: PdfDocument):File
{
    Log.d("TAG", "RetrieveFile: "+fileUriString)

    var pdfDirPath =  File(getFilesDir(), "pdfs");
    pdfDirPath.mkdirs();

    var file =  File(pdfDirPath, "attachment.pdf");
    file.deleteOnExit();

    var uri = FileProvider.getUriForFile(this, getPackageName() + ".file.provider", file);
    var os: FileOutputStream? = null;
    try {

        os =  FileOutputStream(file);

        document.writeTo(os);
        document.close();
        os.close();
    } catch ( e:Exception) {
        e.printStackTrace();
    }
    

    return File(uri.toString())
}
    private fun convertUriToPdfDocument(uri: Uri): PdfDocument? {
        var pdfDocument = PdfDocument()
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            if (parcelFileDescriptor != null) {
                val fileDescriptor = parcelFileDescriptor.fileDescriptor

                // Load the PDF document from the file descriptor
              //  pdfDocument = PdfDocumentAdapter.loadDocument(fileDescriptor)

                // Close the file descriptor
                //parcelFileDescriptor.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pdfDocument
    }
    private fun createMessageWithEmail(email: MimeMessage): Message {
        val buffer = ByteArrayOutputStream()
        email.writeTo(buffer)
        val bytes = buffer.toByteArray()
        val encodedEmail = Base64.getUrlEncoder().encodeToString(bytes)
        val message = Message()
        message.raw = encodedEmail
        return message
    }

    fun showBottomSheetpurchse() {

        val addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG

        )

    }

    override fun onBackPressed() {
        MyCustomDialog(R.layout.layout_discard_dialog).show(supportFragmentManager, "MyCustomFragment")
       // super.onBackPressed()
    }
    private fun option1() {
        mToolbar = findViewById(R.id.areToolbar)
        val bold: IARE_ToolItem = ARE_ToolItem_Bold()
        val italic: IARE_ToolItem = ARE_ToolItem_Italic()
        val underline: IARE_ToolItem = ARE_ToolItem_Underline()
        val strikethrough: IARE_ToolItem = ARE_ToolItem_Strikethrough()
        val quote: IARE_ToolItem = ARE_ToolItem_Quote()
        val listNumber: IARE_ToolItem = ARE_ToolItem_ListNumber()
        val color: IARE_ToolItem = ARE_ToolItem_BackgroundColor()
        val listBullet: IARE_ToolItem = ARE_ToolItem_ListBullet()
        val hr: IARE_ToolItem = ARE_ToolItem_Hr()
        val link: IARE_ToolItem = ARE_ToolItem_Link()
        val subscript: IARE_ToolItem = ARE_ToolItem_Subscript()
        val superscript: IARE_ToolItem = ARE_ToolItem_Superscript()
        val left: IARE_ToolItem = ARE_ToolItem_AlignmentLeft()
        val center: IARE_ToolItem = ARE_ToolItem_AlignmentCenter()
        val right: IARE_ToolItem = ARE_ToolItem_AlignmentRight()
        val image: IARE_ToolItem = ARE_ToolItem_Image()
        val video: IARE_ToolItem = ARE_ToolItem_Video()
        val at: IARE_ToolItem = ARE_ToolItem_At()
        mToolbar.addToolbarItem(bold)
        mToolbar.addToolbarItem(italic)
        mToolbar.addToolbarItem(underline)
        mToolbar.addToolbarItem(strikethrough)
        mToolbar.addToolbarItem(quote)
        mToolbar.addToolbarItem(color)
        mToolbar.addToolbarItem(listNumber)
        mToolbar.addToolbarItem(listBullet)
        mToolbar.addToolbarItem(hr)
        mToolbar.addToolbarItem(link)
        mToolbar.addToolbarItem(subscript)
        mToolbar.addToolbarItem(superscript)
        mToolbar.addToolbarItem(left)
        mToolbar.addToolbarItem(center)
        mToolbar.addToolbarItem(right)
        mToolbar.addToolbarItem(image)
        mToolbar.addToolbarItem(video)
        mToolbar.addToolbarItem(at)

        binding.sub.setToolbar(mToolbar)
            //initToolbarArrow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_WRITE_EXTERNAL_STORAGE) {
            val html: String = binding.sub.getHtml()
            DemoUtil.saveHtml(this, html)
            return
        }
       // if (useOption1) {
            mToolbar.onActivityResult(requestCode, resultCode, data)
       // } else {
            //this.arEditor.onActivityResult(requestCode, resultCode, data)
      //  }
    }

    override fun onItemClick(item: String?) {
    }
}