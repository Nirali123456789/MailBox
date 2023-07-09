package com.aiemail.superemail.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName
import io.github.muhammadmuzammilsharif.interfaces.SectionalUniqueObject

@Entity(tableName = "AllMails")
data class AllMails(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    ) {

    @SerializedName("sender")
    public var sender: String=""

    @SerializedName("senderEmail")
    public var senderEmail: String=""

    var date: String = ""

    @ColumnInfo(name = "thread_id")
    var thread_id: String = ""

    @SerializedName("type")
    var type: String = ""

    @SerializedName("msgid")
    var msgid:String=""

    @SerializedName("author")
    var author: String? = ""

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("content")
    var dataByteArray: ByteArray = byteArrayOf()

    @SerializedName("url")
    var url: String? = null

    @SerializedName("urlToImage")
    var urlToImage: String? = null

    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @SerializedName("content")
    var content: String? = ""


    var ischecked: Boolean = false
    var isselected: Boolean = false

    var body: String = ""


    override fun toString(): String {
        return "Article{" +

                ", author='" + author + '\'' +
                ", title='" + title + '\'' +

                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", dataByteArray='" + dataByteArray + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}'
    }

//    override fun getSection(): String {
//        return "" + id!!
//    }
//
//    override fun getUniqueKey(): Any {
//        return date!!
//    }

    fun isChecked(): Boolean {
        return ischecked;
    }

    override fun equals(other: Any?) = (other is AllMails)
            && date == other.date
            && title == other.title


}
