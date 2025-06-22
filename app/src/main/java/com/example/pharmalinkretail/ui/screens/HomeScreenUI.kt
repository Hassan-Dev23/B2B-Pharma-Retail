package com.example.pharmalinkretail.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmalinkretail.navigation.SpecificProductScreen
import com.example.pharmalinkretail.network.apiResponces.Data
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    viewModel: MyViewModel,
    navController: SnapshotStateList<Any>,
    padding: PaddingValues,
    dataStore: PreferenceDataStore
) {
    val productListState by viewModel.productListState.collectAsState()
    var userid by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        userid = dataStore.userId.first()

    }

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getAllProducts()
                delay(1000)
                isRefreshing = false

            }

        },
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        state = rememberPullToRefreshState()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (productListState) {
                is State.Loading -> item {

                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }

                }

                is State.Error -> item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            (productListState as State.Error).message,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is State.Success -> {

                    items((productListState as State.Success).data.data) {
                        ProductCard(it, navController, viewModel)
                    }

                }

                State.Idle -> {

                }
            }
        }


//
//        if (productListState is State.Loading) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//        if (productListState is State.Error) {
//            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text((productListState as State.Error).message, textAlign = TextAlign.Center)
//            }
//        }
//        if (productListState is State.Success) {
//
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//
//                items((productListState as State.Success).data.data) {
//                    ProductCard(it, navController, viewModel)
//                }
//
//            }
//
//        }
    }
}


@Composable
fun ProductCard(
    product: Data,
    navController: SnapshotStateList<Any>,
    viewModel: MyViewModel
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.specificProductINFO(product.PRODUCT_ID)
                navController.add(SpecificProductScreen(product.PRODUCT_ID))
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = product.PRODUCT_NAME,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: ${product.CATEGORY}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Price: $${product.PRODUCT_PRICE}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
