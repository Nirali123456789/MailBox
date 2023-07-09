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
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import com.aiemail.superemail.Repository.AllfetchRepository
import com.aiemail.superemail.Repository.DraftRepository
import com.aiemail.superemail.Repository.FetchRepository
import com.aiemail.superemail.Repository.FullMessageRepository
import com.aiemail.superemail.Repository.ImportantRepository
import com.aiemail.superemail.Repository.PinRepository
import com.aiemail.superemail.Repository.SentRepository
import com.aiemail.superemail.Repository.SpamRepository
import com.aiemail.superemail.Repository.TrashRepository
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PreferenceManager
import com.aiemail.superemail.utilis.Prefs
import com.example.myapplication.RoomDatabase.AllEmailDatabase
import com.example.myapplication.RoomDatabase.EmailDatabase
import com.example.myapplication.RoomDatabase.FullMessaeDatabase
import com.example.myapplication.RoomDatabase.PinDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.*


public val prefs: Prefs by lazy {
    MyApplication.prefs1!!
}

//@HiltAndroidApp
class MyApplication : Application() {
    val NIGHT_MODE = "NIGHT_MODE"
    private val isNightModeEnabled = false

    companion object {
        var prefs1: Prefs? = null
        lateinit var instance: MyApplication


    }

    @SuppressLint("StaticFieldLeak")
    var context: Context? = null

    private val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        prefs1 = Prefs(applicationContext)
        preferenceManager = this.context?.let { PreferenceManager(it) }!!
        preferenceManager.SetUpPreference()
        ModeSetUp()

        if (isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        var theme = preferenceManager.getString("Theme")
        Log.d("TAG", "onCreate:>>> " + theme)

        if (theme.equals("Dark theme")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else if (theme.equals("Light theme")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private fun ModeSetUp() {
        Helpers.setLocal(this.context!!, preferenceManager.getString("Local"))

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateBaseContextLocale(this)
    }
    val languageCodes = arrayOf(
        "en", // English
        "de", // German
        "es", // Spanish
        "fr", // French
        "it", // Italian
        "ja", // Japanese
        "pt", // Portuguese
        "ru", // Russian
        "zh", // Chinese
        "uk"  // Ukrainian
    )
    private fun updateBaseContextLocale(context: Context): Context {
        preferenceManager = context.let { PreferenceManager(it) }!!
        preferenceManager.SetUpPreference()
        val lang = preferenceManager.getString(languageCodes[preferenceManager.getInt("LocalPos")]) // Retrieve the saved language preference
        val locale = Locale(lang)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else {
            updateResourcesLocaleLegacy(context, locale)
        }
    }

    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    private val database by lazy { EmailDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FetchRepository(database.categorydao) }

    private val Alldatabase by lazy { AllEmailDatabase.getDatabase(this, applicationScope) }
    val allrepository by lazy { AllfetchRepository(Alldatabase.categorydao) }


    private val pinDatabase by lazy { PinDatabase.getDatabase(this, applicationScope) }
    val pinrepository by lazy { PinRepository(pinDatabase.pinDao) }

    fun isNightModeEnabled(): Boolean {
        return isNightModeEnabled
    }

    private val fullmessageDatabase by lazy {
        FullMessaeDatabase.getDatabase(
            this,
            applicationScope
        )
    }
    val fullmessagerepository by lazy { FullMessageRepository(fullmessageDatabase.pinDao) }

    val sentRepository by lazy { SentRepository(database.sentdao) }

    val spamRepository by lazy { SpamRepository(database.spamDao) }
    val importantRepository by lazy { ImportantRepository(database.importantDao) }
    val draftRepository by lazy { DraftRepository(database.draftDao) }
    val trashRepository by lazy { TrashRepository(database.trashDao) }


}

