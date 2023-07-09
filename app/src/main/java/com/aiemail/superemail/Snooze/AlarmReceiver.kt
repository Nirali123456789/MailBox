package com.aiemail.superemail.Snooze

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get

import com.aiemail.superemail.Activities.DirectComposeActivity
import com.aiemail.superemail.Activities.SendActivity
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.FullMessage
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.utilis.EmailWorker
import com.aiemail.superemail.viewmodel.AllEmailViewmodel
import com.aiemail.superemail.viewmodel.EmailViewmodel
import com.aiemail.superemail.viewmodel.FullMessagelViewmodel



class AlarmReceiver : BroadcastReceiver() {
    var listofmail: ArrayList<FullMessage> = arrayListOf()
    private val TIMEOUT = 1500
    override fun onReceive(context: Context, intent: Intent) {




      //  val viewmodel: FullMessagelViewmodel = ViewModelProvider(viewModelStoreOwner, viewModelFactory).get(FullMessagelViewmodel::class.java)


        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {

            // reset all alarms
        } else {

            var value = intent.getStringExtra("senderid")

            val intent=Intent(context, DirectComposeActivity::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK )
            intent.putExtra("fromsend",true)
            intent.putExtra("senderid",value)
            Log.d("TAG", "receiver: "+value)

            context.startActivity(intent)

           // sendEmail(viewModel, value!!)
        }
    }

    private val myObserver = Observer<FullMessage> { value ->
        Log.d("TAG", ":myObserver "+value)
        // Handle the updated value from the ViewModel
        // This block of code will be executed whenever the LiveData value changes
    }
    private fun sendEmail(context: Context,senderid:String) {
//        val inputData = Data.Builder()
//            .putString("sender_id", senderid)
//            .build()
//
//        val workRequest = OneTimeWorkRequestBuilder<EmailWorker>()
//            .setInputData(inputData)
//            .build()
//        WorkManager.getInstance(context).enqueue(workRequest)


    }




}