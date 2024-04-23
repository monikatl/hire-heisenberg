package com.example.lethireheisenbergcompose.ui.home.hire

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.model.Pay
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider

@Composable
fun HireDialog(category: Service, serviceProvider: ServiceProvider, onDismiss: () -> Unit) {
    val viewModel: HireViewModel = hiltViewModel()
    viewModel.getUser()
    viewModel.setServiceProvider(serviceProvider)

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(
                    text = serviceProvider.figure?.name ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.serviceName,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))
                PaymentKindContainer(viewModel)
                Spacer(modifier = Modifier.height(8.dp))
                StepperButton(viewModel)
            }

        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "WYNAJMIJ")
            }
        }
    )
}

@Composable
fun StepperButton(viewModel: HireViewModel) {
    val hourCounter = viewModel.hourCounter.collectAsState()
    viewModel.updateCost()
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.clickable { viewModel.subHour() },
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_remove_24),
            contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = hourCounter.value.toString() )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            modifier = Modifier.clickable { viewModel.addHour() },
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
            contentDescription = null)
    }
    Spacer(modifier = Modifier.height(8.dp))
    CostContainer(viewModel)
}

@Composable
fun CostContainer(viewModel: HireViewModel) {
    val costState by viewModel.cost.collectAsState()
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = costState.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.dolar_svgrepo_com),
            contentDescription = null
        )
    }
}



@Composable
fun PaymentKindContainer(viewModel: HireViewModel) {
    val options = Pay.entries
    var selectedOption by remember { mutableStateOf(options[Pay.ALL_DOWN.ordinal]) }

    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        viewModel.onOptionSelected(option)
                    }
                )
                Text(
                    text = option.text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}