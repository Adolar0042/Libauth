package dev.adolar0042.libauth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.adolar0042.libauth.ui.detail.DetailScreen
import dev.adolar0042.libauth.ui.home.HomeScreen
import dev.adolar0042.libauth.ui.home.HomeViewModel
import dev.adolar0042.libauth.utility.Constants

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        composable(route = TopLevelDestination.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeScreenUiState by remember { homeViewModel.response }.collectAsState()

            HomeScreen(
                uiState = homeScreenUiState,
                onNavigateClick = { source ->
                    navController.navigate(TopLevelDestination.Detail.withArgs(source))
                }
            )
        }

        composable(route = TopLevelDestination.Detail.route + "/{${Constants.SOURCE}}",
            arguments = listOf(
                navArgument(Constants.SOURCE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString(Constants.SOURCE) ?: return@composable

            DetailScreen(
                source = source,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
