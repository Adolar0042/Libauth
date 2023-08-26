package dev.adolar0042.libauth.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dev.adolar0042.libauth.ui.navigation.AppNavigation
import dev.adolar0042.libauth.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()

        Scaffold {
            AppNavigation(
                navController = navController
            )
        }
    }
}
