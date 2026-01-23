package com.ai.alarav2.ui.routes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ai.alarav2.ui.view.chat.AlaraChatComposable
import com.ai.alarav2.ui.view.dashboard.AlaraDashboardComposable

@Composable
fun AlaraRouter(
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = AlaraRoutes.AlaraDashboard,

        ) {
        composable<AlaraRoutes.AlaraChat> {
            AlaraChatComposable(windowWidthSizeClass = windowWidthSizeClass)
        }

        composable<AlaraRoutes.AlaraDashboard> {
            AlaraDashboardComposable(
                windowWidthSizeClass = windowWidthSizeClass,
                onClick = {
                    navController.navigate(AlaraRoutes.AlaraChat)

                })
        }
    }


}