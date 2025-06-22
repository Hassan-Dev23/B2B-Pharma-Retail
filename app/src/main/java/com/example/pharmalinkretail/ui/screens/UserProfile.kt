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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pharmalinkretail.viewModel.MyViewModel
import com.example.pharmalinkretail.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileUI(viewModel: MyViewModel, userId: String, innerPadding: PaddingValues) {

    val state by viewModel.userDetailsState.collectAsState()


    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getUserDetails(userId)
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

                State.Idle -> item{
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                        CircularProgressIndicator()
                    }
                }

                State.Loading -> item{
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                        CircularProgressIndicator()
                    }
                }

                is State.Success<*> -> item{
                    val user = (state as State.Success).data.user

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F7))

                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {

                                // 👤 Header: User Name and Email
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User Icon",
                                        tint = Color(0xFF3F51B5),
                                        modifier = Modifier.size(42.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = user.USER_NAME,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = user.EMAIL,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = Color(0xFFCFD8DC)
                                )


                                Spacer(modifier = Modifier.height(12.dp))

                                // 📝 User Details
                                UserInfoRow(label = "📞 Phone", value = user.PHONE_NUMBER)
                                UserInfoRow(label = "📍 Address", value = user.ADDRESS)
                                UserInfoRow(label = "🔒 PIN Code", value = user.Pin_CODE)
                                UserInfoRow(label = "📅 Created On", value = user.Date_of_Creation)
                                UserInfoRow(label = "🆔 User ID", value = user.USER_ID)

                                Spacer(modifier = Modifier.height(20.dp))

                                // ✅🚫 Status indicators
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    StatusChip(
                                        label = "Blocked",
                                        isActive = user.Blocked == 1,
                                        activeColor = Color.Red
                                    )
                                    StatusChip(
                                        label = "Approved",
                                        isActive = user.isApproved == 1,
                                        activeColor = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UserInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF616161) // Medium gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusChip(label: String, isActive: Boolean, activeColor: Color) {
    val bgColor = if (isActive) activeColor.copy(alpha = 0.25f) else Color(0xFFB0BEC5)
    val textColor = if (isActive) activeColor else Color(0xFF37474F)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}







