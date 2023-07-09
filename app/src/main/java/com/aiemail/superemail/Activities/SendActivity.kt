package com.aiemail.superemail.Activities

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.viewmodel.FullMessagelViewmodel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

class SendActivity : AppCompatActivity() {
    var fileUriString: Uri? = null
    private val TIMEOUT = 1500
    var imageResources: ArrayList<File> = arrayListOf()
    var sender_id:String=""
    val fullmessagemodel: FullMessagelViewmodel by viewModels() {
        FullMessagelViewmodel.Factory(
            (application as MyApplication), (application as MyApplication).fullmessagerepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        if (intent.extras != null) {
            if (intent.extras!!.getBoolean("fromsend")!!.equals(true)) {
          sender_id = intent.extras!!.getString("senderid")!!
                Thread {
                    Log.d("TAG", "sendEmailFullmessage: " + sender_id)
                    sendEmailFullmessage(fullmessagemodel, sender_id)
                }.start()
                return
            }

        }
    }

    fun sendEmailFullmessage(viewmodel: FullMessagelViewmodel, sender_id: String) {
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            Log.d("TAG", "sendEmailFullmessage: " + sender_id)
            val user = viewmodel.getEmail(sender_id)


            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                user.observe(this@SendActivity)
                {
                    val user = it
                    Log.d("TAG", "sendEmailFullmessage: " + user.content)
                    if (user != null) {
                        Thread {
                            sendEmailFullmessage_(MyApplication.instance.context!!, user)
                        }.start()
                    }
                }
                // Update UI with the fetched data

            }
        }

    }

    private fun sendEmailFullmessage_(context: Context, fullMessage: FullMessage) {
        var myPref = context.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");


        // Build a new authorized API client service.
        val httpTransport = NetHttpTransport().createRequestFactory { request ->
            request.connectTimeout = TIMEOUT
            request.readTimeout = TIMEOUT
        }.transport

        val credential1 = GoogleAccountCredential.usingOAuth2(
            context, setOf<String>(GmailScopes.GMAIL_SEND)
        ).setSelectedAccountName(username)
        try {

        } catch (e: IOException) {
            Log.e("TAG", e.message!!)

        }
        val email1 =
            createEmail(
                fullMessage.recipient!!,
                fullMessage.subject!!,
                fullMessage.content!!,
                fullMessage
            )
        val message = createMessageWithEmail(email1)
        val gmailService = Gmail.Builder(
            httpTransport, JacksonFactory(), credential1
        ).setApplicationName("Spark").build()


            val response =
                gmailService.users().messages().send(username, message).execute()
            if (response != null) {
                // Email sending was successful
                System.out.println("Successfully to send the email." + fullMessage.sender+">>>"+convertRawDataToPlainText(message.raw));
                //finish()


            } else {
                // Email sending failed
                System.out.println("Failed to send the email.");

                Log.d("TAG", "fetchMails: " + "failed")
            }



    }

    //Email creates with sender,receiver and all attachments
    private fun createEmail(
        to: String,
        subject: String,
        messageBody: String,
        fullMessage: FullMessage
    ): MimeMessage {

        val attachmentFilePath = applicationContext.assets.open("pdf.docx")
        var bcc = fullMessage.bcc
        var cc = fullMessage.cc
//        var to = "" + binding.editext.text
        val props = Properties()
        val session = Session.getDefaultInstance(props, null)

        val email = MimeMessage(session)
        email.setFrom(InternetAddress())
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(to))
        if (!bcc!!.length.equals(0)) {
            email.addRecipient(javax.mail.Message.RecipientType.BCC, InternetAddress(bcc))

        }
        if (!cc!!.length.equals(0)) {
            email.addRecipient(javax.mail.Message.RecipientType.CC, InternetAddress(cc))

        }
        email.subject = subject

        val bodyPart = MimeBodyPart()
        bodyPart.setContent(messageBody, "text/plain")

        val attachmentBodyPart = MimeBodyPart()

        var assetManager: AssetManager = assets
        val attachmentFileName = "pdf.docx"
        val inputStream: InputStream = assetManager.open(attachmentFileName)
        val source: DataSource = ByteArrayDataSource(inputStream, "application/octet-stream")
        attachmentBodyPart.dataHandler = DataHandler(source)
        //fileUriString = fullMessage.fileUriString
        if (fileUriString != null) {
            //  val pdfDocument = convertUriToPdfDocument(fileUriString!!)
            // attachmentBodyPart.fileName = RetrieveFile(pdfDocument!!).absolutePath
        }

        val multipart: Multipart = MimeMultipart("mixed")
        multipart.addBodyPart(bodyPart)
        imageResources = fullMessage.imageResources
        // Add attachments to the multipart
        // Add attachments to the multipart
        for (file in imageResources) {
            val attachmentPart = MimeBodyPart()
            val fileDataSource = FileDataSource(file)
            attachmentPart.dataHandler = DataHandler(fileDataSource)
            attachmentPart.fileName = file.name
            multipart.addBodyPart(attachmentPart)
        }


        email.setContent(multipart)

        return email
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

    fun convertRawDataToPlainText(binaryData: String): String? {
        val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(binaryData)

        // Convert the decoded bytes to a string
        val decodedString = String(decodedBytes)


        return decodedString
    }
}