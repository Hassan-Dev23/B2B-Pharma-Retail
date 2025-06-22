package com.example.pharmalinkretail.repo

import com.example.pharmalinkretail.network.apiResponces.Approval
import com.example.pharmalinkretail.network.apiResponces.LoginUserResponse
import com.example.pharmalinkretail.network.apiResponces.createUserResponce
import com.example.pharmalinkretail.network.retrofitApi.ApiServiceProvider
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repository {

    fun login(email: String, password: String): Flow<Response<LoginUserResponse>> {
        return flow {
            val response = ApiServiceProvider.ProvideApiService().login(email, password)
            emit(response)
        }
    }

    fun createUser(
        name: String,
        phone: String,
        email: String,
        password: String,
        address: String,
        pinCode: String
    ): Flow<Response<createUserResponce>> {
        return flow {
            val response = ApiServiceProvider.ProvideApiService().createUser(
                name = name,
                phone = phone,
                email = email,
                password = password,
                address = address,
                pinCode = pinCode
            )
            emit(response)
        }
    }


    fun getUserDetails(userId: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().getUserDetails(userId)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }

    }
    fun getAllProductsInRepo() = flow{
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().getAllProducts()
             emit(State.Success(response.body()))
        } catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }
    fun getSpecificProduct(productId: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().getSpecificProduct(productId)
            emit(State.Success(response.body()!!))
        } catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }

    fun approvalStatus(userId: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().approvalStatusCheck(userId)
            emit(State.Success(response.body()!!))
        } catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }


    fun createOrder(
        userId: String,
        productId: String,
        quantity: Int,
        totalPrice: Float,
        productName: String,
        userName: String,
        category: String,
        price: Float,
        message: String
        )= flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().createOrder(
                userId = userId,
                productId = productId,
                quantity = quantity,
                totalPrice = totalPrice,
                productName = productName,
                userName = userName,
                category = category,
                price = price,
                message = message
            )
            if (response.isSuccessful){
                emit(State.Success(response.body()!!))
            }
        }
        catch (e: Exception){
            emit(State.Error(e.toString()))
        }

    }

    fun getUserOrders(userId: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.ProvideApiService().getUserOrders(userId)
            emit(State.Success(response.body()!!))
        } catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }




}

