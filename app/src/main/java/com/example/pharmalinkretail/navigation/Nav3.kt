package com.example.pharmalinkretail.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmalinkretail.R
import com.example.pharmalinkretail.ui.screens.HomeScreenUI
import com.example.pharmalinkretail.ui.screens.OrderCreationScreenUI
import com.example.pharmalinkretail.ui.screens.OrdersScreenUI
import com.example.pharmalinkretail.ui.screens.SignInScreenUI
import com.example.pharmalinkretail.ui.screens.SignUpScreenUI
import com.example.pharmalinkretail.ui.screens.SpecificProductScreenUI
import com.example.pharmalinkretail.ui.screens.UserProfileUI
import com.example.pharmalinkretail.ui.screens.WaitingScreenUI
import com.example.pharmalinkretail.viewModel.ConnectivityViewModel
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation3(
    viewModel: MyViewModel,
    dataStore: PreferenceDataStore,
    connectivityViewModel: ConnectivityViewModel
) {

    val approvalCheckState by viewModel.approvalStatusState.collectAsState()
    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val email by dataStore.email.collectAsState(initial = "")
    val password by dataStore.password.collectAsState(initial = "")
    val userId by dataStore.userId.collectAsState(initial = "")
    val userName by dataStore.userName.collectAsState(initial = "")

    // 2. Derived state for credentials
    val userCredentials = remember(email, password, userId) {
        UserCredentials(email, password, userId)
    }

    LaunchedEffect(email) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (userId.isNotEmpty()) {
                viewModel.checkUserApprovalStatus(userId)
            }
        }
    }
    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackBarHostState.showSnackbar("No internet connection")
        }
    }


    val backStack = remember(email) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mutableStateListOf<Any>(HomeScreen)
        } else {
            mutableStateListOf<Any>(SignInScreen)
        }
    }
    val currentScreen = backStack.lastOrNull()

    val topBarTitle = when (currentScreen) {
        is HomeScreen -> "Products"
        is ProfileScreen -> "Profile"
        is SpecificProductScreen -> "Product Details"
        is OrderCreationScreen -> "Place Order"
        is OrdersScreen -> "My Orders"
        else -> "Unknown Screen"
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }, topBar = {
        if (backStack.lastOrNull() != SignInScreen && backStack.lastOrNull() != SignUpScreen) {
            TopAppBar(

                title = { Text(topBarTitle) }, actions = {
                    if (userId.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.getUserDetails(userId)
                            backStack.add(ProfileScreen)
                        }) {
                            Icon(Icons.Default.Person, "User Profile")
                        }
                    }
                    IconButton(onClick = {

                        if (userId.isNotEmpty()) viewModel.getUserOrders(userId)
                        backStack.add(OrdersScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add Contact",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                })
        }

    }


    ) { innerPadding ->

        if (isConnected) NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                rememberSceneSetupNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<SignInScreen> {
                    SignInScreenUI(
                        navBackStack = backStack,
                        viewModel = viewModel,
                        dataStore = dataStore,
                        userCredentials = userCredentials
                    )
                }
                entry<WaitingScreen> {
                    WaitingScreenUI(backStack)
                }
                entry<HomeScreen> {

                    when (approvalCheckState) {
                        is State.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Something Wrong Happened Please try later")
                            }
                        }

                        State.Idle -> {}
                        State.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is State.Success<*> -> {
                            if (userName.isEmpty()) {
                                LaunchedEffect(Unit) {
                                    if (userName.isEmpty()) {
                                        dataStore.saveUserName((approvalCheckState as State.Success).data.userName)
                                    }
                                }
                            }
                            val response = (approvalCheckState as State.Success).data.status
                            if (response) {
                                HomeScreenUI(viewModel, backStack, innerPadding, dataStore)
                            } else {

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Your Profile is Under Approval.")
                                }

                            }
                        }
                    }


                }
                entry<SignUpScreen> {
                    SignUpScreenUI(viewModel)
                }
                entry<ProfileScreen> {
                    UserProfileUI(viewModel,userId, innerPadding)
                }
                entry<SpecificProductScreen> { key->

                    SpecificProductScreenUI(
                        viewModel = viewModel,
                        innerPadding = innerPadding,
                        backStack,
                        userId,
                        userName,key.productId
                    )
                }
                entry<OrderCreationScreen> { key ->

                    OrderCreationScreenUI(key, viewModel, backStack)
                }
                entry<OrdersScreen> {
                    OrdersScreenUI(viewModel, userId,innerPadding)
                }

            }
        )else{
            Dialog(
                onDismissRequest = { /* no-op to block dismiss */ },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false // full width
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "No internet",
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "Uh-oh!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "It seems you’re offline.\nCheck your connection and try again.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = {}) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}