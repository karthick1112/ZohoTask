package com.example.test.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test.Retrofit.Constants

@Database(entities = [TblNewsList::class], version = 1)
abstract class OfflineRoomDatabase : RoomDatabase() {
    companion object {
        private var myAppDatabase: OfflineRoomDatabase? = null
        fun getDataBaseInstance(context: Context): OfflineRoomDatabase? {
            if (myAppDatabase == null) {
                myAppDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    OfflineRoomDatabase::class.java,  Constants.OFF_LINE_DB_NAME
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return myAppDatabase
        }
    }

    abstract fun iTblNewsList(): ITblNewsList?
}