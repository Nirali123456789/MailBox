package com.aiemail.superemail.utilis

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer


import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.viewmodel.FullMessagelViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class EmailWorker(context: Context){
//
//    private val fullMessageViewModel: FullMessagelViewmodel by lazy {
//        val repository = (context.applicationContext as MyApplication).fullmessagerepository
//        FullMessagelViewmodel(repository)
//    }
//
//    override fun doWork(): Result  {
//        val senderid = inputData.getString("sender_id")
//            // Use the fullMessageViewModel instance here as needed
//           val fullmessage= fullMessageViewModel.getEmail(senderid!!)
//        Log.d("TAG", ":myObserver "+fullmessage)
////        fullmessage.observeForever(myObserver)
//            // Perform your work here
//
//
//        return  Result.success()
//    }
//    private val myObserver = Observer<FullMessage> { value ->
//        Log.d("TAG", ":myObserver "+value)
//        // Handle the updated value from the ViewModel
//        // This block of code will be executed whenever the LiveData value changes
//    }
}
