package com.aiemail.superemail.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.Models.Category
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Repository.FetchRepository
import com.aiemail.superemail.Repository.FullMessageRepository

import kotlinx.coroutines.*

class FullMessagelViewmodel(private val foodItemRepository: FullMessageRepository) : ViewModel() {


    val scope = CoroutineScope(Dispatchers.Main)

    class Factory(private val app: Application, private val repo: FullMessageRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FullMessagelViewmodel(repo) as T
        }

    }


    var mutableLiveData: ArrayList<Category>? = arrayListOf()


    fun insertMail(fullMessage: FullMessage) {
        foodItemRepository.adddata(fullMessage)

    }

    fun DeleteMails(fullMessage: FullMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.deletedata(fullMessage)

        }

    }

    fun getEmailList(): LiveData<List<FullMessage>> {
        return foodItemRepository.getFullMessageList()

    }

    fun getEmail(senderid:String): LiveData<FullMessage> {
        return foodItemRepository.getMessage(senderid)

    }



}