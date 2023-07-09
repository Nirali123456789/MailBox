package com.aiemail.superemail.Models

import com.google.gson.annotations.SerializedName

class Source {
     var id: String? = null

     var name: String? = null



    override fun toString(): String {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}'
    }
}