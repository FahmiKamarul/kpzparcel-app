package com.example.kpzparcel

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.kpzparcel.ui.UserLoginForm

enum class ParcelNavigation(@StringRes val title: Int) {
    UserLogin(title = R.string.app_name),

    UserPage(title = R.string.user_page),

}

@Composable
fun ParcelApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Use the current route directly instead of using enum for dynamic route
//    val currentRoute = backStackEntry?.destination?.route ?: ParcelNavigation.UserLogin.name

    val currentRoute = when {
        backStackEntry?.destination?.route?.startsWith("userPage/") == true -> "Your Parcel"
        backStackEntry?.destination?.route?.startsWith("editParcel/") == true -> "Edit Parcel"
        else -> backStackEntry?.destination?.route ?: ParcelNavigation.UserLogin.name
    }

    Scaffold(
        topBar = {
            ParcelAppBar(
                currentRoute = currentRoute,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = ParcelNavigation.UserLogin.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Login Screen
            composable(route = ParcelNavigation.UserLogin.name) {
                UserLoginForm(
                    PageUser = { trackingNumber ->
                        // This navigates to the next screen AND passes the ID in the URL
                        navController.navigate("userPage/$trackingNumber")
                    }
                )
            }

            // 2. User Page (The Result)
            composable(
                route = "userPage/{trackingNumber}"
            ) { backStackEntry ->
                // A. Extract the tracking number from the navigation URL
                val trackingNumber = backStackEntry.arguments?.getString("trackingNumber") ?: ""

                // B. Pass it to the UserPage
                // Note: We do NOT need to pass the ViewModel here manually if UserPage uses viewModel() default
                com.example.kpzparcel.ui.UserPage(trackingNumber = trackingNumber)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelAppBar(
    currentRoute: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentRoute) },  // Use the route name directly here
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}