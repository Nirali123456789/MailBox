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
import com.aiemail.superemail.Models.Trash


@Dao
interface TrashDao {
    @Insert()
     fun insertAll(Courses: ArrayList<Trash>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: Trash)

    @Query("SELECT * FROM Trash")
    fun getAllMail(): LiveData<List<Trash>>

    @Query("Delete  FROM Trash")
    fun DeleteMails()



      
}