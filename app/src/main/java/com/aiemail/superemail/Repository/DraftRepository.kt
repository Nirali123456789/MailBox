//# Created by Nirali Pandya
////* *Main Repository to make Gmail Api call
////*Authenticate with Token
////* First Made Message List call @ExtractMails() function is starting Entery
////* than iterate json response data
////* @senders.forEach() method For Getting All mails.


package com.aiemail.superemail.Repository


import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Draft
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Models.Sent
import com.aiemail.superemail.Models.Source
import com.aiemail.superemail.Rooms.AllEmailDao
import com.aiemail.superemail.Rooms.DraftDao
import com.aiemail.superemail.Rooms.PinDao
import com.aiemail.superemail.Rooms.SentDao
import com.aiemail.superemail.utilis.Helpers
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.ListMessagesResponse
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart
import com.google.api.services.gmail.model.MessagePartHeader
import com.ibrajix.nftapp.utilis.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.LinkedHashMap
import kotlin.coroutines.CoroutineContext


//# Created by Nirali Pandya
////* *Main Repository to make Gmail Api call
////*Authenticate with Token
////* First Made Message List call @ExtractMails() function is starting Entery
////* than iterate json response data
////* @senders.forEach() method For Getting All mails.


class DraftRepository(private val draftDao: DraftDao) {
    private val TIMEOUT = 1500
    private val MAX_FETCH_THREADS = Runtime.getRuntime().availableProcessors()
    val executors = Executors.newFixedThreadPool(MAX_FETCH_THREADS)
    var pinListData: LiveData<ArrayList<Draft>> = MutableLiveData()
    var arrayList: ArrayList<Draft> = arrayListOf()
    private val _pinList: MutableLiveData<Draft> = MutableLiveData()
    val pin_data: MutableLiveData<Draft> get() = _pinList

    // Create a coroutine scope
    val scope = CoroutineScope(Dispatchers.Main)


    fun fetchMails(
        activity: Activity,
        label: String,
        email: String,
        account: GoogleSignInAccount,
        id: Int,
        from: Boolean = false
    ) {
        val recipientEmail = "swainfo.nirali@gmail.com"
        GlobalScope.launch {
            ExtractMails(activity, email, account.email!!, id, from)


        }
    }


    fun convertRawDataToPlainText(binaryData: String): String? {
        val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(binaryData)

        // Convert the decoded bytes to a string
        val decodedString = String(decodedBytes)


        return decodedString
    }

    fun ExtractMails(
        activity: Activity,
        ac: String,
        accontname: String,
        labelid: Int,
        from: Boolean
    ) {

        // Build a new authorized API client service.
        val httpTransport =
            NetHttpTransport()
                .createRequestFactory { request ->
                    request.connectTimeout = TIMEOUT
                    request.readTimeout = TIMEOUT
                }.transport
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
        gmailService.processFroms(user, user, from) {

            // senders += it

        }


    }

    private fun Gmail.processFroms(
        user: String,
        label: String, from: Boolean,
        process: (String) -> Unit
    ) {
        runBlocking(dispatcher) {
            processMessages(user) { m ->
                launch {
                    fun fetchAndProcess() {
                        try {


                            var message1: Message
                            var article4: Draft = Draft()
                            message1 =
                                users().messages().get(user, m.id)
                                    .setFormat("full")
                                    .execute()
                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->


                                process(from.value.parseAddress())
                            }


                            val messageId = m.id


                            val messageRequest = users().messages().get(user, messageId)
                            messageRequest.format = "full" // Retrieve the full message body
                            val fullMessage = messageRequest.execute()
                            val internalDateMillis = fullMessage.internalDate
                            val date = Date(internalDateMillis)
                            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())

                            article4 = Draft()

                            article4.date = Helpers.getDisplayableTime(internalDateMillis)

                            article4.type = 0

                            if (!from) {
                                if (!arrayList.contains(article4)) {
                                    arrayList.add(article4)
                                    CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                                       // draftDao.insert(article4)

                                    }
                                }
                            }


//

                            var article: Draft = Draft()
                            article.title = fullMessage.snippet
                            article.msgid = message1.id
                            article.date = Helpers.getDisplayableTime(internalDateMillis)
                            article.type = 1
                            article.thread_id = message1.threadId
                            var title = fullMessage.payload.headers.stream()
                                .filter { header: MessagePartHeader -> header.name == "Subject" }
                                .findFirst()
                                .map { obj: MessagePartHeader ->

                                    //article4.content = obj.value
                                    article.author = obj.value


                                }
                            val senderId: String = message1.getPayload().getHeaders()
                                .stream()
                                .filter { header ->
                                    header.getName().equals("From")
                                }
                                .findFirst()
                                .orElse(MessagePartHeader())
                                .getValue()

                            var headers = message1.getPayload().getHeaders()
                            for (header in headers) {
                                if (header.name == "From") {
                                    // Extract the sender's email address and name from the "From" header
                                    var senderEmail = header.value.substring(
                                        header.value.lastIndexOf("<") + 1,
                                        header.value.lastIndexOf(">")
                                    )
                                    var senderName =
                                        header.value.substring(0, header.value.lastIndexOf("<"))
                                            .trim { it <= ' ' }
                                    article.sender = senderName
                                    article.senderEmail = senderEmail
                                    break
                                }
                            }


                            var body: String? = ""

                            if (fullMessage.payload.mimeType == "text/html") {
                                // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                body = fullMessage.payload.body.data
                                val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(body)
                                val decodedString = String(decodedBytes)
                                article.description = decodedString
                                // Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                val parts = fullMessage.payload.parts
                                for (part in parts) {
                                    Log.d("TAG", "fetchAndProcess: " + part.mimeType)

                                    if (part.mimeType == "text/x-amp-html") {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.description = body_attach


                                    }
                                    if (part.mimeType == "text/html") {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.description = body_attach


                                    }


                                    if (part.mimeType == "text/plain") {
                                        var source = Source()
                                        source.id = "2"
                                        article.type = 1
                                        var body_attach: String = ""
                                        body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.content = body_attach


                                    }
                                    arrayList.add(article)


                                }
                            }
                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                                if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                                    if (article.title.equals("") && !article.content.equals(""))
                                        article.title = article.content
                                    else if (article.title.equals("") && !article.author.equals(""))
                                        article.title = article.author


                                    article.isInbox = true
                                    draftDao.insert(article)
//                                    if (!from) {
//                                        // foodItemDao.insertAll(article)
//                                        var a: Email = Email()
//                                        a.type = 3
//                                        foodItemDao.insert(a)
//                                    }


                                }


                            }


                        } catch (e: SocketTimeoutException) {

                            // Process eventual failures.
                            // Restart request on socket timeout.
                            e.printStackTrace()
                            fetchAndProcess()
                        } catch (e: Exception) {
                            // Process eventual failures.
                            e.printStackTrace()
                        }

                    }
                    getCallData()
                    fetchAndProcess()
                }
            }
        }
    }

    private tailrec fun Gmail.processMessages(
        user: String,
        process: (Message) -> Unit
    ) {
        val messages = users().drafts().list(user).apply {


//            pageToken = nextPageToken

        }.execute()
        messages.drafts.forEach { message ->

            process(message.message)

        }

        if (messages.nextPageToken != null) {
            processMessages(user, process)
        }

        val drafts = messages.drafts
        if (drafts.isNullOrEmpty()) {
            println("No drafts found.")
        } else {
            for (draft in drafts) {
                val draftId = draft.id
                val draft = users().drafts().get(user, draftId)
                    .apply {

                        val messageId = draft.message.id

                        try {

                            // Retrieve the full message
                            val messageRequest = users().messages().get(user, messageId)
                            messageRequest.format = "full" // Retrieve the full message body
                            val fullMessage = messageRequest.execute()
                            Log.d("TAG", "processMessages: " + fullMessage)
                            var article4: Draft = Draft()

                            fullMessage.payload.headers.find { it.name == "Subject" }?.let { from ->
                                Log.d("TAG", "processMessages: " + from.value)

                            }
                            Log.d("TAG", "processMessages: " + fullMessage.payload.body.data)


                            val messageId = fullMessage.id

//                            val fullMessage: Message =
//                                users().messages().get("me", messageId)
//                                    .setFormat("full").execute()
                            val internalDateMillis = fullMessage.internalDate
                            val date = Date(internalDateMillis)
                            val sdf = SimpleDateFormat("dd-MMM", Locale.getDefault())

                            article4 = Draft()

                            article4.date = Helpers.getDisplayableTime(internalDateMillis)

                            article4.type = 0


                            if (!arrayList.contains(article4)) {
                                arrayList.add(article4)
                                CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                                    draftDao.insert(article4)

                                }
                            }


//

                            var article: Draft = Draft()
                            article.title = fullMessage.snippet
                            article.msgid = fullMessage.id
                            article.date = sdf.format(date)
                            article.type = 1
                            // article.thread_id = message1.threadId
                            var title = fullMessage.payload.headers.stream()
                                .filter { header: MessagePartHeader -> header.name == "Subject" }
                                .findFirst()
                                .map { obj: MessagePartHeader ->

                                    //article4.content = obj.value
                                    article.author = obj.value


                                }
                            val senderId: String = fullMessage.getPayload().getHeaders()
                                .stream()
                                .filter { header ->
                                    header.getName().equals("From")
                                }
                                .findFirst()
                                .orElse(MessagePartHeader())
                                .getValue()

                            var headers = fullMessage.getPayload().getHeaders()
                            for (header in headers) {
                                if (header.name == "From") {
                                    // Extract the sender's email address and name from the "From" header
                                    var senderEmail = header.value.substring(
                                        header.value.lastIndexOf("<") + 1,
                                        header.value.lastIndexOf(">")
                                    )
                                    var senderName =
                                        header.value.substring(0, header.value.lastIndexOf("<"))
                                            .trim { it <= ' ' }
                                    article.sender = senderName
                                    article.senderEmail = senderEmail
                                    Log.d("TAG", "processMessages: " + article)
                                    break
                                }
                            }


                            var body: String? = ""

                            if (fullMessage.payload.mimeType == "text/html") {
                                // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                body = fullMessage.payload.body.data
                                val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(body)
                                val decodedString = String(decodedBytes)
                                article.description = decodedString
                                // Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                val parts = fullMessage.payload.parts
                                for (part in parts) {
                                    Log.d("TAG", "fetchAndProcess: " + part.mimeType)

                                    if (part.mimeType == "text/x-amp-html") {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.description = body_attach


                                    }
                                    if (part.mimeType == "text/html") {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.description = body_attach
                                        Log.d("TAG", "fetchAndProcess: " + part.body.data)


                                    }


                                    if (part.mimeType == "text/plain") {
                                        var source = Source()
                                        source.id = "2"
                                        article.type = 1
                                        var body_attach: String = ""
                                        body_attach =
                                            convertRawDataToPlainText(part.body.data)!!
                                        article.content = body_attach
                                        Log.d("TAG", "fetchAndProcess:>plain " + part.body.data)


                                    }
                                    arrayList.add(article)


                                }
                            }
                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                                if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                                    if (article.title.equals("") && !article.content.equals(""))
                                        article.title = article.content
                                    else if (article.title.equals("") && !article.author.equals(""))
                                        article.title = article.author


                                    Log.d("TAG", "newidisd .d.c v: " + "?>>>" + article)
                                    draftDao.insert(article)
//                                    if (!from) {
//                                        // foodItemDao.insertAll(article)
//                                        var a: Email = Email()
//                                        a.type = 3
//                                        foodItemDao.insert(a)
//                                    }


                                }


                            }




                            if (messages.nextPageToken != null) {
                                processMessages(user, process)
                            }
                        } catch (e: Exception) {
                            Log.d("TAG", "processMessages: " + e.message)
                        }
                    }.execute()

                // Retrieve the message ID from the draft


            }
        }
    }


    val dispatcher = object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            executors.execute(block)
        }
    }


    fun getCallData() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                //  _pinList.value = pinList

            }

        }
    }

    fun adddata(pin: Draft) {
        scope.launch {
            draftDao.insert(pin)
        }
    }

    fun deletedata() {
        scope.launch {
//            sentDao.de()
        }
    }

    private fun String.parseAddress(): String {
        return if (contains("<")) {
            substringAfter("<").substringBefore(">")
        } else {
            this
        }
    }

    fun getDraftList(): LiveData<List<Draft>> {
        return draftDao.getAllMail()
    }



}