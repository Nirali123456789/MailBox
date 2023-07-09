package com.example.myapplication.RoomDatabase

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Pin
import com.aiemail.superemail.Rooms.AllEmailDao
import com.aiemail.superemail.Rooms.PinDao
import com.aiemail.superemail.TypeConverters.MapConverter
import com.aiemail.superemail.TypeConverters.StringArrayConverter
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Pin::class], version = 1,exportSchema = false)
@TypeConverters(StringArrayConverter::class)
abstract class PinDatabase : RoomDatabase() {
    abstract val pinDao: PinDao

    companion object {
        @Volatile
        public var INSTANCE: PinDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):PinDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PinDatabase::class.java,
                    "pin_database"
                ).addCallback(PinItemCallback(scope))

                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }

    }
    private class PinItemCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

//            INSTANCE?.let { foodItemRoomDB ->
//                scope.launch {
//                    // if you want to populate database
//                    // when RoomDatabase is created
//                    // populate here
//                    foodItemRoomDB.categorydao.insert(Category("2","100f","",""))
//                }
//            }
        }
    }
}