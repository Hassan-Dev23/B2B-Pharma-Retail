package com.example.pharmalinkretail.network.apiResponces

data class CreateOrder(
    val message: String,
    val order_id: String,
    val status: Int
)