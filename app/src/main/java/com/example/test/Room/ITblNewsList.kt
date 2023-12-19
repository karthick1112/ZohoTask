package com.example.test.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ITblNewsList {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(tbllist: TblNewsList)

    @Query("SELECT * FROM TblNewsList")
    fun getAllNewsList(): List<TblNewsList>

    @Delete
    fun reset(tblSignIns: List<TblNewsList>)

    @Query("SELECT * FROM TblNewsList WHERE newsid LIKE :newsid")
    fun getNewsListbyId(newsid: Int): TblNewsList

}