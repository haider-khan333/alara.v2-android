package com.ai.alarav2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ai.alarav2.routes.AlaraRouter
import com.ai.alarav2.routes.AlaraRoutes
import com.ai.alarav2.ui.theme.AlaraV2Theme
import com.ai.alarav2.ui.view.components.drawer.AlaraDrawer
import com.ai.alarav2.ui.view.components.drawer.AlaraDrawerContainer
import com.ai.alarav2.ui.view.splashscreen.AlaraSplashScreen
import com.ai.alarav2.vm.drawer.AlaraDrawerVm
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val windowSize = calculateWindowSizeClass(this)
            val drawerViewModel: AlaraDrawerVm = hiltViewModel()

            val showSplashScreen = drawerViewModel.showSplashScreen.collectAsState().value
            Crossfade(
                targetState = showSplashScreen,
                label = "SplashTransition",
                animationSpec = tween(durationMillis = 1000) // Adjust speed (e.g., 700ms)
            ) { isSplash ->
                if (isSplash) {
                    AlaraSplashScreen {
                        drawerViewModel.hideSplashScreen()
                    }
                } else {
                    // The entire App structure fades in together
                    AlaraV2Theme {
                        AlaraDrawerContainer(
                            viewModel = drawerViewModel, drawerContent = {
                                AlaraDrawer(
                                    selectedItem = AlaraRoutes.Dashboard,
                                    onItemClick = { route ->
                                        drawerViewModel.closeDrawer() // Close on click
                                        navController.navigate(route)
                                    }
                                )
                            }) {

                            AlaraRouter(
                                navController = navController,
                                windowWidthSizeClass = windowSize.widthSizeClass,
                                drawerVm = drawerViewModel,
                                startDestination = drawerViewModel.initDestination()

                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlaraV2Theme {
        Greeting("Android")
    }
}