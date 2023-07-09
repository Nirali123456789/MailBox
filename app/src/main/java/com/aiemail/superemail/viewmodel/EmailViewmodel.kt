package com.aiemail.superemail.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.Models.Category
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Repository.FetchRepository
import io.github.luizgrp.sectionedrecyclerviewadapter.Section

import kotlinx.coroutines.*

class EmailViewmodel (private val foodItemRepository: FetchRepository) : ViewModel() {

    public val MailObject:MutableLiveData<Email> = foodItemRepository.Allmail
    public val SectionObject:MutableLiveData<Section> = foodItemRepository.Section
    val scope = CoroutineScope(Dispatchers.Main)
    class Factory(private val app: Application, private val repo: FetchRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EmailViewmodel(repo) as T
        }

    }




    fun cancelJobs(){
        foodItemRepository.cancelJobs()
    }


    fun insertMail() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.getCallData()

        }

    }
    fun AllInBox(activity: Activity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.AllInbox(activity)

        }
    }
    fun DeleteMails() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.deletedata()

        }

    }
 fun getEmailList(): LiveData<List<Email>> {
        return foodItemRepository.getEmails()

    }

    fun getUnreadEmailList(): LiveData<List<Email>> {
        return foodItemRepository.getUnReadEmails()

    }

    fun getAsideList(): LiveData<List<Email>> {
        return foodItemRepository.getSnooze()

    }
    fun adddataAside(pin: Email) {
        scope.launch {
            pin.isAside=true
            foodItemRepository.insertAside(pin)
        }
    }
    fun FetchAsideList(): LiveData<List<Email>> {
        return  foodItemRepository.getAsideList()


    }
    fun adddataSnooze(snooze: Email) {
        scope.launch {
            snooze.isSnoozed=true
            foodItemRepository.insertAside(snooze)
        }
    }
    fun FetchSnoozeList(): LiveData<List<Email>> {
        return  foodItemRepository.getAsideList()


    }





}