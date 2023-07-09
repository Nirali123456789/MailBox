package com.aiemail.superemail.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.Models.AllMails

import com.aiemail.superemail.Models.Category
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Repository.AllfetchRepository
import kotlinx.coroutines.*

class AllEmailViewmodel(private val foodItemRepository: AllfetchRepository) : ViewModel() {

    public val allMails: MutableLiveData<AllMails> = foodItemRepository.Allmail
    public val allMailsSection: MutableLiveData<AllMails> = foodItemRepository.allsection


    class Factory(private val app: Application, private val repo: AllfetchRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllEmailViewmodel(repo) as T
        }

    }


    var mutableLiveData: ArrayList<Category>? = arrayListOf()


    fun insertMail() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.getCallData()

        }

    }

    fun AllInBox(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.AllInbox(activity)

        }
    }

    fun DeleteMails() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.deletedata()

        }

    }

    //    fun getEmailList(): LiveData<List<AllMails>> {
//
//        return foodItemRepository.getAllMails()
//
//    }
    fun getAllMailList(): MutableLiveData<Map<Int, List<AllMails>>> {
        return foodItemRepository.getAllMails()
    }


    fun insertbody(articles: AllMails) {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.adddata(articles)

        }

    }

    val maildata: LiveData<HashMap<String, ArrayList<AllMails>>> = foodItemRepository.todoListFlow
    val listdata: LiveData<MutableList<AllMails>> = foodItemRepository.emaillist
    val fullbody: LiveData<MutableList<FullMessage>> = foodItemRepository.fullmessage

}