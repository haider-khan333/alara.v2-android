package com.ai.alarav2.ui.view.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.alarav2.routes.AlaraRoutes
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.vm.drawer.AlaraDrawerViewModel

@Composable
fun AlaraSettingsScreen(drawerVm: AlaraDrawerViewModel, onLogout: () -> Unit = {}) {
    val openAlertDialog = remember { mutableStateOf(false) }
    AlaraSettingsComposable(
        onClick = {

        },
        onLogout = {
            openAlertDialog.value = true
        }
    )

    when (openAlertDialog.value) {
        true -> {
            AlaraAlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    drawerVm.logout()
                    openAlertDialog.value = false
                    onLogout()
                },
                dialogText = "You'll need to sign in  again to keep using Alara",
                dialogTitle = "Logout?"
            )
        }

        else -> {
            // Do nothing
        }
    }
}

@Composable
private fun AlaraSettingsComposable(onClick: () -> Unit = {}, onLogout: () -> Unit = {}) {
    Scaffold(containerColor = AlaraColors.Background, topBar = {
        AlaraHeader(
            containerColor = AlaraColors.Background,
            isCentered = true,
            content = {
                AlaraText(text = AlaraRoutes.Settings.toString())
            },
            navigationIcon = {
                AlaraIconButton(
                    onClick = {},
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    painter = null,
                    modifier = Modifier.background(
                        color = AlaraColors.Background,
                        shape = RoundedCornerShape(50)
                    ),
                    iconTint = MaterialTheme.colorScheme.onBackground
                )
            },
            actions = {}
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            val thickness = 2.dp
            // Profile Card
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.background(
                        shape = RoundedCornerShape(50),
                        color = AlaraColors.Background
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(10.dp)
                            .align(alignment = Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                AlaraText(
                    text = "Johnny",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {}, colors = ButtonDefaults.buttonColors(
                        containerColor = AlaraColors.Background,

                        ), border = BorderStroke(1.dp, AlaraColors.ImgBg)
                ) {
                    AlaraText(text = "Edit Profile", color = MaterialTheme.colorScheme.onBackground)
                }


            }

            AlaraText(
                text = "Alara Settings",
                color = AlaraColors.MainContent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp, start = 15.dp)
            )

            AlaraSettingsGroup {
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
                Divider(color = AlaraColors.Background, thickness = thickness)
                AlaraSettingsItem(icon = Icons.Outlined.Person, title = "Person")
            }

            AlaraSettingsGroup {
                AlaraSettingsItem(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    title = "Logout",
                    iconTint = Color.Red,
                    textColor = Color.Red,
                    onClick = {
                        onLogout()

                    },
                    trailingContent = {}
                )
            }

        }

    }

}


@Composable
fun AlaraAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String? = null,
    dialogText: String,
    icon: ImageVector? = null,
) {
    AlertDialog(
        icon = {
            if (icon != null) {
                Icon(icon, contentDescription = "Example Icon")
            }
        },
        title = {
            if (dialogTitle != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AlaraText(text = dialogTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        text = {
            AlaraText(text = dialogText, softWrap = true, fontSize = 13.sp)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                AlaraText(text = "Logout", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                AlaraText(text = "Cancel")
            }
        }
    )
}

@Composable
fun AlaraSettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AlaraColors.SettingsCardColors),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 0.dp)) {
            content()
        }
    }
}

@Composable
fun AlaraSettingsItem(
    icon: ImageVector,
    title: String,
    iconTint: Color = AlaraColors.MainContent,
    textColor: Color = AlaraColors.MainContent,
    onClick: () -> Unit = {},
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        AlaraText(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = AlaraColors.MainContent,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAlaraSettingsScreen() {
    AlaraSettingsComposable()
}


