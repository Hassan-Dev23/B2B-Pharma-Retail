package com.example.pharmalinkretail.viewModel

import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmalinkretail.network.apiResponces.AllProducts
import com.example.pharmalinkretail.network.apiResponces.Approval
import com.example.pharmalinkretail.network.apiResponces.CreateOrder
import com.example.pharmalinkretail.network.apiResponces.GetUserDetails
import com.example.pharmalinkretail.network.apiResponces.LoginUserResponse
import com.example.pharmalinkretail.network.apiResponces.MyOrders
import com.example.pharmalinkretail.network.apiResponces.ProductInfo
import com.example.pharmalinkretail.network.apiResponces.createUserResponce
import com.example.pharmalinkretail.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val repository = Repository()
    private val _createUserState = MutableStateFlow<State<createUserResponce>>(State.Idle)
    val createUserState = _createUserState.asStateFlow()
    private val _loginUserState = MutableStateFlow<State<LoginUserResponse>>(State.Idle)
    val loginUserState = _loginUserState.asStateFlow()
    val loginErrorDismissRequest = { _loginUserState.value = State.Idle }
    private val _userDetailsState = MutableStateFlow<State<GetUserDetails>>(State.Idle)
    val userDetailsState = _userDetailsState.asStateFlow()
    private val _productListState = MutableStateFlow<State<AllProducts>>(State.Idle)
    val productListState = _productListState.asStateFlow()
    private val _specificProductState = MutableStateFlow<State<ProductInfo>>(State.Idle)
    val specificProductState = _specificProductState.asStateFlow()
    private val _approvalStatusState = MutableStateFlow<State<Approval>>(State.Idle)
    val approvalStatusState = _approvalStatusState.asStateFlow()
    private val _orderCreationState = MutableStateFlow<State<CreateOrder>>(State.Idle)
    val orderCreationState = _orderCreationState.asStateFlow()
    private val _userOrdersState = MutableStateFlow<State<MyOrders>>(State.Idle)
    val userOrderState = _userOrdersState.asStateFlow()

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllProductsInRepo().collect {
//                when (it) {
//                    is State.Error -> _productListState.value = State.Error(it.message)
//                    State.Idle -> _productListState.value = State.Idle
//                    State.Loading -> _productListState.value = State.Loading
//                    is State.Success<*> -> _productListState.value =
//                        State.Success(it.data as AllProducts)
//                }
//            }
//        }
        getAllProducts()


    }


    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllProductsInRepo().collect {
                when (it) {
                    is State.Error -> _productListState.value = State.Error(it.message)
                    State.Idle -> _productListState.value = State.Idle
                    State.Loading -> _productListState.value = State.Loading
                    is State.Success<*> -> _productListState.value =
                        State.Success(it.data as AllProducts)
                }
            }
        }
    }

    fun createUser(
        name: String,
        phone: String,
        email: String,
        password: String,
        address: String,
        pinCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _createUserState.value = State.Loading // Set loading state
            repository.createUser(
                name = name,
                phone = phone,
                email = email,
                password = password,
                address = address,
                pinCode = pinCode
            ).collect { response ->
                if (response.isSuccessful) {
                    _createUserState.value = State.Success(response.body()!!)
                } else {
                    _createUserState.value = State.Error(response.message())
                }
            }
        }
    }


    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginUserState.value = State.Loading // Set loading state

            repository.login(email, password).collect { response ->
                if (response.isSuccessful) {
                    _loginUserState.value = State.Success(response.body()!!)
                } else {
                    _loginUserState.value = State.Error(response.message())
                }
            }
        }
    }


    @Composable
    fun WaitingScreen() {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI("android.resource://${context.packageName}/raw/waiting".toUri())
                    setOnPreparedListener { it.isLooping = true }
                    start()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }


    fun resetCreateUserState() {
        _createUserState.value = State.Idle
    }


    fun getUserDetails(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserDetails(userId).collect {
                when (it) {
                    is State.Error -> _userDetailsState.value = State.Error(it.message)
                    State.Idle -> _userDetailsState.value = State.Idle
                    State.Loading -> _userDetailsState.value = State.Loading
                    is State.Success<*> -> _userDetailsState.value =
                        State.Success((it as State.Success).data.body()!!)
                }
            }
        }
    }

    fun specificProductINFO(productID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSpecificProduct(productID).collect {
                when (it) {
                    is State.Error -> _specificProductState.value = State.Error(it.message)
                    State.Idle -> _specificProductState.value = State.Idle
                    State.Loading -> _specificProductState.value = State.Loading
                    is State.Success<*> -> _specificProductState.value =
                        State.Success((it as State.Success).data)
                }
            }
        }
    }

    fun checkUserApprovalStatus(userId: String) {
        viewModelScope.launch {
            repository.approvalStatus(userId).collect {
                when (it) {
                    is State.Error -> _approvalStatusState.value = State.Error(it.message)
                    State.Idle -> _approvalStatusState.value = State.Idle
                    State.Loading -> _approvalStatusState.value = State.Loading
                    is State.Success<*> -> _approvalStatusState.value =
                        State.Success((it as State.Success).data)
                }
            }
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
    ) {
        viewModelScope.launch {
            repository.createOrder(userId,productId,quantity,totalPrice,productName,userName,category,price,message).collect {
                when (it) {
                    is State.Error -> _orderCreationState.value = State.Error(it.message)
                    State.Idle -> _orderCreationState.value = State.Idle
                    State.Loading -> _orderCreationState.value = State.Loading
                    is State.Success<*> -> _orderCreationState.value =
                        State.Success((it as State.Success).data)
                }
            }
        }

    }


    fun getUserOrders(userId: String) {
        viewModelScope.launch {
            repository.getUserOrders(userId).collect {
                when (it) {
                    is State.Error -> _userOrdersState.value = State.Error(it.message)
                    State.Idle -> _userOrdersState.value = State.Idle
                    State.Loading -> _userOrdersState.value = State.Loading
                    is State.Success<*> -> _userOrdersState.value =
                        State.Success((it as State.Success).data)
                }
            }
        }
    }


}


sealed class State<out T> {
    data object Idle : State<Nothing>()
    data object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
}