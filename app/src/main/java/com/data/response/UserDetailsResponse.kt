package com.data.response

data class UserDetailsResponse(
    @com.google.gson.annotations.SerializedName("username") val username: String?,
    @com.google.gson.annotations.SerializedName("email") val email: String?
)
