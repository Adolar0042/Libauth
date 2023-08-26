package dev.adolar0042.libauth.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.adolar0042.libauth.R
import dev.adolar0042.libauth.ui.components.LoadingIndicator
import dev.adolar0042.libauth.ui.components.VerticalSpacer
import dev.adolar0042.libauth.ui.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onNavigateClick: (source: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
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
                            viewModel.toggleMenu()
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
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_action)
                        )
                    }
                }
            )
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                visible = viewModel.menuState,
                enter = expandHorizontally(
                    animationSpec = tween(
                        300,
                        easing = LinearOutSlowInEasing
                    ), expandFrom = Alignment.CenterHorizontally
                ),
                exit = fadeOut() + shrinkHorizontally(
                    animationSpec = tween(
                        200,
                        easing = LinearOutSlowInEasing
                    ), shrinkTowards = Alignment.CenterHorizontally
                )
            ) {
                Divider()
            }
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
                        onNavigateClick = onNavigateClick,
                        viewModel = viewModel
                    )
                }

                is HomeScreenUiState.Error -> {}

                else -> {}
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    welcomeMessage: String,
    onNavigateClick: (source: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false,
            confirmValueChange = {
                when (it) {
                    SheetValue.Hidden -> {
                        viewModel.menuState = false
                    }

                    else -> {}
                }
                true
            }
        )
    )
    LaunchedEffect(viewModel.menuState) {
        if (viewModel.menuState) {
            scaffoldState.bottomSheetState.partialExpand()
        } else {
            scaffoldState.bottomSheetState.hide()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 200.dp,
        sheetTonalElevation = 3.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings)) },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    },
                    tonalElevation = 5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .clickable {

                        }
                )
            }
        }
    ) {
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
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success(stringResource(id = R.string.welcome_message)),
        onNavigateClick = {}
    )
}
