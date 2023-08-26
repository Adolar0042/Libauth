package dev.adolar0042.libauth.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.adolar0042.libauth.R
import dev.adolar0042.libauth.ui.components.LoadingIndicator
import dev.adolar0042.libauth.ui.components.VerticalSpacer
import dev.adolar0042.libauth.ui.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onNavigateClick: (source: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            //TODO: Add action
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Sort,
                            contentDescription = stringResource(R.string.sort)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = {
                        //TODO: Add action
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Menu,
                            contentDescription = stringResource(R.string.menu)
                        )
                    }
                    IconButton(
                        onClick = {
                            //TODO: Add action
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            //TODO: Add action
                        }
                    ){
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_action)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is HomeScreenUiState.Loading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is HomeScreenUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        welcomeMessage = uiState.data,
                        onNavigateClick = onNavigateClick
                    )
                }

                is HomeScreenUiState.Error -> {}

                else -> {}
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    welcomeMessage: String,
    onNavigateClick: (source: String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = welcomeMessage)
        VerticalSpacer(size = 16)
        Button(onClick = { onNavigateClick(context.getString(R.string.home_screen)) }) {
            Text(
                text = stringResource(
                    R.string.go_to_screen,
                    TopLevelDestination.Detail.javaClass.simpleName
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success(stringResource(id = R.string.welcome_message)),
        onNavigateClick = {}
    )
}
