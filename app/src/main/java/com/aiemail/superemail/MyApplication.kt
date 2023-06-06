/*
 * Created by Inuwa Ibrahim on 17/03/2022, 7:43 PM
 *     https://linktr.ee/Ibrajix
 *     Copyright (c) 2022.
 *     All rights reserved.
 */

package com.aiemail.superemail

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.aiemail.superemail.feature.Repository.fetchRepository
import com.example.myapplication.RoomDatabase.EmailDatabase
import com.aiemail.superemail.feature.utilis.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
public val prefs: Prefs by lazy {
    MyApplication.prefs1!!
}
//@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        var prefs1: Prefs? = null
        lateinit var instance: MyApplication
            private set
    }
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null

private val applicationScope = CoroutineScope(SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        instance=this
        context = applicationContext
        prefs1 = Prefs(applicationContext)
    }
    private val database by lazy { EmailDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { fetchRepository(database.categorydao) }
}

