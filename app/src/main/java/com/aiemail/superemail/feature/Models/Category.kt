package com.aiemail.superemail.feature.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Category")
 class Category(
  @PrimaryKey
  var t_id: String = "00",


   var t_thumb: String = "",

   var t_name: String = "")





