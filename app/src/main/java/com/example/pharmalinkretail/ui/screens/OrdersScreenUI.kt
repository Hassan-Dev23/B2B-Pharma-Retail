package com.example.pharmalinkretail.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pharmalinkretail.network.apiResponces.DataXX
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreenUI(viewModel: MyViewModel, userId: String, innerPadding: PaddingValues) {
    val state by viewModel.userOrderState.collectAsState()


    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getUserOrders(userId)
                delay(1000)
                isRefreshing = false

            }

        },
//        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            ,
        state = rememberPullToRefreshState()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is State.Error -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text((state as State.Error).message, textAlign = TextAlign.Center)
                    }
                }

                State.Idle -> item {
                    // Optional: show nothing or a placeholder
                }

                State.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is State.Success<*> -> {
                    val orders = (state as State.Success).data.data


                    items(orders) { order ->
                        OrderItem(order)
                    }

                }
            }
        }
    }
}

@Composable
fun OrderItem(order: DataXX) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 👤 User Name
            Text("Customer: ${order.USER_NAME}", style = MaterialTheme.typography.bodyLarge)

            // 📦 Product Info
            Text("Product: ${order.PRODUCT_NAME}", style = MaterialTheme.typography.titleMedium)
            Text("Quantity: ${order.QUANTITY}", style = MaterialTheme.typography.bodyMedium)
            Text("Total: Rs ${order.TOTAL_PRICE}", style = MaterialTheme.typography.bodyMedium)
            Text("Order Date: ${order.ORDER_DATE}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            // 🚚 Order Status
            val statusColor =
                if (order.STATUS.lowercase() == "delivered") Color(0xFF4CAF50) else Color(0xFFFF9800)
            val statusText = order.STATUS.replaceFirstChar { it.uppercase() }

            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

