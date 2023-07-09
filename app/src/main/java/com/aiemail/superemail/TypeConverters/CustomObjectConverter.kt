package com.aiemail.superemail.TypeConverters

import android.net.Uri
import androidx.room.TypeConverter
import com.google.api.services.gmail.model.Message
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object CustomObjectConverter {
    @TypeConverter
    fun messageToByteArray(message: Message?): ByteArray? {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(message)
            objectOutputStream.close()
            byteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    fun byteArrayToMessage(bytes: ByteArray?): Message? {
        return try {
            val byteArrayInputStream = ByteArrayInputStream(bytes)
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            objectInputStream.readObject() as Message
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        if (uri!=null)
        return uri.toString()
        else return ""
    }

    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }

    @TypeConverter
    fun fromFileList(fileList: ArrayList<File>): String {
        val stringBuilder = StringBuilder()
        for (file in fileList) {
            stringBuilder.append(file.absolutePath)
            stringBuilder.append(",")
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toFileList(fileListString: String): ArrayList<File> {
        val fileList = ArrayList<File>()
        val filePaths = fileListString.split(",").toTypedArray()
        for (filePath in filePaths) {
            if (filePath.isNotEmpty()) {
                fileList.add(File(filePath))
            }
        }
        return fileList
    }
}