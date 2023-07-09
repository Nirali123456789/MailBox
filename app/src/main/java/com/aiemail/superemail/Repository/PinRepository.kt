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
import androidx.lifecycle.liveData
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Models.Source
import com.aiemail.superemail.Rooms.AllEmailDao
import com.aiemail.superemail.Rooms.PinDao
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
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.LinkedHashMap
import kotlin.coroutines.CoroutineContext


class PinRepository(private val pindao: PinDao) {
    var pinListData: LiveData<ArrayList<Pin>> = MutableLiveData()
    private val _pinList: MutableLiveData<Pin> = MutableLiveData()
    val pin_data: MutableLiveData<Pin> get() = _pinList

    // Create a coroutine scope
    val scope = CoroutineScope(Dispatchers.Main)
    fun getCallData() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
              //  _pinList.value = pinList

            }

        }
    }

    fun adddata(pin: Pin) {
        scope.launch {
            pindao.insertPin(pin)
        }
    }

    fun deletedata(pin: Pin) {
        scope.launch {
            pindao.deletePin(pin)
        }
    }

     fun getPinList(): LiveData<List<Pin>> {
        return pindao.getAllPins()
    }
    fun getArchiveList(): LiveData<List<Pin>> {
        return pindao.getArchivedItems()
    }



}