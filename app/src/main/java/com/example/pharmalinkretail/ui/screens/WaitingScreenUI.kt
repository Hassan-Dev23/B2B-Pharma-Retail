package com.example.pharmalinkretail.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun WaitingScreenUI(
    navController: SnapshotStateList<Any>
) {


    if (false) {


        CircularProgressIndicator()
//        // Display reward animation
//        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.reward))
//        val progress by animateLottieCompositionAsState(composition)
//
//        LottieAnimation(
//            composition = composition,
//            progress = { progress },
//            modifier = Modifier.fillMaxSize()
//        )
//
//        // Navigate to HomeScreen after animation completes
//        LaunchedEffect(progress) {
//            if (progress == 1f) {
////                navController.navigate(Routes.HomeScreen) {
////                    popUpTo(Routes.WaitingScreen) { inclusive = true }
////                }
//                navController.add(Nav3Routes.HomeScreen)
//                navController.remove(Nav3Routes.WaitingScreen)
//            }
//        }
    } else {
        // Display waiting screen UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Waiting for approval...",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}







