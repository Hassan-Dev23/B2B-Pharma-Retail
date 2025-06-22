package com.example.pharmalinkretail.network.apiResponces

data class User(
    val ADDRESS: String,
    val Blocked: Int,
    val Date_of_Creation: String,
    val EMAIL: String,
    val PASSWORD: String,
    val PHONE_NUMBER: String,
    val Pin_CODE: String,
    val USER_ID: String,
    val USER_NAME: String,
    val id: Int,
    val isApproved: Int
)