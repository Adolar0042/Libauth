package dev.adolar0042.libauth.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.adolar0042.libauth.ui.navigation.AppNavigation
import dev.adolar0042.libauth.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()

        Scaffold { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}
