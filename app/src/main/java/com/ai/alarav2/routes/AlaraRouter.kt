package com.ai.alarav2.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ai.alarav2.ui.view.chat.AlaraChatComposable
import com.ai.alarav2.ui.view.dashboard.AlaraDashboardComposable
import com.ai.alarav2.ui.view.login.AlaraLoginScreen
import com.ai.alarav2.vm.drawer.AlaraDrawerVm

@Composable
fun AlaraRouter(
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass,
    drawerVm: AlaraDrawerVm
) {

    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = AlaraRoutes.Login,

        ) {
        composable<AlaraRoutes.Chat> {
            AlaraChatComposable(
                windowWidthSizeClass = windowWidthSizeClass,
                drawerVm = drawerVm
            )
        }

        composable<AlaraRoutes.Dashboard> {
            AlaraDashboardComposable(
                windowWidthSizeClass = windowWidthSizeClass,
                onClick = {
                    navController.navigate(AlaraRoutes.Chat)

                })
        }

        composable<AlaraRoutes.Login> {
            AlaraLoginScreen(onClick = {
                navController.navigate(AlaraRoutes.Chat)
            })
        }
    }


}