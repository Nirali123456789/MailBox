package com.example.myapplication.RoomDatabase

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Rooms.AllEmailDao
import com.aiemail.superemail.TypeConverters.MapConverter
import com.aiemail.superemail.TypeConverters.StringArrayConverter
import kotlinx.coroutines.CoroutineScope

@Database(entities = [AllMails::class], version = 3,exportSchema = false)
@TypeConverters(StringArrayConverter::class)
abstract class AllEmailDatabase : RoomDatabase() {

    abstract val categorydao: AllEmailDao
    companion object {
        @Volatile
        public var INSTANCE: AllEmailDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):AllEmailDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AllEmailDatabase::class.java,
                    "food_item_database2"
                ).addCallback(FoodItemCallback(scope))

                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE Contact ADD COLUMN seller_id TEXT NOT NULL DEFAULT ''")
            }
        }
    }
    private class FoodItemCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
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