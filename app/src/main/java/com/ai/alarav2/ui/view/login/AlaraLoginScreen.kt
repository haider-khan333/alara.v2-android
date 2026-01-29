package com.ai.alarav2.ui.view.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ai.alarav2.ui.theme.AlaraButtonColor
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.vm.login.AlaraLoginUiState
import com.ai.alarav2.vm.login.AlaraLoginViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding


@Composable
fun AlaraLoginScreen(
    onClick: () -> Unit = {},
) {

    val loginViewModel: AlaraLoginViewModel = hiltViewModel()
    val loginState = loginViewModel.loginState.collectAsState().value
    val email = loginViewModel.email.collectAsState().value
    val password = loginViewModel.password.collectAsState().value
    val isContinueEnabled = loginViewModel.isContinueEnabled.collectAsState().value
    val typewriterTaglines = loginViewModel.typewriterTaglines.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        when (loginState) {
            is AlaraLoginUiState.Success -> {
                onClick()
                loginViewModel.resetLoginState()
            }

            is AlaraLoginUiState.Error -> {
                Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()

                loginViewModel.resetLoginState()
            }

            else -> { /* Do nothing for Idle/Loading */
            }
        }
    }
    AlaraLoginComposable(
        email = email,
        password = password,
        isContinueEnabled = isContinueEnabled,
        isLoading = loginState is AlaraLoginUiState.Loading,
        onEmailChange = loginViewModel::updateEmail,
        onPasswordChange = loginViewModel::updatePassword,
        onLoginClick = {
            loginViewModel.login(
                email,
                password
            )
        },
        // Pass the fixed list from VM or a static list
        taglines = typewriterTaglines
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlaraLoginComposable(
    email: String,
    password: String,
    isContinueEnabled: Boolean,
    isLoading: Boolean,
    taglines: List<String>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val imeVisible = WindowInsets.isImeVisible
    Scaffold(
        bottomBar = {
            if (!imeVisible) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)


                    ) {
                        AlaraText(
                            text = "Powered by WAI",
                            modifier = Modifier.padding(5.dp)
                        )

                        AlaraText(
                            text = "Version 1.0.0",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        },
        containerColor = AlaraColors.Background,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding()
                .background(AlaraColors.Background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Centers content in available space
        ) {

            Icon(
                painter = AlaraColors.AlaraAppIconHorizontal,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))
            AlaraTypewriterText(
                texts = taglines,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            AlaraText(
                text = "Enter your credentials to access your account",
                modifier = Modifier.fillMaxWidth(0.9f),
                softWrap = true,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            AlaraLoginTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = email,
                onValueChange =
                    onEmailChange,
                placeholder = "Email"
            )

            Spacer(modifier = Modifier.height(20.dp))

            AlaraLoginTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                enabled = isContinueEnabled && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlaraButtonColor,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                ),
                onClick =
                    onLoginClick
            ) {
                if (!isLoading) {
                    AlaraText(
                        text = "Continue",
                        color = if (isContinueEnabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.3f
                        )
                    )
                } else {
                    AlaraText(
                        text = "Signing you in...",
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.3f
                        )
                    )
                }
            }


        }


    }

}

@Composable
fun AlaraTypewriterText(
    texts: List<String>, // CHANGED: Now accepts a list
    modifier: Modifier = Modifier,
    typingSpeed: Long = 80, // Speed of writing
    deletingSpeed: Long = 40, // Speed of deleting (usually faster)
    pauseBeforeDelete: Long = 2000, // How long to wait before deleting
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight,
    textAlign: TextAlign

) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(texts) {
        if (texts.isEmpty()) return@LaunchedEffect

        var index = 0
        while (true) { // Infinite Loop
            val currentText = texts[index]

            // 1. Type Out (Forward)
            currentText.forEach { char ->
                displayedText += char
                delay(typingSpeed)
            }

            // 2. Pause (Let user read)
            delay(pauseBeforeDelete)

            // 3. Delete (Backward)
            while (displayedText.isNotEmpty()) {
                displayedText = displayedText.dropLast(1)
                delay(deletingSpeed)
            }

            // 4. Small pause before next word starts
            delay(500)

            // 5. Move to next text (Loop back to 0 if at end)
            index = (index + 1) % texts.size
        }
    }

    AlaraText(
        text = displayedText,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun AlaraLoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    isPassword: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                AlaraText(
                    text = placeholder,
                    color = AlaraColors.TextSecondary.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
            ),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AlaraColors.Input, // Matches your 'AlaraInput' color
                unfocusedContainerColor = AlaraColors.Input,
                focusedBorderColor = AlaraColors.TextSecondary.copy(alpha = 0.3f), // Subtle border on focus
                unfocusedBorderColor = Color.Transparent, // Clean borderless look when inactive
                cursorColor = AlaraColors.TextPrimary,
                focusedTextColor = AlaraColors.TextPrimary,
                unfocusedTextColor = AlaraColors.TextPrimary
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogin() {
    AlaraLoginComposable(
        email = "test@ai.com",
        password = "password123",
        isContinueEnabled = true,
        isLoading = true,
        taglines = listOf("Preview Mode", "Design & Deploy"),
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {}
    )
}