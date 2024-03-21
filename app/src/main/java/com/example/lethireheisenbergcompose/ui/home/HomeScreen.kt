package com.example.lethireheisenbergcompose.ui.home

/*
* Copyright 2023 Coil Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.lethireheisenbergcompose.MainGraph
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.ui.BottomNavigation
import com.example.lethireheisenbergcompose.ui.WalletStatusItem
import com.example.lethireheisenbergcompose.ui.home.hire.HireDialog
import com.example.lethireheisenbergcompose.ui.home.hire.HireViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(){
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = rememberNavController())
        }
    ) {
        MainGraph()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun HomeContent(homeViewModel: HomeViewModel = hiltViewModel()) {

    Scaffold (topBar = { HomeTopBar() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
                .wrapContentSize(Alignment.Center)
        ) {

            QuoteContainer(homeViewModel)
            JobSearchBar()
            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
               for (service in Service.entries)
                   ServiceProvidersContainer(service, filterServiceProvidersByService(homeViewModel.serviceProviders.value, service))
            }
        }
    }
}

private fun filterServiceProvidersByService(serviceProviders: List<ServiceProvider>, service: Service): List<ServiceProvider> {
    return serviceProviders.filter { it.services.contains(service) }
}

@Composable
fun QuoteContainer(homeViewModel: HomeViewModel) {
    val quote by homeViewModel.quote.collectAsState()
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { homeViewModel.getQuote() }
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 15.dp,
        backgroundColor = MaterialTheme.colorScheme.onBackground,
    ) {

        Column(Modifier.padding(8.dp)) {
            Text(
                text = quote.quote,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = quote.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.align(Alignment.End)
            )
        }

    }
}

@Composable
fun ServiceProvidersContainer(category: Service,  serviceProviders: List<ServiceProvider>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            category.serviceName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center
        )
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
       val services = serviceProviders

        items(services) { serviceProvider ->
            ServiceProviderItem(serviceProvider, category)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(homeViewModel: HomeViewModel = hiltViewModel()) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#ff00ff")),
                        )
                        .clip(CircleShape)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "name")
            }
        },
        actions = { WalletStatusItem() },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSearchBar() {
    val viewModel: HomeViewModel = hiltViewModel()

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val categories by viewModel.categories.collectAsState()

    SearchBar(
        query = searchText,
        onQueryChange = viewModel::onSearchTextChange, 
        onSearch = viewModel::onSearchTextChange, 
        active = isSearching, 
        onActiveChange = { viewModel.onToogleSearch() }, 
        placeholder = { Text(text = "Jakich usług dziś potrzebujesz?")},
        leadingIcon = { Icon(painterResource(id = R.drawable.search_alt_3_svgrepo_com), contentDescription = null)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn {
            items(categories) { category ->
                Text(
                    text = category,
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            top = 4.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
                        .clickable {

                        }
                )
            }
        }
    }
}




@Composable
fun ServiceProviderItem(serviceProvider: ServiceProvider, categogory: Service) {
    val viewModel: HireViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { showDialog = true },
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colorScheme.onBackground,
    ) {

        Column(Modifier.padding(8.dp)) {
           AsyncImage(
                model = serviceProvider.figure.img,
                contentDescription = serviceProvider.figure.name,
                modifier = Modifier
                    .width(150.dp)
                    .height(250.dp)
            )
            serviceProvider.figure.name.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            Text(
                text = serviceProvider.figure.nickname,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            Text(
                text = "${serviceProvider.rate} $",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }
    if (showDialog)
        HireDialog (categogory, serviceProvider) {
            showDialog = false
            viewModel.hireServiceProvider()
        }
}








