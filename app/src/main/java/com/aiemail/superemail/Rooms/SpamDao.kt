package com.aiemail.superemail.Rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Draft
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Models.Spam


@Dao
interface SpamDao {

    @Insert()
     fun insertAll(Courses: ArrayList<Spam>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: Spam)

    @Query("SELECT * FROM Spam ")
    fun getAllMail(): LiveData<List<Spam>>

    @Query("Delete  FROM Spam")
    fun DeleteMails()

    @Query("SELECT * FROM Spam WHERE is_aside = 1")
    fun getAsideItems(): LiveData<List<Spam>>

    @Query("SELECT * FROM Spam WHERE is_snoozed = 1")
    fun getSnoozeItems(): LiveData<List<Spam>>

    @Query("SELECT * FROM Spam WHERE sender =:sender_id")
    fun getSnoozeSend(sender_id:String): Spam
      
}