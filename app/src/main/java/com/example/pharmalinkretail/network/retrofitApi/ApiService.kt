package com.example.pharmalinkretail.network.retrofitApi

import com.example.pharmalinkretail.network.apiResponces.AllProducts
import com.example.pharmalinkretail.network.apiResponces.Approval
import com.example.pharmalinkretail.network.apiResponces.CreateOrder
import com.example.pharmalinkretail.network.apiResponces.GetUserDetails
import com.example.pharmalinkretail.network.apiResponces.LoginUserResponse
import com.example.pharmalinkretail.network.apiResponces.MyOrders
import com.example.pharmalinkretail.network.apiResponces.ProductInfo
import com.example.pharmalinkretail.network.apiResponces.createUserResponce
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    @FormUrlEncoded
    @POST("createUser")
    suspend fun createUser(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("address") address: String,
        @Field("pin_code") pinCode: String
    ): Response<createUserResponce>


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginUserResponse>


    @FormUrlEncoded
    @POST("getUser")
    suspend fun getUserDetails(
        @Field("user_id") userId:String
    ):Response<GetUserDetails>


    @GET("getAllProducts")
    suspend fun getAllProducts(): Response<AllProducts>

    @FormUrlEncoded
    @POST("getProduct")
    suspend fun getSpecificProduct(
        @Field("product_id") userId:String
    ):Response<ProductInfo>

    @FormUrlEncoded
    @POST("userApprovalStatus")
    suspend fun approvalStatusCheck(
        @Field("user_id") userId:String
    ):Response<Approval>


    @FormUrlEncoded
    @POST("createOrder")
    suspend fun createOrder(
        @Field("user_id") userId: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: Int,
        @Field("total_price") totalPrice: Float,
        @Field("product_name") productName: String,
        @Field("user_name") userName: String,
        @Field("category") category: String,
        @Field("price") price: Float,
        @Field("message") message: String

    ): Response<CreateOrder>


    @FormUrlEncoded
    @POST("getUserOrders")
    suspend fun getUserOrders(
        @Field("user_id") userId:String
    ):Response<MyOrders>


}