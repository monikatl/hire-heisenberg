package com.example.lethireheisenbergcompose.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.ui.home.HomeViewModel

@Composable
fun WalletStatusItem(homeViewModel: HomeViewModel = hiltViewModel()) {
    homeViewModel.getWallet()
    val wallet by homeViewModel.wallet.collectAsState(null)

    val walletImg = ImageVector.vectorResource(id = R.drawable.wallet_cash_svgrepo_com)
    val currency = ImageVector.vectorResource(id = R.drawable.dolar_svgrepo_com)
    Icon(imageVector = walletImg, contentDescription = null)
    Spacer(modifier = Modifier.width(16.dp))
    Text(text = wallet?.contents.toString(), fontSize = 18.sp)
    Spacer(modifier = Modifier.width(8.dp))
    Icon(imageVector = currency, contentDescription = null)
    Spacer(modifier = Modifier.width(4.dp))
}