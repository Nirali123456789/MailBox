package com.aiemail.superemail.Rooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.Models.Pin


@Dao
interface EmailDao {
    @Insert()
     fun insertAll(Courses: ArrayList<Email>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: Email)
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertAll(foodItem: AllMails)
//    @Query("SELECT * FROM AllMails")
//    fun getAllMailAll(): LiveData<List<AllMails>>
    @Query("SELECT * FROM Email  WHERE isInbox = 1")
    fun getAllMail(): LiveData<List<Email>>

    @Query("SELECT * FROM Email  WHERE isUnread = 1")
    fun getUnreadMail(): LiveData<List<Email>>

    @Query("Delete  FROM Email")
    fun DeleteMails()

    @Query("SELECT * FROM Email WHERE is_aside = 1")
    fun getAsideItems(): LiveData<List<Email>>

    @Query("SELECT * FROM Email WHERE is_snoozed = 1")
    fun getSnoozeItems(): LiveData<List<Email>>

    @Query("SELECT * FROM Email WHERE sender =:sender_id")
    fun getSnoozeSend(sender_id:String): Email
      
}