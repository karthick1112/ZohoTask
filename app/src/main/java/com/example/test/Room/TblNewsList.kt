package com.example.test.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TblNewsList")
class TblNewsList {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var newsid: Int = 0
    var title: String? = ""
    var url: String? = ""
    var image_url: String? =""
    var summary: String? = ""
}