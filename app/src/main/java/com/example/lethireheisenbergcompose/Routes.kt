package com.example.lethireheisenbergcompose


object Graph {
    const val ROOT = "ROOT_GRAPH"
    const val AUTH = "AUTH_GRAPH"
    const val MAIN = "MAIN_GRAPH"
}


object Screens {
    const val LOGIN_SCREEN = "LoginScreen"
    const val SIGN_UP_SCREEN = "SignUpScreen"
}

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){
    object Home : BottomNavItem("Home", R.drawable.my_favorites_svgrepo_com,"HomeScreen")
    object Profile: BottomNavItem("Profil",R.drawable.sad_sitting_svgrepo_com,"ProfileScreen")
}

