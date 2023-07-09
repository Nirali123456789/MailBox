package com.aiemail.superemail.Models

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("resourceName") val resourceName: String,
    @SerializedName("photos") val photos: List<Photo>
    // Add other required fields
)

data class Photo(
    @SerializedName("metadata") val metadata: Metadata,
    @SerializedName("url") val url: String
    // Add other required fields
)

data class Metadata(
    @SerializedName("primary") val primary: Boolean
    // Add other required fields
)