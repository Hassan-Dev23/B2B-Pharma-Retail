package com.example.pharmalinkretail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmaadminpro.utils.AndroidConnectivityObserver
import com.example.pharmalinkretail.navigation.Navigation3
import com.example.pharmalinkretail.ui.theme.PharmaLinkRetailTheme
import com.example.pharmalinkretail.viewModel.ConnectivityViewModel
import com.example.pharmalinkretail.viewModel.MyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val dataStore = PreferenceDataStore(context)
            val viewModel = viewModels<MyViewModel>().value
            val connectivityViewModel = viewModel<ConnectivityViewModel> {
                ConnectivityViewModel(
                    connectivityObserver = AndroidConnectivityObserver(
                        context = applicationContext
                    )
                )
            }
            PharmaLinkRetailTheme {


//                AppNavigation(viewModel, dataStore)
                Navigation3(
                    viewModel = viewModel,
                    dataStore = dataStore,
                    connectivityViewModel = connectivityViewModel,
                )




            }
        }
    }
}

