package com.example.lethireheisenbergcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lethireheisenbergcompose.ui.login.LoginScreen
import com.example.lethireheisenbergcompose.ui.signup.SignUpScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SIGN_UP_SCREEN
    ) {
        composable(route = LOGIN_SCREEN) {
            LoginScreen()
        }
        composable(route = SIGN_UP_SCREEN) {
            SignUpScreen(navController)
        }
    }

}