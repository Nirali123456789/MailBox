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
import com.aiemail.superemail.Models.Sent
import com.aiemail.superemail.Models.Spam


@Dao
interface SentDao {
    @Insert()
     fun insertAll(Courses: ArrayList<Sent>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: Sent)
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertAll(foodItem: AllMails)
//    @Query("SELECT * FROM AllMails")
//    fun getAllMailAll(): LiveData<List<AllMails>>
    @Query("SELECT * FROM Sent")
    fun getAllMail(): LiveData<List<Sent>>


    @Query("Delete  FROM Sent")
    fun DeleteMails()



//    @Query("SELECT * FROM Sent WHERE is_aside = 1")
//    fun getAsideItems(): LiveData<List<Spam>>
//
//    @Query("SELECT * FROM Sent WHERE is_snoozed = 1")
//    fun getSnoozeItems(): LiveData<List<Spam>>
//
//    @Query("SELECT * FROM Sent WHERE sender =:sender_id")
//    fun getSnoozeSend(sender_id:String): Spam
      
}