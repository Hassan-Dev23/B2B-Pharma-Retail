package com.example.pharmalinkretail.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State

@Composable
fun SignUpScreenUI(viewModel: MyViewModel) {
    val createUserState by viewModel.createUserState.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }



    Scaffold(Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (createUserState) {

                is State.Success -> {

                    var failed by remember { mutableStateOf(false) }
                        if((createUserState as State.Success).data.uuid == "null")
                            failed = true
                    AlertDialog(
                        onDismissRequest = {
                            viewModel.resetCreateUserState()
                            name = ""
                            phone = ""
                            email = ""
                            address = ""
                            password = ""
                            pinCode = ""
                        },
                        title = {
                            if (failed)
                                Text("Failed")
                            else
                                Text("Success")
                        },
                        text = {
                            Column {
                                if (failed)
                                    Text("Profile Creation Failed")
                                else
                                    Text("Profile Created Successfully")
                                Spacer(modifier = Modifier.height(8.dp))
                                if (!failed) {
                                    Text("User Id: ${(createUserState as State.Success).data.uuid}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Message: ${(createUserState as State.Success).data.message}")
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.resetCreateUserState()
                                name = ""
                                phone = ""
                                email = ""
                                address = ""
                                password = ""
                                pinCode = ""


                            }) {
                                Text("OK")
                            }
                        }
                    )
                }

                is State.Loading -> {
                    CircularProgressIndicator()
                }

                is State.Error -> {
                    Text((createUserState as State.Error).message)
                }

                is State.Idle -> {

                    Text(
                        "Sign Up",
                        style = MaterialTheme.typography.headlineMedium,
                        textDecoration = MaterialTheme.typography.headlineMedium.textDecoration
                    )

                    Spacer(modifier = Modifier.height(16.dp))



                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Name") },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Phone") },
                        label = { Text("Phone") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Email") },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text("Address") },
                        label = { Text("Address") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password") },
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        placeholder = { Text("Pin Code") },
                        label = { Text("Pin Code") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || phone.isBlank() || email.isBlank() || password.isBlank() || address.isBlank() || pinCode.isBlank()) {
                                viewModel.resetCreateUserState()
                                return@Button
                            }
                            viewModel.createUser(
                                name = name,
                                phone = phone,
                                email = email,
                                password = password,
                                address = address,
                                pinCode = pinCode
                            )
                        }
                    ) {
                        Text(text = "Sign Up")
                    }
                }

            }


        }
    }
}