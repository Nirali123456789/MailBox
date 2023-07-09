//# Created by Nirali Pandya
////* *Main Repository to make Gmail Api call
////*Authenticate with TOken
////* First Made Message List call @ExtractMails() function is starting Entery
////* than iterate json response data
////* @senders.forEach() method For Getting All mails.


package com.aiemail.superemail.Repository


import android.R.id.message
import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Source
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.Rooms.EmailDao
import com.aiemail.superemail.Singletone.singleton_.singleEmail
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PeopleHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePartHeader
import com.google.api.services.people.v1.PeopleService
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.net.SocketTimeoutException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


class FetchRepository(private val foodItemDao: EmailDao, var page: Int = 1) {

    val TAG = javaClass.simpleName
    var arrayList: ArrayList<Email> = arrayListOf()
    var messagelist: ArrayList<FullMessage> = arrayListOf()
    private val MAX_FETCH_THREADS = Runtime.getRuntime().availableProcessors()
    val executors = Executors.newFixedThreadPool(MAX_FETCH_THREADS)
    private val TIMEOUT = 1500

    private val _todoListFlow: MutableLiveData<MutableList<Email>> = MutableLiveData()
    val todoListFlow: LiveData<MutableList<Email>> get() = _todoListFlow

    private val _messageList: MutableLiveData<MutableList<FullMessage>> = MutableLiveData()
    val fullmessage: LiveData<MutableList<FullMessage>> get() = _messageList

    private val _emaillist: MutableLiveData<MutableList<Email>> = MutableLiveData()
    val emaillist: LiveData<MutableList<Email>> get() = _emaillist
    public var Allmail = MutableLiveData<Email>()
    public var Section = MutableLiveData<Section>()
    var job: CompletableJob? = null
    val httpTransport =
        NetHttpTransport()
            .createRequestFactory { request ->
                request.connectTimeout = TIMEOUT
                request.readTimeout = TIMEOUT
            }.transport

    fun getCallData() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                _todoListFlow.value = arrayList

            }

        }
    }

    fun adddata(articles: MutableList<Email>) {
        foodItemDao.insertAll(articles as ArrayList<Email>)
    }

    fun deletedata() {
        foodItemDao.DeleteMails()


    }

    fun getEmails(): LiveData<List<Email>> {
        return foodItemDao.getAllMail()
    }

    fun getUnReadEmails(): LiveData<List<Email>> {
        return foodItemDao.getUnreadMail()
    }

    fun getSnooze(): LiveData<List<Email>> {
        return foodItemDao.getSnoozeItems()
    }


    private fun searchSeenMessages(
        USER_EMAIL: String,
        service: Gmail.Users
    ): MutableList<Message?>? {
        val query = "is:seen"
        val response = service.messages().list(USER_EMAIL).setQ(query).execute()

        return response.messages
    }


    fun fetchMails(
        activity: Activity,
        label: String,
        email: String,
        account: GoogleSignInAccount,
        id: Int,
        from: Boolean = false
    ) {

        GlobalScope.launch {

            ExtractMails(activity, email, account.email!!, id, from)


        }
    }


    fun cancelJobs(){
        job?.cancel()
    }

    private fun Gmail.processFroms(
        activity: Activity,
        user: String,

        label: Label, from: Boolean,
        process: (String) -> Unit
    ) {
        runBlocking(dispatcher) {

            processMessages(user, label) { message ->


                fun fetchAndProcess() {
                    job = Job()

                    val fullMessage =
                       users().messages().get("me", message.id)
                            .setFormat("full")
                            .execute()

                            job?.let { theJob ->
                                CoroutineScope(IO ).launch {
                                    //BackgroundTask
                                    val list_ = execute(activity, fullMessage, from)


                                    withContext(Main) {
//                                        value = list_
                                        theJob.complete()
                                    }
                                }

                            }

                        }



                getCallData()
                fetchAndProcess()


            }
        }
    }

    fun execute(activity: Activity, message1: Message, from: Boolean): Email {
        val myPref =
            activity.getSharedPreferences(
                "APP_SHARED_PREF",
                Context.MODE_PRIVATE
            );

        try {




            val internalDateMillis = message1.internalDate
//                            val date = Date(internalDateMillis)
//                            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())


            singleEmail.date =
                Helpers.getDisplayableTime(internalDateMillis)

            singleEmail.type = 0

            if (!from) {
                if (!arrayList.contains(singleEmail)) {
                    arrayList.add(singleEmail)
                    CoroutineScope(Dispatchers.Main).launch(
                        Dispatchers.IO
                    ) {
                        foodItemDao.insert(singleEmail)

                    }
                }
            }


//

            val isUnread = message1.labelIds.contains("UNREAD")
            Log.d(TAG, "isunread: " + isUnread)
            singleEmail.isUnread = isUnread
            singleEmail.title = message1.snippet
            singleEmail.msgid = message1.id
            singleEmail.date =
                Helpers.getDisplayableTime(internalDateMillis)
            singleEmail.type = 1
            singleEmail.thread_id = message1.threadId
            message1.payload.headers.stream()
                .filter { header: MessagePartHeader -> header.name == "Subject" }
                .findFirst()
                .map { obj: MessagePartHeader ->

                    //article4.content = obj.value
                    singleEmail.author = obj.value


                }
            val senderId: String =
                message1.getPayload().getHeaders()
                    .stream()
                    .filter { header ->
                        header.getName().equals("From")
                    }
                    .findFirst()
                    .orElse(MessagePartHeader())
                    .getValue()
            // Get the sender's profile picture URL


            var headers = message1.getPayload().getHeaders()
            for (header in headers) {
                if (header.name == "From") {


                    // Extract the sender's email address and name from the "From" header
                    var senderEmail = header.value.substring(
                        header.value.lastIndexOf("<") + 1,
                        header.value.lastIndexOf(">")
                    )
                    var senderName =
                        header.value.substring(
                            0,
                            header.value.lastIndexOf("<")
                        )
                            .trim { it <= ' ' }
                    singleEmail.sender = senderName

                    singleEmail.senderEmail = senderEmail
                    if (!singleEmail.senderEmail.equals("")) {

                    }

                    break
                }
            }


            var body: String? = ""

            if (message1.payload.mimeType == "text/html") {
                // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                body = message1.payload.body.data
                val decodedBytes: ByteArray =
                    Base64.getUrlDecoder().decode(body)
                val decodedString = String(decodedBytes)
                singleEmail.description = decodedString
                // Log.d(TAG, "fetchAndProcess:1 " + decodedString)

            } else if (message1.payload.mimeType == "multipart/alternative") {

                val parts = message1.payload.parts
                for (part in parts) {
                    Log.d(TAG, "fetchAndProcess: " + part.mimeType)

                    if (part.mimeType == "text/x-amp-html") {
                        var body_attach =
                            convertRawDataToPlainText(part.body.data)!!
                        singleEmail.description = body_attach


                    }
                    if (part.mimeType == "text/html") {
                        var body_attach =
                            convertRawDataToPlainText(part.body.data)!!
                        singleEmail.description = body_attach


                    }


                    if (part.mimeType == "text/plain") {
                        var source = Source()
                        source.id = "2"
                        singleEmail.type = 1
                        var body_attach: String = ""
                        body_attach =
                            convertRawDataToPlainText(part.body.data)!!
                        // Extract the text content from the full body
                        val textContent =
                            body_attach.replace(Regex("<.*?>"), "")
                        singleEmail.content = textContent


                    }
                    arrayList.add(singleEmail)


                }
            }


            if (!singleEmail.title.isNullOrBlank() || !singleEmail.content.isNullOrBlank() || !singleEmail.author.isNullOrBlank()) {

                if (singleEmail.title.equals("") && !singleEmail.content.equals(
                        ""
                    )
                )
                    singleEmail.title = singleEmail.content
                else if (singleEmail.title.equals("") && !singleEmail.author.equals(
                        ""
                    )
                )
                    singleEmail.title = singleEmail.author


                singleEmail.isInbox = true
                CoroutineScope(Dispatchers.IO).launch {
                    foodItemDao.insert(singleEmail)
                }


            }


        } catch (e: SocketTimeoutException) {

            // Process eventual failures.
            // Restart request on socket timeout.
            e.printStackTrace()
            execute(activity, message1, from)
        } catch (e: Exception) {
            Log.d(TAG, "execute: "+e.message)
            // Process eventual failures.
            e.printStackTrace()
        }
        return singleEmail

    }


    private fun String.parseAddress(): String {
        return if (contains("<")) {
            substringAfter("<").substringBefore(">")
        } else {
            this
        }
    }

    fun convertRawDataToPlainText(binaryData: String): String? {
        val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(binaryData)

        // Convert the decoded bytes to a string
        val decodedString = String(decodedBytes)


        return decodedString
    }

    val dispatcher = object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            executors.execute(block)
        }
    }

    fun ExtractMails(
        activity: Activity,
        ac: String,
        accontname: String,
        labelid: Int,
        from: Boolean
    ) {

        // Build a new authorized API client service.

//

        val credential1 = GoogleAccountCredential.usingOAuth2(
            activity, setOf<String>(GmailScopes.GMAIL_READONLY)
        ).setSelectedAccountName(accontname)
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

        val user = ac


        val labelList = gmailService.users().labels().list(user)
            .execute()
        val label = labelList.labels

//                .find { it.id == labelName } ?: error("Label `$labelName` is unknown.")




        gmailService.processFroms(activity, user, label.get(labelid), from) {

            // senders += it

        }


    }

    fun AllInbox(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            awaitAll(
                { Allmails(activity, 2) },
                { Allmails(activity, 3) },
                { Allmails(activity, 4) }

            )
        }
    }

    tailrec fun Gmail.SmartInbox(accontname: String) {
        var list = searchSeenMessages(accontname, users())
//        Log.d(TAG, "SmartInbox: "+ list!!.size)
    }

    suspend fun awaitAll(vararg blocks: suspend () -> Unit) = coroutineScope {
        blocks.forEach {
            launch { it() }
        }
    }

    private tailrec fun Gmail.processMessages(
        user: String,
        label: Label,
        nextPageToken: String? = null,
        process: (Message) -> Unit
    ) {


        val messages = users().messages().list(user).apply {

            labelIds = listOf(label.id)
            pageToken = nextPageToken
            includeSpamTrash = false
        }.execute()

        try {


            messages.messages.forEach { message ->

                process(message)

            }

            if (messages.nextPageToken != null) {

                processMessages(user, label, messages.nextPageToken, process)
            }
        } catch (e: Exception) {
            Log.d(TAG, "processMessages: " + e.message)
        }
    }


    private fun Allmails(activity: Activity, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            var myPref =
                MyApplication.instance.context?.getSharedPreferences(
                    "APP_SHARED_PREF",
                    Context.MODE_PRIVATE
                );
            var username = myPref?.getString("username", "");
            var acc = GoogleSignIn.getLastSignedInAccount(activity)

            deletedata()
            getCallData()

            fetchMails(
                activity,
                username!!,
                username!!,
                acc!!, id, true
            )
        }
    }

    suspend fun insertAside(email: Email) {
        foodItemDao.insert(email)
    }

    fun getAsideList(): LiveData<List<Email>> {
        return foodItemDao.getAsideItems()

    }



}