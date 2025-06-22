package com.example.pharmalinkretail.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pharmalinkretail.R

import com.example.pharmalinkretail.navigation.OrderCreationScreen
import com.example.pharmalinkretail.viewModel.MyViewModel

@Composable
fun OrderCreationScreenUI(
    key: OrderCreationScreen,
    viewModel: MyViewModel,
    backStack: SnapshotStateList<Any>,
) {
    var showAnimation by remember { mutableStateOf(false) }

    var quantity by remember { mutableStateOf("") }
    var quantityError by remember { mutableStateOf(false) }
    var quantityField by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }



    val price = key.price ?: 0f
    val totalPrice = quantity.toIntOrNull()?.let { it * price } ?: 0f




    if (showAnimation) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.reward))
        val progress by animateLottieCompositionAsState(composition)

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize()
        )

            LaunchedEffect(progress) {
            if (progress == 1f) {
                backStack.removeLastOrNull()
            }
        }

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Order Product",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(text = "Product: ${key.productName ?: "Unknown"}")
            Text(text = "Category: ${key.category ?: "N/A"}")
            Text(text = "Price per item: Rs. $price")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Enter Quantity") },
                isError = quantityError,
                supportingText = {
                    if (quantityError) {
                        Text("This field is Required")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = quantityField,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total Price: Rs. $totalPrice",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Push button to bottom

            Button(
                onClick = {

                    if (quantity.isNotEmpty()) {
                        viewModel.createOrder(
                            userId = key.userId!!,
                            productId = key.productId!!,
                            quantity = quantity.toInt(),
                            totalPrice = totalPrice,
                            productName = key.productName!!,
                            userName = key.userName!!,
                            category = key.category!!,
                            price = price,
                            message = message
                        )
                        quantityField = false
                        message = ""
                        quantity = ""
                        showAnimation = true
                    } else {
                        quantityError = true
                    }

                },
                enabled = quantity.toIntOrNull() != null && quantity.toIntOrNull()!! > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Place Order")
            }
        }
    }
}
