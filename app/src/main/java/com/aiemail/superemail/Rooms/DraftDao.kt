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


@Dao
interface DraftDao {
    @Insert()
     fun insertAll(Courses: ArrayList<Draft>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: Draft)

    @Query("SELECT * FROM Draft ")
    fun getAllMail(): LiveData<List<Draft>>

    @Query("Delete  FROM Draft")
    fun DeleteMails()

    @Query("SELECT * FROM Draft WHERE is_aside = 1")
    fun getAsideItems(): LiveData<List<Draft>>

    @Query("SELECT * FROM Draft WHERE is_snoozed = 1")
    fun getSnoozeItems(): LiveData<List<Draft>>

    @Query("SELECT * FROM Draft WHERE sender =:sender_id")
    fun getSnoozeSend(sender_id:String): Draft
      
}