package com.example.lethireheisenbergcompose.ui.profile


import android.annotation.SuppressLint

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.Operation
import com.example.lethireheisenbergcompose.model.OperationType
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.ui.home.HomeViewModel
import com.example.lethireheisenbergcompose.ui.home.hire.HireViewModel
import com.example.lethireheisenbergcompose.utils.showFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user = profileViewModel.user.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserDetailsContainer(user)
        WalletContainer()
        PendingHireContainer(profileViewModel)
        HireLastHistoryContainer()
    }

}

@Composable
fun UserDetailsContainer(user: State<User?>) {
    BoxWithConstraints(
        modifier = Modifier
            .systemBarsPadding()
    ) {
        Surface {
            Column {
                ProfileHeader(
                    user.value,
                    this@BoxWithConstraints.maxHeight
                )
                user.value?.name?.let { Text(text = it) }
                user.value?.email?.let { Text(text = it) }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    data: User? = null,
    containerHeight: Dp
) {

    Image(
        modifier = Modifier
            .heightIn(max = containerHeight / 2)
            .padding(
                end = 16.dp
            )
            .clip(CircleShape),
        painter = painterResource(id = R.drawable.sad_sitting_svgrepo_com),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WalletContainer(homeViewModel: HomeViewModel = hiltViewModel(), profileViewModel: ProfileViewModel = hiltViewModel()) {
    homeViewModel.getWallet()

    val wallet by homeViewModel.wallet.collectAsState(initial = null)


    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    val setShowHistory = remember { mutableStateOf(false) }

    val (enteredNumber, setEnteredNumber) = remember { mutableStateOf(0.0) }

    val onNumberEntered: (Double) -> Unit = { number ->
        setEnteredNumber(number)
        profileViewModel.inputMoney(number)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column (modifier = Modifier.fillMaxWidth()) {
            Row (
                modifier = Modifier.padding(4.dp)
            ){
                Icon(
                    painter = painterResource(id = R.drawable.wallet_cash_svgrepo_com),
                    modifier = Modifier
                        .padding(8.dp),
                    contentDescription = "wallet"
                )
                Text(
                    text = "Portfel",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                wallet?.contents?.let {
                    Text(
                        text = it.toString(),
                        fontSize = 35.sp,
                        color = if(it >= 0.0) Color.Black else Color.Red
                    )
                }
                Icon(
                    painter = painterResource(id =
                    when(wallet?.currency?.name) {
                        "USD" -> R.drawable.dolar_svgrepo_com
                        "PLN" -> R.drawable.z_
                        "EUR" -> R.drawable.pay_money_svgrepo_com
                        else -> R.drawable.dolar_svgrepo_com
                    }),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { profileViewModel.changeCurrency() },
                    contentDescription = "currency"
                )
            }

            Row (modifier = Modifier.align(Alignment.End)){
                Icon(
                    painter = painterResource(id = R.drawable.pay_money_svgrepo_com),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { setShowDialog(true) },

                    contentDescription = "input"
                )
                Spacer(modifier = Modifier.size(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.history_svgrepo_com),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { setShowHistory.value = !setShowHistory.value },
                    contentDescription = "input"
                )
            }

            if(setShowHistory.value)
                OperationsHistoryList()

            NumberInputDialog(
                showDialog = showDialog,
                onDismiss = { setShowDialog(false) },
                onNumberEntered = onNumberEntered
            )
        }
    }
    LaunchedEffect (wallet) {

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OperationsHistoryList(profileViewModel: ProfileViewModel = hiltViewModel()) {
    profileViewModel.getOperations()
    val history = profileViewModel.operationHistory.collectAsState()
    val setShowAllHistory = remember { mutableStateOf(false) }

    Column {
        OutlinedButton(onClick = { setShowAllHistory.value = !setShowAllHistory.value} ) {
            Text(text = if(setShowAllHistory.value) "Ukyryj" else "Pokaż pełną historię")
        }
        Spacer(modifier = Modifier.size(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            items(
                if(setShowAllHistory.value)
                    history.value
                else history.value.take(3)
            ) { operation ->
                OperationItem(operation)
            }
        }
    }
    LaunchedEffect (history.value){

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OperationItem(operation: Operation) {
    Column (
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .border(1.dp, if(operation.type == OperationType.DEPOSIT) Color.Green else  Color.Red, CircleShape)
            .padding(16.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id =
                    when(operation.type) {
                        OperationType.DEPOSIT -> R.drawable.money_deposit_money_deposit_account_svgrepo_com
                        OperationType.DRAW -> R.drawable.cash_payment_pay_money_cash_svgrepo_com
            }) , contentDescription = null )
            Text(
                text = operation.date.showFormat(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = operation.amount.toString() + " $",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun HireLastHistoryContainer(profileViewModel: ProfileViewModel = hiltViewModel()) {
    val hires by profileViewModel.historyHires.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "\uD83C\uDF3F  Ostatnie zlecenia",
            style = MaterialTheme.typography.bodyLarge
        )
    }
    LazyColumn(
        modifier = Modifier.size(400.dp, 400.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(hires) { hire ->
            HireLastHistoryItem(hire)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PendingHireContainer(profileViewModel: ProfileViewModel) {
    profileViewModel.getHires()
    val hires by profileViewModel.pendingHires.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "\uD83C\uDF3F  W trakcie",
            style = MaterialTheme.typography.bodyLarge
        )
    }
        LazyRow(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(hires) { hire ->
                PendingHireItem(hire)
            }
        }

    LaunchedEffect (hires) {

    }
}


@Composable
fun HireLastHistoryItem(hire: Hire) {
    val hireViewModel: HireViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = hire.serviceProvider.figure?.img,
                    contentDescription = hire.serviceProvider.figure?.name,
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                )
                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = hire.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                    hire.serviceProvider.figure?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
                Text(
                    text = hire.duration.hours.toString() + " h",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = hire.duration.amount.toString() + " $",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            Row (modifier = Modifier.fillMaxWidth()) {
                FilledTonalButton (text = "Ponów") {
                    showDialog = true
                }
            }

        }

        ConfirmDialog(
            showDialog = showDialog,
            title = "Czy chcesz ponownie zatrudnić postać?",
            onAccept = { hireViewModel.rehire(hire) },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun PendingHireItem(hire: Hire, profileViewModel: ProfileViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = hire.serviceProvider.figure?.img,
                contentDescription = hire.serviceProvider.figure?.name,
                modifier = Modifier
                    .width(150.dp)
                    .height(250.dp)
            )
            Text(
                text = hire.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            hire.serviceProvider.figure?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            //LinearDeterminateIndicator(hire.duration.hourCounter)
        }

        FilledTonalButton (text = "Zwolnij") { showDialog = true }

        val localContext = LocalContext.current
        ConfirmDialog(
            showDialog = showDialog,
            title = "Czy na pewno chcesz zwolnić postać?",
            onAccept = { profileViewModel.endJob(localContext, hire) },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun FilledTonalButton(text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(onClick = { onClick() }) {
            Text(text)
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LinearDeterminateIndicator(duration: Int) {
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (loading) {
            scope.launch {
                loadProgress (duration) { progress ->
                    currentProgress = progress
                }
                loading = false
            }
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(duration: Int, updateProgress: (Float) -> Unit) {
    for (i in 1..duration) {
        updateProgress(i.toFloat() / 1)
        delay(10)
    }
}

@Composable
fun SettingsContainer() {

}

@Composable
fun NumberInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onNumberEntered: (Double) -> Unit
) {
    // Stan przechowujący wprowadzaną liczbę
    val (numberText, setNumberText) = remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Enter a Number") },
            text = {
                TextField(
                    value = numberText,
                    onValueChange = { setNumberText(it) },
                    label = { Text("Number") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val number = numberText.toDoubleOrNull()
                        if (number != null) {
                            onNumberEntered(number)
                            onDismiss()
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

    @Composable
    fun ConfirmDialog(
        showDialog: Boolean,
        title: String,
        onAccept: () -> Unit,
        onDismiss: () -> Unit
    ) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(title) },
                confirmButton = {
                    Button(
                        onClick = {
                            onAccept()
                            onDismiss()
                        }
                    ) {
                        Text("TAK")
                    }
                },
                dismissButton = {
                    Button(onClick = { onDismiss() }) {
                        Text("NIE")
                    }
                }
            )
        }
    }
