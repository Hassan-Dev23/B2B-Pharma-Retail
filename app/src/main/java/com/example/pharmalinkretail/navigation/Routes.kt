package com.example.pharmalinkretail.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object SignUpScreen

    @Serializable
    data object SignInScreen

    @Serializable
    data object WaitingScreen

    @Serializable
    data object ProfileScreen

    @Serializable
    data object HomeScreen

    @Serializable
    data object SpecificProductScreen

}



@Serializable
data object SignUpScreen : NavKey

@Serializable
data object SignInScreen : NavKey

@Serializable
data object WaitingScreen : NavKey

@Serializable
data object ProfileScreen : NavKey

@Serializable
data object HomeScreen : NavKey

@Serializable
data class SpecificProductScreen(val productId: String ? = null) : NavKey

@Serializable
data class OrderCreationScreen(
    val userId : String? = null,
    val productId: String? = null,
    val quantity: Int? = null,
    val totalPrice: Float? = null,
    val productName: String? = null,
    val userName: String? = null,
    val category: String? = null,
    val price: Float? = null,
    val message: String? = null
) : NavKey

@Serializable
data object OrdersScreen : NavKey