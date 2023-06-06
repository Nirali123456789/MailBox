package com.aiemail.superemail.feature.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.feature.Models.Email
import com.aiemail.superemail.feature.Models.Category
import com.aiemail.superemail.feature.Models.FullMessage
import com.aiemail.superemail.feature.Repository.fetchRepository
import kotlinx.coroutines.*

class EmailViewmodel(private val foodItemRepository: fetchRepository) : ViewModel() {


    class Factory(private val app: Application, private val repo: fetchRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EmailViewmodel(repo) as T
        }

    }


    var mutableLiveData: ArrayList<Category>? = arrayListOf()


    fun insertMail() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.getCallData()

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



    fun insertbody(articles: MutableList<Email>) {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.adddata(articles)

        }

    }

    val maildata: LiveData<MutableList<Email>> = foodItemRepository.todoListFlow
    val listdata: LiveData<MutableList<Email>> = foodItemRepository.emaillist
    val fullbody: LiveData<MutableList<FullMessage>> = foodItemRepository.fullmessage

}