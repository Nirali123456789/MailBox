package com.aiemail.superemail.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.github.muhammadmuzammilsharif.interfaces.SectionalUniqueObject

@Entity(tableName = "Trash")
data class Trash(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : SectionalUniqueObject<String> {

    @ColumnInfo(name = "is_snoozed")
    var isSnoozed: Boolean = false


    @ColumnInfo(name = "isInbox")
    var isInbox: Boolean = false

    @ColumnInfo(name = "thread_id")
    var thread_id: String = ""

    @ColumnInfo(name = "is_aside")
    var isAside: Boolean = false

    @SerializedName("sender")
    public var sender: String=""

    @SerializedName("senderEmail")
    public var senderEmail: String=""

    var type: Int = 1
    @SerializedName("msgid")
    var msgid:String=""

    @SerializedName("author")
    var author: String? = ""

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("content")
     var dataByteArray:ByteArray = byteArrayOf()

    @SerializedName("url")
    var url: String? = null

    @SerializedName("urlToImage")
    var urlToImage: String? = null

    @SerializedName("publishedAt")
    var publishedAt: String? = null

    @SerializedName("content")
    var content: String? =""

    @ColumnInfo(name = "address_lines")
    var imagedata= arrayOf<String>()


    @ColumnInfo(name = "snooze_id")
    var snooze_id: String=""

    var date: String? = null
    var ischecked: Boolean = false
    var isselected: Boolean = false


    var body:String=""

    fun setimagedata( data: Array<String>)
    {
        imagedata=data
    }

    fun getimagedata(): Array<String> {
        return imagedata
    }

    override fun toString(): String {
        return "Article{" +

                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", imagedata='" + imagedata + '\'' +
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

    override fun getSection(): String {
        return "" + id!!
    }

    override fun getUniqueKey(): Any {
        return date!!
    }

    fun isChecked(): Boolean {
        return ischecked;
    }

    override fun equals(other: Any?)
            = (other is Trash)
            && date == other.date
            && title == other.title

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + id
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + dataByteArray.contentHashCode()
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (urlToImage?.hashCode() ?: 0)
        result = 31 * result + (publishedAt?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + imagedata.hashCode()
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + ischecked.hashCode()
        result = 31 * result + isselected.hashCode()
        result = 31 * result + body.hashCode()
        return result
    }
}
