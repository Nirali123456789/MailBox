package com.aiemail.superemail.feature.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.feature.Models.Article
import com.aiemail.superemail.feature.Models.Category
import com.aiemail.superemail.feature.Models.FullMessage
import com.aiemail.superemail.feature.Repository.fetchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel(private val foodItemRepository: fetchRepository) : ViewModel() {


    class Factory(private val app: Application, private val repo: fetchRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CategoryViewModel(repo) as T
        }

    }


    var mutableLiveData: ArrayList<Category>? = arrayListOf()


    fun insertMail() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.getCallData()

        }

    }
    fun getEmailList(): LiveData<List<Article>> {

        return foodItemRepository.getEmails()

    }

    fun insertbody() {
        CoroutineScope(Dispatchers.IO).launch {
            foodItemRepository.adddata()

        }

    }
    val maildata: LiveData<MutableList<Article>> = foodItemRepository.todoListFlow
    val listdata: LiveData<MutableList<Article>> = foodItemRepository.emaillist
    val fullbody:LiveData<MutableList<FullMessage>> =foodItemRepository.fullmessage

}