package com.aiemail.superemail.Activities

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivityDirectComposeBinding
import com.aiemail.superemail.databinding.ActivityTeamBinding
import com.aiemail.superemail.databinding.LayoutTeamsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class TeamActivity : AppCompatActivity() {
    private lateinit var emailAddresses: List<String>
    private lateinit var binding: ActivityTeamBinding
    public companion object {
        const val REQUEST_AUTHORIZATION = 101;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetUpUI()


    }

    private fun SetUpUI() {
        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");
        binding.account.setText(username)
        binding.createTeam.setOnClickListener {
            TeamSelection(binding.teamName.text.toString())
        }
    }


    private fun getGmailService(account: GoogleSignInAccount): Gmail? {
        val httpTransport: HttpTransport = NetHttpTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
        val credential = GoogleAccountCredential.usingOAuth2(
            this, setOf(GmailScopes.GMAIL_MODIFY)
        )
            .setBackOff(ExponentialBackOff())
            .setSelectedAccount(account.account)
        return Gmail.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName("Your Application Name")
            .build()
    }

    // Use this method to create a team with email IDs

    private fun createTeam(teamName: String, members: MutableList<String>):List<com.google.api.services.gmail.model.Message> {
        var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        var username = myPref.getString("username", "");

        val subject = "New Team: $teamName"
        val body = "Hello Team,\n\nWe are excited to announce the creation of a new team: $teamName!\n\nTeam members:\n${members.joinToString("\n")}\n\nLet's collaborate and achieve great things together!\n\nBest regards,\nYour Organization"
        val messages = mutableListOf<com.google.api.services.gmail.model.Message>()
        val email = MimeMessage(Session.getDefaultInstance(Properties(), null))
        email.setFrom(InternetAddress(username))
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress("nehavaidya95@gmail.com")) // Replace with the recipient email address for team notifications
        email.subject = subject
        email.setText(body)
        Log.d("TAG", "createTeam: "+username)

        try {

                val buffer = ByteArrayOutputStream()
                email.writeTo(buffer)
                val bytes = buffer.toByteArray()
                val encodedEmail = Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP)

                val message = com.google.api.services.gmail.model.Message()
                message.raw = encodedEmail
            messages.add(message)
            Log.d("TAG", "createTeam: "+message)
            return  messages



        } catch (e: IOException) {
            Log.d("TAG", "createTeam: "+e.message)
            // Handle IOException
            e.printStackTrace()
        } catch (e: MessagingException) {
            Log.d("TAG", "createTeam: "+e.message)
            // Handle MessagingException
            e.printStackTrace()
        }

        // Return an empty message if an exception occurs
        return listOf(com.google.api.services.gmail.model.Message())
    }



    private fun createMessageWithEmail(email: MimeMessage): com.google.api.services.gmail.model.Message {

        val buffer = ByteArrayOutputStream()
        email.writeTo(buffer)
        val bytes = buffer.toByteArray()
        val encodedEmail = Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP)

        val message = com.google.api.services.gmail.model.Message()
        message.raw = encodedEmail
        return message
    }

    private fun createInviteEmail(toEmail: String, teamName: String, subject: String, body: String): MimeMessage {
        val props = Properties()
        val session = Session.getDefaultInstance(props, null)
        val email = MimeMessage(session)
        email.setFrom(InternetAddress("your-email@example.com"))
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(toEmail))
        email.subject = subject
        email.setText(body)

        // Add attachment
        val attachmentData: ByteArray = getAttachmentData() // Replace with your attachment data
        val attachmentName = "attachment.txt" // Replace with your attachment name
        val attachmentDataSource = ByteArrayDataSource(attachmentData, "application/octet-stream")
        email.setDataHandler(DataHandler(attachmentDataSource))
        email.setFileName(attachmentName)

        return email
    }

    private fun getAttachmentData(): ByteArray {
        // Replace with your logic to retrieve attachment data
        return ByteArray(0)
    }

    private class ByteArrayDataSource(private val data: ByteArray, private val type: String) :
        DataSource {
        override fun getInputStream(): java.io.InputStream {
            return data.inputStream()
        }

        override fun getOutputStream(): java.io.OutputStream {
            throw UnsupportedOperationException("Not supported")
        }

        override fun getContentType(): String {
            return type
        }

        override fun getName(): String {
            return "ByteArrayDataSource"
        }
    }



    fun TeamSelection(teamName: String) {
        // Create the team

// Create the team
        val emailIds: MutableList<String> = ArrayList()
        emailIds.add("nehavaidya95@gmail.com")
        emailIds.add("email2@example.com")
        var acc = GoogleSignIn.getLastSignedInAccount(this)
// Call the createTeam method
// Call the createTeam method
        Thread {
          val message=  createTeam(teamName, emailIds)
            sendInvitations(getGmailService(acc!!)!!, message)
        }.start()
    }
    private fun sendInvitations(gmailService: Gmail, messages: List<com.google.api.services.gmail.model.Message>) {
        for (message in messages) {
            gmailService.users().messages().send("me", message).execute()
        }
    }


}