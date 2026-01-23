package com.ai.alarav2.ui.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ai.alarav2.ui.view.chat.AlaraChatComposable

@Composable
fun AlaraRouter(
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = AlaraRoutes.AlaraChat
    ) {
        composable<AlaraRoutes.AlaraChat> {
            AlaraChatComposable(windowWidthSizeClass = windowWidthSizeClass)
        }
    }


}