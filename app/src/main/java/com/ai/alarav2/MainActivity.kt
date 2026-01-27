package com.ai.alarav2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ai.alarav2.routes.AlaraRouter
import com.ai.alarav2.routes.AlaraRoutes
import com.ai.alarav2.ui.theme.AlaraV2Theme
import com.ai.alarav2.ui.view.components.drawer.AlaraDrawer
import com.ai.alarav2.ui.view.components.drawer.AlaraDrawerContainer
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
            var isDrawerOpened by remember { mutableStateOf(false) }

            AlaraV2Theme {
                AlaraDrawerContainer(isDrawerOpened = isDrawerOpened, onSwipe = {
                    isDrawerOpened = it
                }) {

                    AlaraDrawer(selectedItem = AlaraRoutes.Dashboard, onItemClick = { route ->
                        navController.navigate(route)
                    })

                    AlaraRouter(
                        navController = navController,
                        windowWidthSizeClass = windowSize.widthSizeClass
                    )
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