//# Created by Nirali Pandya
////* *Main Repository to make Gmail Api call
////*Authenticate with Token
////* First Made Message List call @ExtractMails() function is starting Entery
////* than iterate json response data
////* @senders.forEach() method For Getting All mails.


package com.aiemail.superemail.Repository


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Source
import com.aiemail.superemail.Rooms.AllEmailDao
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.LinkedHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AllfetchRepository(private val foodItemDao: AllEmailDao, var page: Int = 1) {

    val TAG = javaClass.simpleName
    var arrayList: ArrayList<AllMails> = arrayListOf()
    public var Allmail = MutableLiveData<AllMails>()
    public var allsection = MutableLiveData<AllMails>()
    var datamap: LinkedHashMap<String, ArrayList<AllMails>> =
        LinkedHashMap<String, ArrayList<AllMails>>()
    var first_data: Boolean = false
    var first_data1: Boolean = false
    private val MAX_FETCH_THREADS = Runtime.getRuntime().availableProcessors()
    val executors = Executors.newFixedThreadPool(MAX_FETCH_THREADS)
    private val TIMEOUT = 1500

    private val _todoListFlow: MutableLiveData<HashMap<String, ArrayList<AllMails>>> =
        MutableLiveData()
    val todoListFlow: LiveData<HashMap<String, ArrayList<AllMails>>> get() = _todoListFlow

    private val _messageList: MutableLiveData<MutableList<FullMessage>> = MutableLiveData()
    val fullmessage: LiveData<MutableList<FullMessage>> get() = _messageList

    private val _emaillist: MutableLiveData<MutableList<AllMails>> = MutableLiveData()
    val emaillist: LiveData<MutableList<AllMails>> get() = _emaillist
    var key: String = ""


    fun getCallData() {
        // Sort the messages based on the internal date in descending order


        GlobalScope.launch {
            withContext(Dispatchers.Main) {

                _todoListFlow.value = datamap

            }

        }
    }

    fun adddata(articles: AllMails) {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemDao.insert(articles)
        }
    }

    fun deletedata() {
        foodItemDao.DeleteMails()


    }


    fun getAllMails(): MutableLiveData<Map<Int, List<AllMails>>> {

        var map: HashMap<Int, List<AllMails>> = hashMapOf()
        Log.e(TAG, "getAllMails: " + foodItemDao.getAllMail())
        map.put(0, foodItemDao.getAllMail().value!!)
        val liveData: MutableLiveData<Map<Int, List<AllMails>>> = MutableLiveData()
        liveData.value = map
        return liveData
    }

    private fun searchSeenMessages(
        USER_EMAIL: String,
        service: Gmail.Users
    ): MutableList<Message?>? {
        val query = "is:seen"
        val response = service.messages().list(USER_EMAIL).setQ(query).execute()
        Log.d(TAG, "searchSeenMessages: " + response)
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
        val recipientEmail = "swainfo.nirali@gmail.com"
        GlobalScope.launch {
            ExtractMails(activity, email, account.email!!, id, from)


        }
    }

    fun Gmail.Smartbox(accontname: String) {
        runBlocking {
            SmartInbox(accontname)
        }
    }

    private fun Gmail.processFroms(
        activity: Activity,
        fullMessage: Message,

        id: Int,
        user: String,
        label: String, from: Boolean,

        ) {
        runBlocking(dispatcher) {

            processMessages(id, user, fullMessage) { m ->
                launch {
                    Log.d(TAG, "ExtractMails: " + m.snippet)
                    Log.d(TAG, "labellist: " + label)
//                        arrayList = arrayListOf()
                    try {

                        var article4: AllMails = AllMails()
//                            var message1: Message =
//                                users().messages().get(user, m.id).setFormat("full")
//                                    .execute()
//                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->
//
//
//                                process(from.value.parseAddress())
//                            }


                        val messageId = m.id

//                            val fullMessage: Message =
//                                users().messages().get("me", messageId)
//                                    .setFormat("full").execute()
                        Log.d(TAG, "fetchAndProcess: " + label)
                        val internalDateMillis = m.internalDate
                        val date = Date(internalDateMillis)
                        val sdf = SimpleDateFormat("dd-MMM", Locale.getDefault())

                        article4 = AllMails()
                        article4.type = label


                        var article: AllMails = AllMails()
                        article.title = m.snippet
                        article.msgid = messageId
                        article.date = sdf.format(date)
                        article.type = label
                        var title = m.payload.headers.stream()
                            .filter { header: MessagePartHeader -> header.name == "Subject" }
                            .findFirst()
                            .map { obj: MessagePartHeader ->

                                //article4.content = obj.value
                                article.author = obj.value
                                Log.d(TAG, "fetchAndProcess: " + article.author)


                            }

                        var body: String? = ""

                        if (m.payload.mimeType == "text/html") {
                            // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                            body = m.payload.body.data
                            val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(body)
                            val decodedString = String(decodedBytes)
                            article.description = decodedString
                            Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                        } else if (m.payload.mimeType == "multipart/alternative") {

                            val parts = m.payload.parts
                            for (part in parts) {
                                //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

                                if (part.mimeType == "text/x-amp-html") {
                                    var body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.description = body_attach
                                    Log.d(TAG, "fetchAndProcess: 2" + body_attach)

                                }
                                if (part.mimeType == "text/html") {
                                    var body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.description = body_attach
                                    //Log.d(TAG, "fetchAndProcess:3 " + body_attach)


                                }


                                if (part.mimeType == "text/plain") {
                                    var source = Source()
                                    source.id = "2"
                                    article.type = label
                                    var body_attach: String = ""
                                    body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.content = body_attach

                                }
                                // arrayList.add(article)


                            }
                        }
//                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                        if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                            if (article.title.equals("") && !article.content.equals(""))
                                article.title = article.content
                            else if (article.title.equals("") && !article.author.equals(""))
                                article.title = article.author

                            arrayList.add(article)
                            key = article4.type
                            if (!article.description.isNullOrBlank() || !article.author.isNullOrBlank()) {
                                Log.d(TAG, "datacallingsev: " + key + ">>" + arrayList.size)
                                datamap.put(article4.type, arrayList)
                            }


                            //   foodItemDao.insertKey(arrayList)
                            // foodItemDao.insert(article)

                            // foodItemDao.insertAll(article)
                            var a: AllMails = AllMails()
                            a.type = "Show more"
                            //arrayList.add(a)
                            Log.d(TAG, "Size i want" + datamap.keys + ">>>")

                            //datamap.put(a.type, arrayList)
                            //  foodItemDao.insertKey(arrayList)
                            // foodItemDao.insert(a)


                        }


                        // }


                    } catch (e: SocketTimeoutException) {

                        // Process eventual failures.
                        // Restart request on socket timeout.
                        e.printStackTrace()

                    } catch (e: Exception) {
                        // Process eventual failures.
                        e.printStackTrace()
                    }

                    //  }

//                    fun fetchAndProcess1() {
////                        arrayList = arrayListOf()
//                        try {
//
//                            var article4: AllMails = AllMails()
////                            var message1: Message =
////                                users().messages().get(user, m.id).setFormat("full")
////                                    .execute()
////                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->
////
////
////                                process(from.value.parseAddress())
////                            }
//
//
//                            val messageId = m.id
//
//                            val internalDateMillis = fullMessage.internalDate
//                            val date = Date(internalDateMillis)
//                            val sdf = SimpleDateFormat("dd-MMM", Locale.getDefault())
//
//                            article4 = AllMails()
//                            article4.type = label
//
//
//                            var article: AllMails = AllMails()
//                            article.title = fullMessage.snippet
//                            article.date = sdf.format(date)
//                            article.type = label
//                            var title = fullMessage.payload.headers.stream()
//                                .filter { header: MessagePartHeader -> header.name == "Subject" }
//                                .findFirst()
//                                .map { obj: MessagePartHeader ->
//
//                                    //article4.content = obj.value
//                                    article.author = obj.value
//                                    Log.d(TAG, "fetchAndProcess: " + article.author)
//
//
//                                }
//
//                            var body: String? = ""
//
//                            if (fullMessage.payload.mimeType == "text/html") {
//                                // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
//                                body = fullMessage.payload.body.data
//                                val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(body)
//                                val decodedString = String(decodedBytes)
//                                article.description = decodedString
//                                Log.d(TAG, "fetchAndProcess:1 " + decodedString)
//
//                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {
//
//                                val parts = fullMessage.payload.parts
//                                for (part in parts) {
//                                    //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))
//
//                                    if (part.mimeType == "text/x-amp-html") {
//                                        var body_attach =
//                                            convertRawDataToPlainText(part.body.data)!!
//                                        article.description = body_attach
//                                        Log.d(TAG, "fetchAndProcess: 2" + body_attach)
//
//                                    }
//                                    if (part.mimeType == "text/html") {
//                                        var body_attach =
//                                            convertRawDataToPlainText(part.body.data)!!
//                                        article.description = body_attach
//                                        //Log.d(TAG, "fetchAndProcess:3 " + body_attach)
//
//
//                                    }
//
//
//                                    if (part.mimeType == "text/plain") {
//                                        var source = Source()
//                                        source.id = "2"
//                                        article.type = label
//                                        var body_attach: String = ""
//                                        body_attach =
//                                            convertRawDataToPlainText(part.body.data)!!
//                                        article.content = body_attach
//
//                                    }
//                                    // arrayList.add(article)
//
//
//                                }
//                            }
////                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
//
//                            if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {
//
//                                if (article.title.equals("") && !article.content.equals(""))
//                                    article.title = article.content
//                                else if (article.title.equals("") && !article.author.equals(""))
//                                    article.title = article.author
//
//                                arrayList.add(article)
//                                key = article4.type
//                                if (!article.description.isNullOrBlank() || !article.author.isNullOrBlank())
//                                    datamap.put(article4.type, arrayList)
//
//
//                                //   foodItemDao.insertKey(arrayList)
//                                // foodItemDao.insert(article)
//
//                                // foodItemDao.insertAll(article)
//                                var a: AllMails = AllMails()
//                                a.type = "Show more"
//                                //arrayList.add(a)
//                                Log.d(TAG, "Size i want" + datamap.keys + ">>>")
//
//                                //datamap.put(a.type, arrayList)
//                                //  foodItemDao.insertKey(arrayList)
//                                // foodItemDao.insert(a)
//
//
//                            }
//
//
//                            // }
//
//
//                        } catch (e: SocketTimeoutException) {
//
//                            // Process eventual failures.
//                            // Restart request on socket timeout.
//                            e.printStackTrace()
//                            fetchAndProcess()
//                        } catch (e: Exception) {
//                            // Process eventual failures.
//                            e.printStackTrace()
//                        }
//
//                    }

//
//                    if (arrayList.size < 6 || !first_data) {
//                        first_data = true
//                        getCallData()
//                        fetchAndProcess()
//                    } else {
//                        if (!first_data1) {
//                            first_data1 = true
//                            fetchAndProcess1()
//                        }
//                    }
//                    if (arrayList.size > 6 && first_data && first_data1) {
//                        arrayList = arrayListOf()
//                    }
//
//
//
                    Log.d(TAG, "processFroms: " + arrayList.size + ">>" + id)
                    // fetchAndProcess()
                    getCallData()

                }
            }
        }


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
        arrayList = arrayListOf()
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

        val labelList = gmailService.users().labels().list(user).execute()
        val label = labelList.labels


// Assuming you have already initialized the Gmail service client 'gmailService'

// Define the list of categories
        val categories = arrayOf("promotions")

// Perform the API calls within a coroutine scope
        runBlocking {
            // Iterate over each category
            for (category in categories) {

//                launch(Dispatchers.IO) {
                    // Retrieve the list of messages for the specific category
                    val response = gmailService.users().messages().list(user)
                        .setMaxResults(5)
                        .setQ("category:$category")
                        .execute()
                    val messages = response.messages
                    var messageCount = 0


                    if (messages != null && messages.size != 0) {
                        arrayList = arrayListOf()
                        for (message in messages) {

                            if (messageCount >= 3) {
                                break
                            }

                            val fullMessage =
                                gmailService.users().messages().get("me", message.id)
                                    .setFormat("full")
                                    .execute()

                            // Retrieve the body of each message
                            val senders = mutableSetOf<String>()

                            try {

                                var article4: AllMails = AllMails()
//                            var message1: Message =
//                                users().messages().get(user, m.id).setFormat("full")
//                                    .execute()
//                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->
//
//
//                                process(from.value.parseAddress())
//                            }


                                val messageId = fullMessage.id

//                            val fullMessage: Message =
//                                users().messages().get("me", messageId)
//                                    .setFormat("full").execute()
                                Log.d(TAG, "fetchAndProcess: " + label)
                                val internalDateMillis = fullMessage.internalDate
                                val date = Date(internalDateMillis)
                                val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())

                                article4 = AllMails()
                                article4.type = "NewsLetters"


                                var article: AllMails = AllMails()
                                article.title = fullMessage.snippet
                                article.date = Helpers.getDisplayableTime(internalDateMillis)!!
                                article.type = "NewsLetters"
                                var title = fullMessage.payload.headers.stream()
                                    .filter { header: MessagePartHeader -> header.name == "Subject" }
                                    .findFirst()
                                    .map { obj: MessagePartHeader ->

                                        //article4.content = obj.value
                                        article.author = obj.value
                                        Log.d(TAG, "fetchAndProcess: " + article.author)


                                    }
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
                                        break
                                    }
                                }

                                var body: String? = ""

                                if (fullMessage.payload.mimeType == "text/html") {
                                    // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                    body = fullMessage.payload.body.data
                                    val decodedBytes: ByteArray =
                                        Base64.getUrlDecoder().decode(body)
                                    val decodedString = String(decodedBytes)
                                    article.description = decodedString
                                    Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                                } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                    val parts = fullMessage.payload.parts
                                    for (part in parts) {
                                        //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

                                        if (part.mimeType == "text/x-amp-html") {
                                            var body_attach =
                                                convertRawDataToPlainText(part.body.data)!!
                                            article.description = body_attach
                                            Log.d(TAG, "fetchAndProcess: 2" + body_attach)

                                        }
                                        if (part.mimeType == "text/html") {
                                            var body_attach =
                                                convertRawDataToPlainText(part.body.data)!!
                                            article.description = body_attach
                                            //Log.d(TAG, "fetchAndProcess:3 " + body_attach)


                                        }


                                        if (part.mimeType == "text/plain") {
                                            var source = Source()
                                            source.id = "2"
                                            article.type = category
                                            var body_attach: String = ""
                                            body_attach =
                                                convertRawDataToPlainText(part.body.data)!!
                                            // Extract the text content from the full body
                                            val textContent = body_attach.replace(Regex("<.*?>"), "")
                                            article.content = textContent

                                        }
                                        // arrayList.add(article)


                                    }
                                }
//                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                                if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                                    if (article.title.equals("") && !article.content.equals(""))
                                        article.title = article.content
                                    else if (article.title.equals("") && !article.author.equals(
                                            ""
                                        )
                                    )
                                        article.title = article.author

                                    arrayList.add(article)
                                    messageCount++
                                    key = article4.type
                                    if (!article.description.isNullOrBlank() || !article.author.isNullOrBlank()) {


                                        datamap.put(article4.type, arrayList)
                                        Log.d(
                                            TAG,
                                            "ExtractMails: " + article4.type + ">>" + arrayList.get(
                                                arrayList.size - 1
                                            ).title
                                        )
//                                        arrayList= arrayListOf()


                                    }

                                    // val reversedMap = Constants.reverseOrderOfKeys(datamap)
//                                datamap = reversedMap
//                                // Print the reversed map
//                                for ((key, value) in reversedMap) {
//                                    println("Key: $key, Value: $value")
//                                    Log.d(TAG, "ExtractMails: "+ke)
//                                }


                                    //   foodItemDao.insertKey(arrayList)
                                    // foodItemDao.insert(article)

                                    // foodItemDao.insertAll(article)
                                    var a: AllMails = AllMails()
                                    a.type = "Show more"


                                }


                                // }


                            } catch (e: SocketTimeoutException) {

                                // Process eventual failures.
                                // Restart request on socket timeout.
                                e.printStackTrace()

                            } catch (e: Exception) {
                                // Process eventual failures.
                                e.printStackTrace()
                            }
                        }
                        Log.d(
                            TAG,
                            "Block of code:>>>>> " + datamap.keys.size + ">>" + arrayList.size
                        )
//                        if (datamap.keys.size > 2) {
                        //getCallData()
                        // }
                    }
                }

           // }
          //  delay(2000)


            arrayList = arrayListOf()
//            launch(Dispatchers.IO) {

                // Retrieve the list of messages for the specific category
                val response = gmailService.users().messages().list("me")
                    .setQ("is:read")
                    .execute()
                val messages = response.messages
                var messageCount = 0



                for (message in messages) {
                    if (messageCount >= 5) {
                        break
                    }

                    val fullMessage =
                        gmailService.users().messages().get("me", message.id).setFormat("full")
                            .execute()
                    Log.d(
                        TAG,
                        "ExtractMails: " + fullMessage.size + ">>" + fullMessage.snippet + ">>" + fullMessage.labelIds
                    )
                    // Retrieve the body of each message


                    try {

                        var article4: AllMails = AllMails()
//


                        val internalDateMillis = fullMessage.internalDate


                        article4 = AllMails()
                        article4.type = "Seen"


                        var article: AllMails = AllMails()
                        article.title = fullMessage.snippet
                        article.date = Helpers.getDisplayableTime(internalDateMillis)!!
                        article.type = "Seen"
                        var title = fullMessage.payload.headers.stream()
                            .filter { header: MessagePartHeader -> header.name == "Subject" }
                            .findFirst()
                            .map { obj: MessagePartHeader ->

                                //article4.content = obj.value
                                article.author = obj.value
                                Log.d(TAG, "fetchAndProcess: " + article.author)


                            }
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
                            Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                        } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                            val parts = fullMessage.payload.parts
                            for (part in parts) {
                                //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

                                if (part.mimeType == "text/x-amp-html") {
                                    var body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.description = body_attach
                                    Log.d(TAG, "fetchAndProcess: 2" + body_attach)

                                }
                                if (part.mimeType == "text/html") {
                                    var body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.description = body_attach
                                    //Log.d(TAG, "fetchAndProcess:3 " + body_attach)


                                }


                                if (part.mimeType == "text/plain") {
                                    var source = Source()
                                    source.id = "2"
                                    article.type = "seen"
                                    var body_attach: String = ""
                                    body_attach =
                                        convertRawDataToPlainText(part.body.data)!!
                                    article.content = body_attach

                                }
                                // arrayList.add(article)


                            }
                        }
//                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                        if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                            if (article.title.equals("") && !article.content.equals(""))
                                article.title = article.content
                            else if (article.title.equals("") && !article.author.equals(""))
                                article.title = article.author

                            arrayList.add(article)
                            messageCount++
                            key = article4.type
                            if (!article.description.isNullOrBlank() || !article.author.isNullOrBlank()) {
                                Log.d(
                                    TAG,
                                    "datacallingsev: " + datamap.keys + ">>" + arrayList.size
                                )
                                datamap.put(article4.type, arrayList)

                            }

                            // val reversedMap = Constants.reverseOrderOfKeys(datamap)
//                                datamap = reversedMap
//                                // Print the reversed map
//                                for ((key, value) in reversedMap) {
//                                    println("Key: $key, Value: $value")
//                                    Log.d(TAG, "ExtractMails: "+ke)
//                                }


                            //   foodItemDao.insertKey(arrayList)
                            // foodItemDao.insert(article)

                            // foodItemDao.insertAll(article)
                            var a: AllMails = AllMails()
                            a.type = "Show more"
                            //arrayList.add(a)


                            //datamap.put(a.type, arrayList)
                            //  foodItemDao.insertKey(arrayList)
                            // foodItemDao.insert(a)


                        }


                        // }


                    } catch (e: SocketTimeoutException) {

                        // Process eventual failures.
                        // Restart request on socket timeout.
                        e.printStackTrace()

                    } catch (e: Exception) {
                        // Process eventual failures.
                        e.printStackTrace()
                    }
                }
                Log.d(TAG, "Block of code: " + datamap.keys.size + ">>" + arrayList.size)
//                    if (datamap.keys.size > 3) {
//                   getCallData()
                // }
           // }
            // delay(2000)


            arrayList = arrayListOf()
            val categories = arrayOf("social")

// Perform the API calls within a coroutine scope
            runBlocking {
                // Iterate over each category
                for (category in categories) {

//                    launch(Dispatchers.IO) {
                        // Retrieve the list of messages for the specific category
                        val response = gmailService.users().messages().list(user)
                            .setMaxResults(5)
                            .setQ("category:$category")
                            .execute()
                        val messages = response.messages
                        var messageCount = 0


                        if (messages != null && messages.size != 0) {
                            arrayList = arrayListOf()
                            for (message in messages) {

                                if (messageCount >= 3) {
                                    break
                                }

                                val fullMessage =
                                    gmailService.users().messages().get("me", message.id)
                                        .setFormat("full")
                                        .execute()

                                // Retrieve the body of each message
                                val senders = mutableSetOf<String>()

                                try {

                                    var article4: AllMails = AllMails()
//                            var message1: Message =
//                                users().messages().get(user, m.id).setFormat("full")
//                                    .execute()
//                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->
//
//
//                                process(from.value.parseAddress())
//                            }


                                    val messageId = fullMessage.id

//                            val fullMessage: Message =
//                                users().messages().get("me", messageId)
//                                    .setFormat("full").execute()
                                    Log.d(TAG, "fetchAndProcess: " + label)
                                    val internalDateMillis = fullMessage.internalDate


                                    article4 = AllMails()
                                    article4.type = "Social"


                                    var article: AllMails = AllMails()
                                    article.title = fullMessage.snippet
                                    article.date = Helpers.getDisplayableTime(internalDateMillis)!!
                                    article.type = "Social"
                                    var title = fullMessage.payload.headers.stream()
                                        .filter { header: MessagePartHeader -> header.name == "Subject" }
                                        .findFirst()
                                        .map { obj: MessagePartHeader ->

                                            //article4.content = obj.value
                                            article.author = obj.value
                                            Log.d(TAG, "fetchAndProcess: " + article.author)


                                        }
                                    var headers = fullMessage.getPayload().getHeaders()
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
                                            article.sender = senderName
                                            article.senderEmail = senderEmail
                                            break
                                        }
                                    }

                                    var body: String? = ""

                                    if (fullMessage.payload.mimeType == "text/html") {
                                        // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                        body = fullMessage.payload.body.data
                                        val decodedBytes: ByteArray =
                                            Base64.getUrlDecoder().decode(body)
                                        val decodedString = String(decodedBytes)
                                        article.description = decodedString
                                        Log.d(TAG, "fetchAndProcess:1 " + decodedString)

                                    } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                        val parts = fullMessage.payload.parts
                                        for (part in parts) {
                                            //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

                                            if (part.mimeType == "text/x-amp-html") {
                                                var body_attach =
                                                    convertRawDataToPlainText(part.body.data)!!
                                                article.description = body_attach
                                                Log.d(TAG, "fetchAndProcess: 2" + body_attach)

                                            }
                                            if (part.mimeType == "text/html") {
                                                var body_attach =
                                                    convertRawDataToPlainText(part.body.data)!!
                                                article.description = body_attach
                                                //Log.d(TAG, "fetchAndProcess:3 " + body_attach)


                                            }


                                            if (part.mimeType == "text/plain") {
                                                var source = Source()
                                                source.id = "2"
                                                article.type = category
                                                var body_attach: String = ""
                                                body_attach =
                                                    convertRawDataToPlainText(part.body.data)!!
                                                article.content = body_attach

                                            }
                                            // arrayList.add(article)


                                        }
                                    }
//                            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {

                                    if (!article.title.isNullOrBlank() || !article.content.isNullOrBlank() || !article.author.isNullOrBlank()) {

                                        if (article.title.equals("") && !article.content.equals(
                                                ""
                                            )
                                        )
                                            article.title = article.content
                                        else if (article.title.equals("") && !article.author.equals(
                                                ""
                                            )
                                        )
                                            article.title = article.author

                                        arrayList.add(article)
                                        messageCount++
                                        key = article4.type
                                        if (!article.description.isNullOrBlank() || !article.author.isNullOrBlank()) {


                                            datamap.put(article4.type, arrayList)
                                            Log.d(
                                                TAG,
                                                "ExtractMails:>Sociall " + article4.type + ">>" + arrayList.get(
                                                    arrayList.size - 1
                                                ).title
                                            )
//                                        arrayList= arrayListOf()


                                        }

                                        // val reversedMap = Constants.reverseOrderOfKeys(datamap)
//                                datamap = reversedMap
//                                // Print the reversed map
//                                for ((key, value) in reversedMap) {
//                                    println("Key: $key, Value: $value")
//                                    Log.d(TAG, "ExtractMails: "+ke)
//                                }


                                        //   foodItemDao.insertKey(arrayList)
                                        // foodItemDao.insert(article)

                                        // foodItemDao.insertAll(article)
                                        var a: AllMails = AllMails()
                                        a.type = "Show more"
                                        //arrayList.add(a)


                                        //datamap.put(a.type, arrayList)
                                        //  foodItemDao.insertKey(arrayList)
                                        // foodItemDao.insert(a)


                                    }


                                    // }


                                } catch (e: SocketTimeoutException) {

                                    // Process eventual failures.
                                    // Restart request on socket timeout.
                                    e.printStackTrace()

                                } catch (e: Exception) {
                                    // Process eventual failures.
                                    e.printStackTrace()
                                }
                            }
                            //launch {
                                Log.d(TAG, "Block of code: " + datamap.keys.size)
                                if (datamap.keys.size != 0) {
                                    getCallData()

                                }

                            //}
//
                        } else {
                            Log.d(TAG, "Block of code: " + datamap.keys + ">>" + arrayList.size)
                            if (datamap.keys.size != 0) {
                                getCallData()
                            }
                        }
                    //}

                }

            }
        }


    }


    fun AllInbox(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                synchronousOperation1(activity)
                // synchronousOperation2(activity)


            }
        }
    }

    suspend fun synchronousOperation1(activity: Activity): String =
        suspendCoroutine { continuation ->
            Allmails(activity, 2) }

    suspend fun synchronousOperation2(activity: Activity): Int = suspendCoroutine { continuation ->
        Allmails(activity, 9)
        Thread.sleep(2000)
        continuation.resume(42)
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
        id: Int,
        user: String,
        message: Message,
        nextPageToken: String? = null,
        process: (Message) -> Unit
    ) {


        try {


            message.forEach { message1 ->

                process(message)
                Log.d(TAG, "processMessages:mess " + message)
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

            // deletedata()
            //getCallData()

            fetchMails(
                activity,
                username!!,
                username!!,
                acc!!, id, true
            )
        }
    }

}