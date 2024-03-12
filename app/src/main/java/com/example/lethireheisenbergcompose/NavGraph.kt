package com.example.lethireheisenbergcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lethireheisenbergcompose.ui.login.LoginScreen
import com.example.lethireheisenbergcompose.ui.profile.ProfileScreen
import com.example.lethireheisenbergcompose.ui.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    NavHost(
        navController = navController,
        startDestination = SIGN_UP_SCREEN
    ) {
        composable(route = LOGIN_SCREEN) {
            LoginScreen(navController)
        }
        composable(route = SIGN_UP_SCREEN) {
            SignUpScreen(navController)
        }
        composable(route = PROFILE_SCREEN) {
            ProfileScreen(auth = auth, firestore = firestore)
        }
    }

}