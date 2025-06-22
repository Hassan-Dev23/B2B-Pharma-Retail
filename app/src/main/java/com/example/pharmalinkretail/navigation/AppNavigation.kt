package com.example.pharmalinkretail.navigation

//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.pharmaadminpro.preference.PreferenceDataStore
//import com.example.pharmalinkretail.ui.screens.HomeScreenUI
//import com.example.pharmalinkretail.ui.screens.SignInScreenUI
//import com.example.pharmalinkretail.ui.screens.SignUpScreenUI
//import com.example.pharmalinkretail.ui.screens.SpecificProductScreenUI
//import com.example.pharmalinkretail.ui.screens.UserProfileUI
//import com.example.pharmalinkretail.ui.screens.WaitingScreenUI
//import com.example.pharmalinkretail.viewModel.MyViewModel


data class UserCredentials(
    val email: String, val password: String, val userId: String
)

//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppNavigation(viewModel: MyViewModel, dataStore: PreferenceDataStore) {
////    val navController = rememberNavController()
//
//    // 1. Use StateFlow/State to observe preferences
//    val email by dataStore.email.collectAsState(initial = "")
//    val password by dataStore.password.collectAsState(initial = "")
//    val userId by dataStore.userId.collectAsState(initial = "")
//
//    // 2. Derived state for credentials
//    val userCredentials = remember(email, password, userId) {
//        UserCredentials(email, password, userId)
//    }
//
//    Scaffold(
//        topBar = {
//
//                TopAppBar(
//                    title = { Text("Products") },
//                    actions = {
//                        if (userId.isNotEmpty()) {
//                            IconButton(onClick = {
//                                viewModel.getUserDetails(userId)
//                                navController.navigate(Routes.ProfileScreen)
//                            }) {
//                                Icon(Icons.Default.Person, "User Profile")
//                            }
//                        }
//                    }
//                )
//
//        }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = Routes.SignInScreen
//        ) {
//
//
//            composable<Routes.SignInScreen> {
//
//                SignInScreenUI(navController, viewModel, dataStore, userCredentials)
//
//
//            }
//            composable<Routes.SignUpScreen> {
//                SignUpScreenUI(viewModel)
//            }
//            composable<Routes.WaitingScreen> {
//                WaitingScreenUI(navController)
//            }
//
//            composable<Routes.ProfileScreen> {
//                UserProfileUI(viewModel, innerPadding)
//            }
//            composable<Routes.HomeScreen> {
//                HomeScreenUI(viewModel, navController, innerPadding, dataStore)
//            }
//            composable<Routes.SpecificProductScreen> {
//                SpecificProductScreenUI(
//                    viewModel = viewModel, innerPadding = innerPadding
//                )
//            }
//        }
//    }
//}
//




























//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppNavigation(viewModel: MyViewModel, dataStore: PreferenceDataStore) {
//    val navController = rememberNavController()
//    var emailInPrefs = ""
//    var passInPrefs = ""
//    var userIdInPrefs = ""
//    val scope = rememberCoroutineScope()
//    LaunchedEffect(Unit) {
//        scope.launch {
//            emailInPrefs = dataStore.email.first()
//            passInPrefs = dataStore.password.first()
//            userIdInPrefs = dataStore.userId.first()
//            Log.d("prf", "AppNavigation: $emailInPrefs")
//        }
//    }
//
//
//    val userCredentials: UserCredentials = UserCredentials(emailInPrefs, passInPrefs, userIdInPrefs)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Products") }, actions = {
//                IconButton(onClick = {
//
//                    if (userIdInPrefs.isNotEmpty()) {
//                        viewModel.getUserDetails(userIdInPrefs)
//                        navController.navigate(Routes.ProfileScreen)
//                    }
//
//                }) {
//                    Icon(Icons.Default.Person, "User Profile")
//                }
//            })
//
//        }) { innerPadding ->
//        NavHost(
//            navController = navController, startDestination = Routes.SignInScreen
//        ) {
//
//            composable<Routes.SignInScreen> {
//
//                SignInScreenUI(navController, viewModel, dataStore, userCredentials)
//
//
//            }
//            composable<Routes.SignUpScreen> {
//                SignUpScreenUI(viewModel)
//            }
//            composable<Routes.WaitingScreen> {
//                WaitingScreenUI(navController)
//            }
//
//            composable<Routes.ProfileScreen> {
//                UserProfileUI(viewModel, innerPadding)
//            }
//            composable<Routes.HomeScreen> {
//                HomeScreenUI(viewModel, navController, innerPadding, dataStore)
//            }
//            composable<Routes.SpecificProductScreen> {
//                SpecificProductScreenUI(
//                    viewModel = viewModel, innerPadding = innerPadding
//                )
//            }
//        }
//    }
//}
