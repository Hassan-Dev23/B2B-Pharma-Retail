package com.example.pharmalinkretail.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pharmalinkretail.navigation.OrderCreationScreen
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificProductScreenUI(
    viewModel: MyViewModel,
    innerPadding: PaddingValues,
    backStack: SnapshotStateList<Any>,
    userId: String,
    userName: String,
    productId: String?
) {
    val state by viewModel.specificProductState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                if(productId != null) viewModel.specificProductINFO(productId)
                delay(1000)
                isRefreshing = false

            }

        },
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        state = rememberPullToRefreshState()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is State.Error -> item{
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {
                        Text((state as State.Error).message, textAlign = TextAlign.Center)
                    }
                }

                State.Idle -> {}
                State.Loading -> item{
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

                is State.Success<*> -> item{
                    val product = (state as State.Success).data.data

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // 🛒 Product Info Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FB))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = product.PRODUCT_NAME,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF263238)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "💰 Price: Rs ${product.PRODUCT_PRICE}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF37474F)
                                    )

                                    Text(
                                        text = "📦 Stock: ${product.PRODUCT_QUANTITY} pcs left",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (product.PRODUCT_QUANTITY > 0) Color(0xFF4CAF50) else Color.Red,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = "🆔 Product ID: ${product.PRODUCT_ID}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // 🎯 Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { /* TODO: Share logic */ },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE3F2FD))
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = "Share Icon Button",
                                        tint = Color(0xFF1976D2)
                                    )
                                }

                                Button(
                                    onClick = {
                                        backStack.add(
                                            OrderCreationScreen(
                                                userId = userId,
                                                productId = product.PRODUCT_ID,
                                                productName = product.PRODUCT_NAME,
                                                userName = userName,
                                                category = product.CATEGORY,
                                                price = product.PRODUCT_PRICE.toFloat()
                                            )
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF1976D2
                                        )
                                    )
                                ) {
                                    Text("Place Order", color = Color.White)
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}