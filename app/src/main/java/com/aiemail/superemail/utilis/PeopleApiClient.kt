package com.aiemail.superemail.utilis

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.BatchGetContactGroupsResponse
import com.google.api.services.people.v1.model.GetPeopleResponse

class PeopleApiClient(private val credential: GoogleAccountCredential) {

    companion object {
        private const val TAG = "PeopleApiClient"
        private val HTTP_TRANSPORT = NetHttpTransport()
        private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
    }

    fun fetchProfilePictures(emailList: List<String>, callback: Callback) {
        FetchProfilePicturesTask(credential, emailList, callback).execute()
    }

    interface Callback {
        fun onSuccess(profilePictureUrls: Map<String, String>)
        fun onError(errorMessage: String)
    }

    private inner class FetchProfilePicturesTask(
        private val credential: GoogleAccountCredential,
        private val emailList: List<String>,
        private val callback: Callback
    ) : AsyncTask<Void, Void, Map<String, String>>() {

        private lateinit var peopleService: PeopleService

        override fun doInBackground(vararg params: Void?): Map<String, String> {
            try {
                val httpTransport = NetHttpTransport()
                val jsonFactory = JacksonFactory.getDefaultInstance()

                val initializer: HttpRequestInitializer = HttpRequestInitializer { request ->
                    credential.initialize(request)
                }
                peopleService = PeopleService.Builder(httpTransport, jsonFactory, initializer)
                    .setApplicationName(MyApplication.instance.context!!.getString(R.string.app_name))
                    .build()
                Log.d(TAG, "doInBackground: "+peopleService.people())

                val personFields = "photos"
                val request = peopleService.people().batchGet
                request.setPersonFields(personFields)
                request.setResourceNames(emailList.map { "people/${Uri.encode(it)}" })

                val response = request.execute() as BatchGetContactGroupsResponse
                val personResponses = response.responses as List<GetPeopleResponse>

                val profilePictureUrls = mutableMapOf<String, String>()
                for (i in personResponses.indices) {
                    val email = emailList[i]

                    val personResponse = personResponses[i].responses.get(i).person

                    if (personResponse.photos != null && personResponse.photos.isNotEmpty()) {
                        val profilePictureUrl = personResponse.photos[0].url
                        profilePictureUrls[email] = profilePictureUrl
                    }

                    return profilePictureUrls
                }
            } catch (e: UserRecoverableAuthIOException) {
                // Handle user authorization recovery (e.g., showing permission dialog)
                Log.e(TAG, "User authorization required: ${e.message}")
                callback.onError("User authorization required")
            } catch (e: GoogleJsonResponseException) {
                // Handle API error response
                Log.e(TAG, "API error: ${e.details.message}")
                callback.onError("API error: ${e.details.message}")
            }
            return emptyMap()
        }
    }
}
