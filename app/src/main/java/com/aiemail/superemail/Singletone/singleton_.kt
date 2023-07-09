package com.aiemail.superemail.Singletone

import com.aiemail.superemail.Models.Email

object singleton_ {
    val singleEmail: Email by lazy{
        Email()
    }

}

