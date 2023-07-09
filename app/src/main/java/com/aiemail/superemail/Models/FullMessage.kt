package com.aiemail.superemail.Models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.util.ArrayList

@Entity(tableName = "FullMessage")
class FullMessage() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "sender_id")
    var sender_id: String = ""

    @ColumnInfo(name = "sender")
    var sender: String? = ""

    @ColumnInfo(name = "recipient")
    var recipient: String? = ""

    @ColumnInfo(name = "subject")
    var subject: String? = ""

    @ColumnInfo(name = "content")
    var content: String? = ""

    @ColumnInfo(name = "bcc")
    var bcc: String? = ""

    @ColumnInfo(name = "cc")
    var cc: String? = ""


    @ColumnInfo(name = "imageResources")
    var imageResources: ArrayList<File> = arrayListOf()


    @ColumnInfo(name = "fileuri")
    var fileUriString: String = ""

}
