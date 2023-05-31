package com.aiemail.superemail.Activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.ListMessagesResponse
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePartHeader
import com.aiemail.superemail.MyApplication

import com.aiemail.superemail.feature.Slideshow.OnBoardingActivity
import com.aiemail.superemail.feature.Models.Article
import com.aiemail.superemail.feature.Models.Source
import com.aiemail.superemail.feature.utilis.Constant
import com.aiemail.superemail.feature.viewmodel.CategoryViewModel
import com.aiemail.superemail.prefs

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.SocketTimeoutException

import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import kotlin.collections.ArrayList
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 100
    private lateinit var binding: ActivityLoginBinding
    private val TIMEOUT = 1500
    private var TOKENS_DIRECTORY_PATH = "tokens/gmail"
    val model: CategoryViewModel by viewModels() {
        CategoryViewModel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }



    companion object {
        public var newsList1: ArrayList<Article> = arrayListOf()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Constant.SetUpFullScreen(window)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(GmailScopes.GMAIL_READONLY))
                .requestIdToken(resources.getString(R.string.web_id))
                .requestEmail()
                .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            getResult.launch(signInIntent)


        }


        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
        var wv1 = binding.webview1

        wv1.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                // If you wnat to open url inside then use
                view.loadUrl(url);

                // if you wanna open outside of app
                /*if (url.contains(URL)) {
                        view.loadUrl(url)
                        return false
                    }else {
                        // Otherwise, give the default behavior (open in browser)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }*/


                return true
            }


        }
        wv1.getSettings().setJavaScriptEnabled(true);


       // wv1.loadData(html, "text/html", "UTF-8");
    }
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
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
    private val SCOPES = setOf(
        GmailScopes.GMAIL_LABELS,
        GmailScopes.GMAIL_READONLY,
        GmailScopes.GMAIL_METADATA
    )

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val idToken1 = account.idToken

        val personId = acct!!.id
        val personPhoto = acct!!.photoUrl
        Log.d("TAG", "handleSignInResult: " + personId + "??" + idToken1)


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
            var payload = idToken.getPayload();
            var userId = payload.getSubject();
            System.out.println("User ID: " + userId);
            var email = payload.getEmail();
            var emailVerified = payload.getEmailVerified();
            var name = payload.get("name");
            Log.i("TAG33", "Signed in as: " + account.givenName)
            updateUI()


                (application as MyApplication).repository.fetchMails(this@LoginActivity, email, email, acct,4)
                model.insertMail()


        } else {
            System.out.println("Invalid ID token.");
        }


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

    fun getEmails(gmail: Gmail): ListMessagesResponse {
        val userId = "me" // 'me' represents the authenticated user

        val request = gmail.users().messages().list(userId)
        val response = request.execute()

        return response
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


//

}