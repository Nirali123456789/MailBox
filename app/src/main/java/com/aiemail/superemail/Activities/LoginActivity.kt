package com.aiemail.superemail.Activities


import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.Slideshow.OnBoardingActivity
import com.aiemail.superemail.databinding.ActivityLoginBinding
import com.aiemail.superemail.prefs
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PeopleHelper
import com.aiemail.superemail.viewmodel.EmailViewmodel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.ListMessagesResponse
import com.google.api.services.gmail.model.Message
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.ListConnectionsResponse
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 100
    private lateinit var binding: ActivityLoginBinding
    private val TIMEOUT = 1500
    private var TOKENS_DIRECTORY_PATH = "tokens/gmail"
    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }


    companion object {
        public var newsList1: ArrayList<Email> = arrayListOf()
    }

    val scopes =
        GmailScopes.GMAIL_READONLY + " " + GmailScopes.GMAIL_SEND + " " + GmailScopes.GMAIL_LABELS
    private val SCOPES = setOf(
        GmailScopes.GMAIL_LABELS,
        GmailScopes.GMAIL_READONLY,
        GmailScopes.GMAIL_METADATA,
        GmailScopes.GMAIL_SEND
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Helpers.SetUpFullScreen(window)
        //Hide navigation bar and status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                // Default behavior is that if navigation bar is hidden, the system will "steal" touches
                // and show it again upon user's touch. We just want the user to be able to show the
                // navigation bar by swipe, touches are handled by custom code -> change system bar behavior.
                // Alternative to deprecated SYSTEM_UI_FLAG_IMMERSIVE.
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // make navigation bar translucent (alternative to deprecated
                // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                // - do this already in hideSystemUI() so that the bar
                // is translucent if user swipes it up

                // Finally, hide the system bars, alternative to View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                // and SYSTEM_UI_FLAG_FULLSCREEN.
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            Helpers.hideSystemUI(this.window.decorView)




        }
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetUpUi()





    }

    private fun SetUpUi() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(
                    Scope("https://mail.google.com/"),
                    Scope(GmailScopes.GMAIL_READONLY),
                    Scope(GmailScopes.GMAIL_SEND),
                    Scope(GmailScopes.GMAIL_LABELS),
                    Scope("https://www.googleapis.com/auth/contacts.readonly")


                )
                .requestIdToken(resources.getString(R.string.web_id))
                .requestServerAuthCode(resources.getString(R.string.web_id))
                .requestEmail()
                .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //setGooglePlusButtonText(binding.login,"Sign up with Google")
        binding.login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            getResult.launch(signInIntent)


        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            }
        }


    val httpTransport =
        NetHttpTransport()
            .createRequestFactory { request ->
                request.connectTimeout = TIMEOUT
                request.readTimeout = TIMEOUT
            }.transport
    val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val idToken1 = account.idToken
        var AuthCode:String=""
        if (acct != null) {
            AuthCode= account.getServerAuthCode()!!
            Log.d("TAG", "auth Code:" + AuthCode)
        };

        val personId = acct!!.id
        val personPhoto = acct!!.photoUrl
        Log.d("TAG", "handleSignInResult: " + personId + "??" + personPhoto)


//        #Authnticate

        val httpClient: HttpClient = DefaultHttpClient()
        val httpPost = HttpPost("https://accounts.google.com/o/oauth2/auth")

        try {
            val nameValuePairs: MutableList<NameValuePair> = ArrayList(1)
            nameValuePairs.add(BasicNameValuePair("idToken", idToken1))
            httpPost.entity = UrlEncodedFormEntity(nameValuePairs)
            val response: HttpResponse = httpClient.execute(httpPost)
            val statusCode: Int = response.getStatusLine().getStatusCode()
            val responseBody = EntityUtils.toString(response.getEntity())
            //Log.i("TAG33", "Signed in as: "+responseBody)
        } catch (e: ClientProtocolException) {
            Log.e("TAG33", "Error sending ID token to backend." + e.message)
        } catch (e: IOException) {
            Log.e("TAG33", "Error sending ID token to backend." + e.message)
        }


        var verifier = GoogleIdTokenVerifier.Builder(httpTransport, JSON_FACTORY)
            .setAudience(Collections.singletonList(resources.getString(R.string.web_id)))
            .build();


        var idToken = verifier.verify(idToken1);
        if (idToken1 != null) {
         //   var payload = idToken.getPayload();
//            var userId = payload.getSubject();
//            System.out.println("User ID: " + userId);
//            var email = payload.getEmail();
//            var emailVerified = payload.getEmailVerified();
//            var name = payload.get("name");
            Log.i("TAG33", "Signed in as: " + account.givenName)
            // updateUI()


            (application as MyApplication).repository.fetchMails(
                this@LoginActivity,
                acct.email!!,
                acct.email!!,
                acct,
                2
            )
            model.DeleteMails()
            model.insertMail()
            val editor: SharedPreferences.Editor =
                applicationContext.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE)
                    .edit()
            editor.putString("username", acct.email)
            editor.putString("account", acct.displayName)
            editor.putString("AuthCode",AuthCode)
            editor.putString("PersonPic",personPhoto.toString())
            editor.commit()
            Thread {

                // Fetch the user's profile picture URL
                var myPref = getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
                var Authcode = myPref.getString("AuthCode", "");
                var username = myPref.getString("username", "");
            //peopleApiCall(Authcode!!, acct.account!!)


            }.start()
            updateUI()


            //encript if you need to secure it


//        var manager: GmailManager
//        manager =  GmailManager("359794650065-t9p7q1ne4i5fiqili00r5kpj68v6qckc.apps.googleusercontent.com","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",personId, "offline","consent",SCOPES);
//
//
//        val user = "me"
//       manager.getmessage(personId,"Spark")


//            Log.d("TAG", "handleSignInResult: "+idToken1)
            //  var em= EmailExtractor()
            //  EmailExtractor().extract("Inbox",resources)
//var gmailQuickstart:GmailQuickstart=GmailQuickstart()
//        gmailQuickstart.AuthMethod(resources)

//            val httpPost = HttpPost("https://accounts.google.com/o/oauth2/auth")
//
//            try {
//                val nameValuePairs: MutableList<NameValuePair> = ArrayList(1)
//                nameValuePairs.add(BasicNameValuePair("authCode", authCode))
//                httpPost.entity = UrlEncodedFormEntity(nameValuePairs)
//                val response: HttpResponse = httpClient.execute(httpPost)
//                val statusCode: Int = response.getStatusLine().getStatusCode()
//                val responseBody = EntityUtils.toString(response.getEntity())
//            } catch (e: ClientProtocolException) {
//                Log.e("TAG", "Error sending auth code to backend.", e)
//            } catch (e: IOException) {
//                Log.e("TAG", "Error sending auth code to backend.", e)
//            }

//
//            var verifier =  GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//
//                .setAudience(Collections.singletonList(getString(R.string.client_id)))
//
//                .build();
//
//
//
//            var idToken = verifier.verify(idTokenString);
//            if (idToken != null) {
//                var payload = idToken.getPayload();
//
//
//                var userId = payload.getSubject();
//                System.out.println("User ID: " + userId);
//
//
//                var email = payload . getEmail ();
//                var emailVerified = (payload.getEmailVerified());
//                var name = payload.get ("name");
//                var pictureUrl =payload . get ("picture");
//                var locale = payload . get ("locale");
//                var familyName = payload . get ("family_name");
//                var givenName =payload . get ("given_name");
//            }


//            var token_id=account.idToken
//
//
//            val transport: HttpTransport = NetHttpTransport()
//
//            val jsonFactory: JsonFactory = JacksonFactory()
//            val manager = GooglePublicKeysManager.Builder(transport, JSON_FACTORY)
//                .setPublicCertsEncodedUrl("https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
//                .build()
//            val verifier = GoogleIdTokenVerifier.Builder(manager)
//                .setAudience(listOf<String>("175589752046-oc87iv9r1t0p4f3p3trettiv7kk81s7b.apps.googleusercontent.com"))
//                .setIssuer("accounts.google.com")
//                .build()


// (Receive idTokenString by HTTPS POST)


// (Receive idTokenString by HTTPS POST)\
            // Log.e("TAG", "handleSignInResult:2 "+token_id)
            // val idToken: GoogleIdToken = verifier.verify(token_id)

//
//            TOKENS_DIRECTORY_PATH= token_id!!
//            // Build a new authorized API client service.
//            val idToken: GoogleIdToken = verifier.verify(token_id)
//            val payload: GoogleIdToken.Payload = idToken.getPayload()
//
//            // Print user identifier
//
//            // Print user identifier
//            val userId: String = payload.getSubject()
//            println("User ID: $userId")

            // Get profile information from payload

            // Get profile information from payload
//            val email: String = payload.getEmail()
//            val emailVerified: Boolean = java.lang.Boolean.valueOf(payload.getEmailVerified())
//            val name = payload.get("name") as String
//            val pictureUrl = payload.get("picture") as String
//            val locale = payload.get("locale") as String
//            val familyName = payload.get("family_name") as String
//            val givenName = payload.get("given_name") as String


//            var user = "me";
//            var listResponse = service.users().labels().list(user).execute();
//            var labels = listResponse.getLabels();
//
//
//            if (labels.isEmpty()) {
//                System.out.println("No labels found.");
//            } else {
//                System.out.println("Labels:");
//                for (label in labels) {
//                    Log.d("TAG", "onCreate: " + label.name)
//
//                }
//            }

            // Signed in successfully, show authenticated UI.
            //updateUI()
        }
    }
//    public fun peopleApiCall(serverAuthCode: String,acc: Account) {
//        val httpTransport: HttpTransport = NetHttpTransport()
//        val jsonFactory = JacksonFactory.getDefaultInstance()
//
//        // Redirect URL for web based applications.
//        // Can be empty too.
//        val redirectUrl = "urn:ietf:wg:oauth:2.0:oob"
//
//
//        // Exchange auth code for access token
//        val tokenResponse = GoogleAuthorizationCodeTokenRequest(
//            httpTransport,
//            jsonFactory,
//            "390762603984-lbdjhgffolaq6eb6jemsljand6orptqs.apps.googleusercontent.com",
//            "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",
//            serverAuthCode,
//            redirectUrl
//        ).execute()
//
//        // Then, create a GoogleCredential object using the tokens from GoogleTokenResponse
//        val credential = GoogleCredential.Builder()
//            .setClientSecrets(
//                "390762603984-lbdjhgffolaq6eb6jemsljand6orptqs.apps.googleusercontent.com",
//                "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"
//            )
//            .setTransport(httpTransport)
//            .setJsonFactory(jsonFactory)
//            .build()
//        credential.setFromTokenResponse(tokenResponse)
//        val peopleService = PeopleHelper.setUp(this, serverAuthCode, acc)
//        val id= PeopleHelper.getPersonIdFromEmail("swainfo.nirali@gmail.com", credential,this)
//        val peopleService1 = PeopleService.Builder(
//            GoogleNetHttpTransport.newTrustedTransport(),
//            GsonFactory.getDefaultInstance(),
//            credential
//        ).setApplicationName(getString(R.string.app_name)).build()
//        // Retrieve the visible people
//        // Retrieve the visible people
//
//        Log.d("TAG", "peopleApiCall: "+id)
//        val response = peopleService1.people().get("people/102849317771326025140")
//            .setPersonFields("photos")
//
//
//            .execute()
//
//
//        Log.d("TAG", "peopleApiCall: "+response)
////        val people: MutableList<com.google.api.services.plus.model.Person>? = peopleFeed.items
////        for (p in people?.indices!!) {
////            Log.d("peopleApiCall", "peopleApiCall: " + people.get(p).url)
////        }
//
//
//    }

    fun getEmails(gmail: Gmail): ListMessagesResponse {
        val userId = "me" // 'me' represents the authenticated user

        val request = gmail.users().messages().list(userId)
        val response = request.execute()

        return response
    }

    private fun createEmail(to: String, subject: String, messageBody: String): MimeMessage {
        val props = Properties()
        val session = Session.getDefaultInstance(props, null)

        val email = MimeMessage(session)
        email.setFrom(InternetAddress("nehavaidya95@gmail.com"))
        email.addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(to))
        email.subject = subject

        val bodyPart = MimeBodyPart()
        bodyPart.setContent(messageBody, "text/plain")

        val multipart = MimeMultipart()
        multipart.addBodyPart(bodyPart)

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


    private fun updateUI() {
        prefs.islogin = true
        if (!prefs.intExamplePref) {
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }, 3100)
        } else {
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3100)
        }
    }
    protected fun setGooglePlusButtonText(signInButton: SignInButton, buttonText: String?) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (i in 0 until signInButton.childCount) {
            val v: View = signInButton.getChildAt(i)
            if (v is TextView) {
                val tv = v as TextView
                tv.text = buttonText
                return
            }
        }
    }

//

}