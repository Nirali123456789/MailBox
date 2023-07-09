package com.aiemail.superemail.Rooms

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiemail.superemail.Models.AllMails



@Dao
interface AllEmailDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(datamap:  AllMails)


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertKey(datamap:  List<AllMails>)

    @Query("SELECT type FROM AllMails")
    fun getKey():  Int

    @Query("SELECT * FROM AllMails")
    fun getAllMail(): LiveData< List<AllMails>>


    @Query("Delete  FROM AllMails")
    fun DeleteMails()

}