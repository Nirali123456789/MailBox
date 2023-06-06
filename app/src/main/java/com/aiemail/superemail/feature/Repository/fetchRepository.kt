//#Created by Nirali Pandya

package com.aiemail.superemail.feature.Repository


import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.feature.Models.Email
import com.aiemail.superemail.feature.Models.FullMessage
import com.aiemail.superemail.feature.Models.Source
import com.aiemail.superemail.feature.Rooms.EmailDao
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePartHeader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class fetchRepository(private val foodItemDao: EmailDao, var page: Int = 1) {

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


    fun fetchMails(
        activity: Activity,
        label: String,
        email: String,
        account: GoogleSignInAccount,
        id: Int
    ) {
        val recipientEmail = "swainfo.nirali@gmail.com"
        GlobalScope.launch {
            ExtractMails(activity, email, account.email!!, id)


        }
    }





    private fun Gmail.processFroms(
        user: String,
        label: Label,
        process: (String) -> Unit
    ) {
        runBlocking(dispatcher) {

            processMessages(user, label) { m ->
                launch {
                    fun fetchAndProcess() {

                        try {

                            var message1: Message
                            var article4: Email = Email()
                            message1 =
                                users().messages().get(user, m.id).setFormat("full")

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
                            val sdf = SimpleDateFormat("MMM", Locale.getDefault())

                            article4 = Email()
                            article4.date = getFormattedDate(MyApplication.instance.context!!,internalDateMillis)

                            article4.type=0
                            Log.d(TAG, "dateitemmm: "+  article4.date )
                            if (!arrayList.contains(article4)) {
                                arrayList.add(article4)
                                CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                                    foodItemDao.insert(article4)
                                    Log.d(TAG, "fetchAndProcess:inside "+article4)
                                }
                            }




//

                          var  article:Email = Email()
                            article.title = fullMessage.snippet
                            article.type=1
                            var title = fullMessage.payload.headers.stream()
                                .filter { header: MessagePartHeader -> header.name == "Subject" }
                                .findFirst()
                                .map { obj: MessagePartHeader ->

                                    //article4.content = obj.value
                                    article.author = obj.value


                                }

                            var body: String? = ""

                            if (fullMessage.payload.mimeType == "text/html") {
                                // Log.d(TAG, "fetchAndProcess: "+fullMessage.payload.parts)
                                body = fullMessage.payload.body.data
                                val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(body)
                                val decodedString = String(decodedBytes)
                                article.description = decodedString

                            } else if (fullMessage.payload.mimeType == "multipart/alternative") {

                                val parts = fullMessage.payload.parts
                                for (part in parts) {
                                    //  Log.d(TAG, "fetchAndProcess: "+convertRawDataToPlainText(part.body.data))

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

                                if (!article.title.isNullOrBlank() ||  !article.content.isNullOrBlank()|| !article.author.isNullOrBlank())
                                {
                                    Log.d(TAG, "alldata:" + article.title.isNullOrBlank()+">>"+article.content.isNullOrBlank()+">>>"+article.author.isNullOrBlank()+">")
                                    if (article.title.equals("") &&!article.content.equals(""))
                                    article.title=article.content
                                    else if (article.title.equals("") &&!article.author.equals(""))
                                        article.title=article.author
                                    foodItemDao.insert(article)
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
    fun getFormattedDate(context: Context, smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = smsTimeInMilis

        val now = Calendar.getInstance()

        val timeFormatString = "h:mm aa"
        val dateTimeFormatString = " MMM"
        val HOURS = 60 * 60 * 60
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today "
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday "
        } else
            return DateFormat.format(dateTimeFormatString, smsTime).toString()


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

    fun ExtractMails(activity: Activity, ac: String, accontname: String, labelid: Int) {

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
        Log.d(TAG, "extract: " + label)
//                .find { it.id == labelName } ?: error("Label `$labelName` is unknown.")


        // Process all From headers.
        val senders = mutableSetOf<String>()


        gmailService.processFroms(user, label.get(labelid)) {

            senders += it

        }


    }

    private tailrec fun Gmail.processMessages(
        user: String,
        label: Label,
        nextPageToken: String? = null,
        process: (Message) -> Unit
    ) {


        val messages = users().messages().list().apply {
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