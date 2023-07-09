package com.aiemail.superemail.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.aiemail.superemail.Models.AllMails

import com.aiemail.superemail.Models.Category
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Repository.AllfetchRepository
import com.aiemail.superemail.Repository.PinRepository
import kotlinx.coroutines.*

class PinViewmodel(private val pinRepository: PinRepository) : ViewModel() {

    public val pinlist: MutableLiveData<Pin> = pinRepository.pin_data

    class Factory(private val app: Application, private val repo: PinRepository) :
        ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PinViewmodel(repo) as T
        }

    }


    fun insertpin(pin: Pin) {
        CoroutineScope(Dispatchers.IO).launch {
            pinRepository.adddata(pin)

        }

    }

    fun delete(pin: Pin) {
        CoroutineScope(Dispatchers.IO).launch {
            pinRepository.deletedata(pin)

        }

    }
    fun getArchive():LiveData<List<Pin>>
    {
        return  pinRepository.getArchiveList()
    }

    fun FetchPinList(): LiveData<List<Pin>> {
      return  pinRepository.getPinList()


    }

    fun DeleteMails(pin: Pin) {
        CoroutineScope(Dispatchers.IO).launch {
            pinRepository.deletedata(pin)

        }

    }


    val pindata: LiveData<ArrayList<Pin>> = pinRepository.pinListData


}