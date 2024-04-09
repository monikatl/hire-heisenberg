package com.example.lethireheisenbergcompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.lethireheisenbergcompose.ui.category.CategoryContainerScreen
import com.example.lethireheisenbergcompose.ui.home.HomeContent
import com.example.lethireheisenbergcompose.ui.home.HomeScreen
import com.example.lethireheisenbergcompose.ui.login.LoginScreen
import com.example.lethireheisenbergcompose.ui.profile.ProfileScreen
import com.example.lethireheisenbergcompose.ui.signup.SignUpScreen

@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        authGraph(navController)
        composable(Graph.MAIN) {
            HomeScreen()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomNavItem.Home.screen_route
    ) {

        composable(BottomNavItem.Profile.screen_route) {
            ProfileScreen()
        }

        composable("CategoryScreen") {
            CategoryContainerScreen()
        }

        composable(BottomNavItem.Home.screen_route) {
            HomeContent()
        }
    }

}

fun NavGraphBuilder.authGraph(navController: NavHostController,
) {
    navigation (
        route = Graph.AUTH,
        startDestination = Screens.SIGN_UP_SCREEN
    ) {

        composable(route = Screens.LOGIN_SCREEN) {
            LoginScreen(navController)
        }
        composable(route = Screens.SIGN_UP_SCREEN) {
            SignUpScreen(navController)
        }
    }
}