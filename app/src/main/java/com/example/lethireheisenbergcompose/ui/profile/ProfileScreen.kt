package com.example.lethireheisenbergcompose.ui.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val user = profileViewModel.user.collectAsState(initial = null)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserDetailsContainer(user)
        WalletContainer(profileViewModel)
        PendingHireContainer()
        HireLastHistoryContainer()
    }

}

@Composable
fun UserDetailsContainer(user: State<User?>, nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()) {
    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = Modifier
            .nestedScroll(nestedScrollInteropConnection)
            .systemBarsPadding()
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState),
            ) {
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
        painter = painterResource(id = R.drawable.icons8_google),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun WalletContainer(profileViewModel: ProfileViewModel) {

    val user = profileViewModel.user.collectAsState(initial = null)

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

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
                Text(
                    text = user.value?.wallet?.contents?.toString() ?: "0.0",
                    fontSize = 35.sp
                )
                Icon(
                    painter = painterResource(id =
                    when(user.value?.wallet?.currency?.name) {
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
            }

            NumberInputDialog(
                showDialog = showDialog,
                onDismiss = { setShowDialog(false) },
                onNumberEntered = onNumberEntered
            )
        }

    }
}

@Composable
fun HireLastHistoryContainer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 25.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "\uD83C\uDF3F  Ostatnie zlecenia",
            style = MaterialTheme.typography.bodyLarge
        )
    }
    LazyRow(
        modifier = Modifier.size(350.dp, 400.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        val hires: List<Hire> = listOf(
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            )
        )
        items(hires) { hire ->
            HireLastHistoryItem(hire)
        }
    }
}

@Composable
fun PendingHireContainer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
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
            .height(300.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {

        val hires: List<Hire> = listOf(
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            ),
//            Hire("Nauka gotowania",
//                Service.COOK,
//                ServiceProvider(Figure(1,"Walter White", "...", "image", "Heisenberg"), 56.25, true ),
//                User(),
//                Duration(5, 5*56.25),
//                HireStatus.END
//            )
        )
        items(hires) { hire ->
            PendingHireItem(hire)
        }
    }
}


@Composable
fun HireLastHistoryItem(hire: Hire) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.onBackground
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.dolar_svgrepo_com),
                contentDescription = hire.serviceProvider.figure.name,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit,
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = hire.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                hire.serviceProvider.figure.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        }
    }
}

@Composable
fun PendingHireItem(hire: Hire) {
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
            Image(
                painter = painterResource(id = R.drawable.dolar_svgrepo_com),
                contentDescription = hire.serviceProvider.figure.name,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = hire.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            hire.serviceProvider.figure.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            LinearDeterminateIndicator()
        }
    }
}

@Composable
fun LinearDeterminateIndicator() {
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false // Reset loading when the coroutine finishes
            }
        }, enabled = !loading) {
            Text("Zwolnij")
        }

        if (loading) {
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
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