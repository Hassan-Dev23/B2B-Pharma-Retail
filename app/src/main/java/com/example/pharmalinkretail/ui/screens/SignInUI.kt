package com.example.pharmalinkretail.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmalinkretail.navigation.SignUpScreen
import com.example.pharmalinkretail.navigation.UserCredentials
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInScreenUI(
    navBackStack: SnapshotStateList<Any>,
    viewModel: MyViewModel,
    dataStore: PreferenceDataStore,
    userCredentials: UserCredentials
) {


    var email by remember(viewModel) { mutableStateOf("") }
    var password by remember(viewModel) { mutableStateOf("") }
    var emailError by remember(viewModel) { mutableStateOf<String?>(null) }
    var passwordError by remember(viewModel) { mutableStateOf<String?>(null) }
    val loginUserState by viewModel.loginUserState.collectAsState()
    val scope = rememberCoroutineScope()
    val approvalCheckState by viewModel.approvalStatusState.collectAsState()

    fun validateForm(): Boolean {
        var isValid = true
        emailError = when {
            email.isEmpty() -> "Email is required"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
        passwordError = when {
            password.isEmpty() -> "Password is required"
            password.length < 5 -> "Password must be at least 5 characters"
            else -> null
        }
        isValid = emailError == null && passwordError == null
        return isValid
    }

    // Handle success state and navigation
    LaunchedEffect(loginUserState) {
        when (loginUserState) {

            is State.Success -> {
                val response = (loginUserState as State.Success).data
                if (response.message == "Invalid email or password") {
                    emailError = "Invalid credentials"
                    passwordError = "Invalid credentials"
                } else {

//                    viewModel.checkUserApprovalStatus(userId = response.user_id)
//                    if (approvalCheckState is State.Success) {
//                        val approvalResponse = (approvalCheckState as State.Success).data
//                        if (approvalResponse.status) {
//
//                            navBackStack.add(Nav3Routes.HomeScreen)
////                        navController.navigate(Routes.HomeScreen)
//                        } else {
////                        navController.navigate(Routes.WaitingScreen)
//                            navBackStack.add(Nav3Routes.WaitingScreen)
//                        }
//                    }
                    scope.launch {

                        if (response.user_id.isNotEmpty()) {
                            dataStore.saveUserId(response.user_id)
                        }
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            // Always save credentials on successful login
                            dataStore.saveUserCredentialsInPreference(email.trim(), password.trim())
                        }
                    }
                }
            }

            else -> {}
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if ((userCredentials.email.isEmpty() && userCredentials.password.isEmpty())) {

                // Regular sign-in form content
                Text("Sign In", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text("Email") },
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it) } },
                    modifier = Modifier.padding(8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = { Text("Password") },
                    isError = passwordError != null,
                    supportingText = passwordError?.let { { Text(it) } },
                    modifier = Modifier.padding(8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = {
                        if (validateForm()) {
                            viewModel.loginUser(email, password)


                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && loginUserState !is State.Loading
                ) {
                    if (loginUserState is State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.background
                        )
                    } else {
                        Text("LOGIN")
                    }
                }

                Text(
                    "Don't have an account?",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Sign Up",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable(enabled = loginUserState !is State.Loading) {
//                                navController.navigate(Routes.SignUpScreen)
                            navBackStack.add(SignUpScreen)
                        },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Error dialog
            if (loginUserState is State.Error) {
                AlertDialog(
                    onDismissRequest = { viewModel.loginErrorDismissRequest() },
                    confirmButton = {
                        Button(onClick = { viewModel.loginErrorDismissRequest() }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Error") },
                    text = { Text((loginUserState as State.Error).message) })
            }

            if (approvalCheckState is State.Error) {
                AlertDialog(
                    onDismissRequest = { viewModel.loginErrorDismissRequest() },
                    confirmButton = {
                        Button(onClick = { viewModel.loginErrorDismissRequest() }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Error") },
                    text = { Text((approvalCheckState as State.Error).message) })
            }
        }

//        if (approvalCheckState is State.Loading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f)), // This is your "scrim color"
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        }


    }

}

