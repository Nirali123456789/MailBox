package com.aiemail.superemail.feature.Repository

import android.R.id.message
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Html
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aiemail.superemail.feature.Models.Article
import com.aiemail.superemail.feature.Models.FullMessage
import com.aiemail.superemail.feature.Models.Source
import com.aiemail.superemail.feature.Rooms.CategoryDao
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart
import com.google.api.services.gmail.model.MessagePartHeader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.coroutines.CoroutineContext



class fetchRepository(private val foodItemDao: CategoryDao, var page: Int = 1) {

    val TAG = javaClass.simpleName
    var arrayList: ArrayList<Article> = arrayListOf()
    var messagelist: ArrayList<FullMessage> = arrayListOf()
    private val MAX_FETCH_THREADS = Runtime.getRuntime().availableProcessors()
    val executors = Executors.newFixedThreadPool(MAX_FETCH_THREADS)
    private val TIMEOUT = 1500

    private val _todoListFlow: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val todoListFlow: LiveData<MutableList<Article>> get() = _todoListFlow

    private val _messageList: MutableLiveData<MutableList<FullMessage>> = MutableLiveData()
    val fullmessage: LiveData<MutableList<FullMessage>> get() = _messageList

    private val _emaillist: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val emaillist: LiveData<MutableList<Article>> get() = _emaillist


    fun getCallData() {
        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                _todoListFlow.value = arrayList

            }

        }
    }

    fun adddata() {


       // foodItemDao.insertAll(arrayList)


    }

    fun getEmails(): LiveData<List<Article>> {
        return foodItemDao.getAllMail()
    }





    fun fetchMails(activity: Activity, label: String, email: String, account: GoogleSignInAccount,id:Int) {
        GlobalScope.launch {
            extract(activity, email, account.email!!,id)
        }

    }



    private fun Gmail.processFroms(
        user: String,
        label: Label,
        process: (String) -> Unit
    ) {
        runBlocking(dispatcher) {
//            ExtractLablesData
            processMessages(user, label) { m ->
                launch {
                    fun fetchAndProcess() {

                        try {

                            var message1: Message
                            var article4: Article = Article()
                            message1 =
                                users().messages().get(user, m.id)
                                    .execute()
                            message1.payload.headers.find { it.name == "Subject" }?.let { from ->


                                process(from.value.parseAddress())
                            }


                            val messageId = m.id

                            val fullMessage: Message =
                                users().messages().get("me", messageId)
                                    .setFormat("full").execute()
                            val internalDateMillis = fullMessage.internalDate
                            val date = Date(internalDateMillis)
                            val sdf = SimpleDateFormat("dd-MMM", Locale.getDefault())
                            article4.date=sdf.format(date)
                            article4.title=fullMessage.snippet
                            Log.d(TAG, "fetchAndProcess: "+fullMessage.snippet+">>>snipet")

                            Log.d(TAG, "mimetype: " + "")








//


                            var title = fullMessage.payload.headers.stream()
                                .filter { header: MessagePartHeader -> header.name == "Subject" }
                                .findFirst()
                                .map { obj: MessagePartHeader ->
                                    Log.d(TAG, "fetchAndProcess: "+obj)
                                    //article4.content = obj.value
                                    article4.author=obj.value


                                }



//

//                            val subject = fullMessage.payload.headers.stream()
//
//                                .filter { header: MessagePartHeader -> header.name == "From" }
//                                .findFirst()
//                                .map { obj: MessagePartHeader ->
//                                    Log.d(TAG, "fetchAndProcess: "+obj)
//                                    article4.title = obj.value
//                                    obj.value
//
//                                }
//                                .orElse("")

//                            val parts1 = fullMessage.payload.parts
//                            for (part in parts1) {
//                                if (part.body.attachmentId != null) {
//                                    val attachmentId = part.body.attachmentId
//                                    var attachment =
//                                        users().messages().attachments()
//                                            .get("me", messageId, attachmentId).execute()
//                                    val data = attachment.data
//                                    Log.d(TAG, "Messagess: " + attachment)
//                                    val bytes = com.google.api.client.util.Base64.decodeBase64(data)
//                                    Log.d(TAG, "Messagess: " + bytes)
//                                    article4.dataByteArray=bytes
////                                    val imageUrl = convertByteArrayToBitmap(bytes)
////                                    imageUrls.add(imageUrl!!)
//
//                                    var imagedata : ByteArray = byteArrayOf()
//                                    for (i in bytes.indices) {
//                                        imagedata[i] = bytes.get(i)
//                                        Log.d(TAG, "Messagess: " + imagedata[i])
//                                    }
////                                    article4.setimagedata(imagedata)
//
//                                }
//                            }


                            var body: String? = ""

                            if (fullMessage.payload.mimeType == "text/html") {
                               // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                body = fullMessage.payload.body.data
//                                var body_attach =
//                                    convertRawDataToPlainText(part.body.data)!!







                                val decodedBytes: ByteArray =Base64.getUrlDecoder().decode(body)

                                // Convert the decoded bytes to a string
                                val decodedString = String(decodedBytes)
                                article4.description = decodedString


                              //  Log.d(TAG, "fetchAndProcess: minee" + decodedString)




                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                val parts = fullMessage.payload.parts
                                for (part in parts) {
                                    Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

                                    if (part.mimeType=="text/x-amp-html")
                                    {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!


                                        article4.description = body_attach

                                    }
                                      if (part.mimeType == "text/html") {
                                        var body_attach =
                                            convertRawDataToPlainText(part.body.data)!!


                                        article4.description = body_attach


                                    }


                                     if (part.mimeType == "text/plain") {


                                        var source = Source()
                                        source.id = "2"
                                        article4.type = 1
                                        var body_attach: String = ""

                                            body_attach =
                                                convertRawDataToPlainText(part.body.data)!!


                                            article4.content = body_attach




//
//


















                                    }
                                    arrayList.add(article4)



                                }
                            }


                        } catch (e: SocketTimeoutException) {
                            Log.d(TAG, "fetchAndProcess: "+e.message)
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


                    if (label.equals("Inbox")) {

//                processMessages(user, label) { m ->
//                    launch {
//                        fun fetchAndProcess() {
//                            try {
//                                var message1: Message
//
//                                message1 =
//                                    users().messages().get(user, m.id).apply { format = "METADATA" }
//                                        .execute()
//                                message1.payload.headers.find { it.name == "From" }?.let { from ->
//                                    process(from.value.parseAddress())
//                                }
//
//                                var article4: Article = Article()
//                                val messageId = m.id
//                                val fullMessage: Message =
//                                    users().messages().get(user, messageId)
//                                        .setFormat("full").execute()
//
//                                // Retrieve email header information
//                                val subject = fullMessage.payload.headers.stream()
//
////                                .filter { header: MessagePartHeader -> header.name == "Subject" }
//                                    .findFirst()
//                                    .map { obj: MessagePartHeader ->
//                                        Log.d(
//                                            TAG,
//                                            "fetchAndProcess: " + obj.name + "??" + obj.value
//                                        )
//                                        obj.value
//
//                                    }
//                                    .orElse("")
//
//                                // Retrieve email body content
//                                var body: String? = ""
//                                if (fullMessage.payload.mimeType == "text/plain") {
//                                    body = fullMessage.payload.body.data
//                                } else if (fullMessage.payload.mimeType == "multipart/alternative") {
//                                    val parts = fullMessage.payload.parts
//                                    for (part in parts) {
//                                        if (part.mimeType == "text/plain") {
//
//                                            Log.d("TAG", "fetchAndProcess: " + "excutee")
//
//                                            var source = Source()
//                                            source.id = "2"
//                                            source.name = "Hey this is to inform you about event!!"
//
//                                            article4.type = 1
//                                            article4.title = subject
//                                            article4.author =
//                                                "This is a sample news title which has no intent"
//                                            var body_attach: String = ""
//                                            try {
//                                                body_attach =
//                                                    convertRawDataToPlainText(part.body.data)!!
//                                            } catch (e: java.lang.Exception) {
//
//                                            }
//
//                                            article4.description = body_attach
//                                            arrayList.add(article4)
//                                            Log.d(TAG, "fetchAndProcess: " + body_attach)
//                                            getCallData()
//                                            break
//
//
//                                        }
//                                    }
//                                }
//
//                                // Process the retrieved email header and body as needed
//                                // ...
//
//
//                            } catch (e: SocketTimeoutException) {
//                                // Process eventual failures.
//                                // Restart request on socket timeout.
//                                e.printStackTrace()
//                                fetchAndProcess()
//                            } catch (e: Exception) {
//                                // Process eventual failures.
//                                e.printStackTrace()
//                            }
//                        }
//                        fetchAndProcess()
//                    }
//                }
                    } else if (label.equals("Spam")) {

                    }

                }
            }
        }


//    fun  Gmail.ExtractLablesData(user: String, label: Label, process: (String) -> Unit)
//
//    {
//        runBlocking(dispatcher) {
//            processMessages(user, label) { m ->
//                launch {
//                    fun fetchAndProcess() {
//                        try {
//                            var message1: Message
//
//                            message1 =
//                                users().messages().get(user, m.id).apply { format = "METADATA" }
//                                    .execute()
//                            message1.payload.headers.find { it.name == "From" }?.let { from ->
//                                process(from.value.parseAddress())
//                            }
//
//                            var article4: Article = Article()
//                            val messageId = m.id
//                            val fullMessage: Message =
//                                users().messages().get(user, messageId)
//                                    .setFormat("full").execute()
//
//                            // Retrieve email header information
//                            val subject = fullMessage.payload.headers.stream()
//
////                                .filter { header: MessagePartHeader -> header.name == "Subject" }
//                                .findFirst()
//                                .map { obj: MessagePartHeader ->
//                                    Log.d(
//                                        TAG,
//                                        "fetchAndProcess: " + obj.name + "??" + obj.value
//                                    )
//                                    obj.value
//
//                                }
//                                .orElse("")
//
//                            // Retrieve email body content
//                            var body: String? = ""
//                            if (fullMessage.payload.mimeType == "text/plain") {
//                                body = fullMessage.payload.body.data
//                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {
//                                val parts = fullMessage.payload.parts
//                                for (part in parts) {
//                                    if (part.mimeType == "text/plain") {
//
//                                        Log.d("TAG", "fetchAndProcess: " + "excutee")
//
//                                        var source = Source()
//                                        source.id = "2"
//                                        source.name = "Hey this is to inform you about event!!"
//
//                                        article4.type = 1
//                                        article4.title = subject
//                                        article4.author =
//                                            "This is a sample news title which has no intent"
//                                        var body_attach: String = ""
//                                        try {
//                                            body_attach =
//                                                convertRawDataToPlainText(part.body.data)!!
//                                        } catch (e: java.lang.Exception) {
//
//                                        }
//
//                                        article4.description = body_attach
//                                        arrayList.add(article4)
//                                        Log.d(TAG, "fetchAndProcess: " + body_attach)
//                                        getCallData()
//                                        break
//
//
//                                    }
//                                }
//                            }
//
//                            // Process the retrieved email header and body as needed
//                            // ...
//
//
//                        } catch (e: SocketTimeoutException) {
//                            // Process eventual failures.
//                            // Restart request on socket timeout.
//                            e.printStackTrace()
//                            fetchAndProcess()
//                        } catch (e: Exception) {
//                            // Process eventual failures.
//                            e.printStackTrace()
//                        }
//                    }
//                    fetchAndProcess()
//                }
//            }
//        }
//    }
    }

//    private fun extractUrlsFromMessage(message: Message) {
//        val bodyData: String = message.getPayload().getBody().getData()
//        if (bodyData != null) {
//            val decodedBody =
//                String(Base64.getUrlDecoder().decode(bodyData), StandardCharsets.UTF_8)
//            val urls = decodedBody.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray() // Split by whitespace
//            for (url in urls) {
//                if (isUrl(url)) {
//                    // Process the URL
//                    println("URL: $url")
//                    Log.d(TAG, "extractUrlsFromMessage: " + url)
//                }
//            }
//        }
//    }

    private fun isUrl(text: String): Boolean {
        // Check if the given text is a valid URL
        // You can use a regular expression or URL validation library for more accurate URL detection
        // This is a simple example to check if the text starts with "http://" or "https://"
        return text.startsWith("http://") || text.startsWith("https://")
    }

    val imageUrls = ArrayList<Bitmap>()

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            // Handle the exception if the byte array cannot be decoded into a bitmap
        }
        return bitmap
    }

//    private fun getTextFromMessageParts(part: MessagePart): String? {
//        val sb = StringBuilder()
//
//        if (part.mimeType == "text/plain") {
//            val partData = part.body.data
//            if (partData != null) {
//                sb.append(String(Base64.getDecoder().decode(partData), StandardCharsets.UTF_8))
//            }
//        } else if (part.mimeType == "text/html") {
//            val partData = part.body.data
//            if (partData != null) {
//                val htmlContent =
//                    String(Base64.getDecoder().decode(partData), StandardCharsets.UTF_8)
//                sb.append(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY).toString())
//            }
//        } else if (part.parts != null) {
//            //sb.append(getTextFromMessageParts(part.parts.))
//        } else if (part.filename != null && part.filename.length > 0) {
//            val attachmentText = """
//                Attachment: ${part.filename}
//
//                """.trimIndent()
//            sb.append(attachmentText)
//        }
//
//        return sb.toString()
//    }

    private fun String.parseAddress(): String {
        return if (contains("<")) {
            substringAfter("<").substringBefore(">")
        } else {
            this
        }
    }

    fun convertRawDataToPlainText(binaryData: String): String? {
        val decodedBytes: ByteArray =Base64.getUrlDecoder().decode(binaryData)

        // Convert the decoded bytes to a string
        val decodedString = String(decodedBytes)


        return decodedString
    }

    val dispatcher = object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            executors.execute(block)
        }
    }

    fun extract(activity: Activity, ac: String, accontname: String,labelid:Int) {

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
//        val credential = GoogleCredential.Builder()
//            .setTransport(httpTransport)
//            .setJsonFactory(JSON_FACTORY)
//            .setServiceAccountId("service_account")
//            // .setServiceAccountPrivateKeyFromP12File(new
//            // File(certLocation))
//            .setServiceAccountPrivateKey(serviceAccountPrivateKey)
//            .setServiceAccountUser(ac).build()
//
//            val service= Gmail.Builder(httpTransport, JSON_FACTORY, credential)
//                .setApplicationName("Google-TasksAndroidSample/1.0").build()

//            val service =
//                Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(resources, httpTransport))
//                    .setApplicationName(APPLICATION_NAME)
//                    .build()

        // Find the requested label
        val user = ac

        val labelList = gmailService.users().labels().list(user).execute()
        val label = labelList.labels
//                .find { it.id == labelName } ?: error("Label `$labelName` is unknown.")


        // Process all From headers.
        val senders = mutableSetOf<String>()
//        for(labellist in label) {
        Log.d(TAG, "extract: " + label)
        gmailService.processFroms(user, label.get(labelid)) {

            senders += it

        }

        // }

        // senders.forEach(::println)
        //Log.d("TAG", "extract: "+senders.size)
//        } catch (e: Exception) {
        // Log.e("getCredentials", "extract: "+e.message)

        // }
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
            includeSpamTrash = true
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

    private fun getEmailUrl(messageId: String): String? {
        val baseUrl = "https://mail.google.com/mail/u/0/#inbox/"
        return baseUrl + messageId
    }
}