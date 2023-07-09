package com.aiemail.superemail.utilis

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aiemail.superemail.R
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.Person
import com.google.api.services.plus.Plus
import java.io.IOException

object PeopleHelper {
    private const val APPLICATION_NAME = "Peoples App"
    @Throws(IOException::class)
    fun setUp(context: Context, serverAuthCode: String?,user: Account): Plus {
        val httpTransport: HttpTransport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        // Redirect URL for web based applications.
        // Can be empty too.
        val redirectUrl = "urn:ietf:wg:oauth:2.0:oob"


        // Exchange auth code for access token
//        val tokenResponse = GoogleAuthorizationCodeTokenRequest(
//            httpTransport,
//            jsonFactory,
//            "390762603984-lbdjhgffolaq6eb6jemsljand6orptqs.apps.googleusercontent.com",
//            "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",
//            serverAuthCode,
//            redirectUrl
//        ).execute()

        // Then, create a GoogleCredential object using the tokens from GoogleTokenResponse
        val credential = GoogleAccountCredential.usingOAuth2(
            context, setOf(GmailScopes.GMAIL_MODIFY)
        )
            .setBackOff(ExponentialBackOff())
            .setSelectedAccount(user)
       // Log.d("TAG", "setUp: "+tokenResponse)
      //tokenResponse.refreshToken


      //  credential.setFromTokenResponse(tokenResponse)
        //refreshAccessToken(tokenResponse.refreshToken)


        // credential can then be used to access Google services
        return Plus.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }
    fun refreshAccessToken(refreshToken: String): String? {
        try {
            val httpTransport: HttpTransport = NetHttpTransport()
            val jsonFactory = JacksonFactory.getDefaultInstance()
            val credential = GoogleCredential.Builder()
                .setClientSecrets(
                    "390762603984-lbdjhgffolaq6eb6jemsljand6orptqs.apps.googleusercontent.com",
                    "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"
                )
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build() // Replace "your-scopes" with the desired scopes for the access token

            val refreshRequest = GoogleRefreshTokenRequest(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                refreshToken,
                "390762603984-lbdjhgffolaq6eb6jemsljand6orptqs.apps.googleusercontent.com",
                "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"
            )

            val response = refreshRequest.execute()
            return response.accessToken
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }








    fun getPersonIdFromEmail(email: String,credential:HttpRequestInitializer,context: Activity): String? {
        try {
//
            val peopleService = PeopleService.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName(context.getString(R.string.app_name)).build()



            // Use the service to search for people by email address
//            try {

            val response = peopleService.people().connections()
                .list("people/me")
                .setPageSize(100)
                .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers")
                .execute()
            Log.e("getPersonIdFromEmail", "getPersonIdFromEmail: ")
//            val me: Person = peopleService.people().get("people/$email")
//
//                .execute()




            return "people/c949940121999911795"

//            val connectionsRequest: AbstractGoogleClientRequest<ListConnectionsResponse> =
//                peopleService.people().connections().list("people/me")
//                    .setPersonFields("connections.emailAddresses")
//                    .setPageSize(1000) // Set an appropriate page size based on your needs



//            val connections = connectionsResponse.connections
//            for (connection in connections) {
//                val emailAddresses = connection.emailAddresses
//                for (emailAddress in emailAddresses) {
//                    if (emailAddress.value.equals(email, ignoreCase = true)) {
//                        Log.d("getPersonIdFromEmail", "getPersonIdFromEmail: "+connection.resourceName)
//                        return connection.resourceName
//                    }
//                }
//            }
        } catch (e: UserRecoverableAuthIOException) {
            // Handle error
            val authorizationIntent: Intent = e.getIntent()
            context.startActivityForResult(authorizationIntent, 101)
            e.printStackTrace()
        } catch (e: UserRecoverableAuthIOException) {
            val authorizationIntent: Intent = e.getIntent()
            context.startActivityForResult(authorizationIntent, 101)
            // Handle other exceptions
            e.printStackTrace()
        }
        return null
    }
}