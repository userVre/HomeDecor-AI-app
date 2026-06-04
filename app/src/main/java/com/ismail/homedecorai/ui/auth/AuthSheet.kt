package com.ismail.homedecorai.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*

enum class AuthMode { SIGN_IN, SIGN_UP }

@Composable
fun AuthSheet(
    onClose: () -> Unit,
    onAuth: () -> Unit,
) {
    var authMode by remember { mutableStateOf(AuthMode.SIGN_IN) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val modalTapBlocker = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    if (authMode == AuthMode.SIGN_UP) authMode = AuthMode.SIGN_IN else onClose()
                }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        if (authMode == AuthMode.SIGN_UP) Icons.Rounded.Close else Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = StudioInk,
                    )
                }
                IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = StudioInk)
                }
            }

            Spacer(Modifier.height(8.dp))

            Surface(shape = CircleShape, color = StudioMist, modifier = Modifier.size(72.dp)) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(36.dp), tint = StudioInk)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                if (authMode == AuthMode.SIGN_IN) stringResource(R.string.auth_welcome_back)
                else stringResource(R.string.auth_start_creating),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (authMode == AuthMode.SIGN_IN) stringResource(R.string.auth_sign_in_subtitle)
                else stringResource(R.string.auth_sign_up_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(28.dp))

            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF8F6F3)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    OutlinedButton(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    ) {
                        Text("G", fontWeight = FontWeight.Black, color = Color(0xFF4285F4))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (authMode == AuthMode.SIGN_IN) stringResource(R.string.continue_with_google)
                            else stringResource(R.string.continue_with_google),
                            color = StudioInk,
                        )
                    }

                    OutlinedButton(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    ) {
                        Text("🍎", fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (authMode == AuthMode.SIGN_IN) stringResource(R.string.continue_with_apple)
                            else stringResource(R.string.continue_with_apple),
                            color = StudioInk,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = StudioLine)
                        Text(stringResource(R.string.or), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = StudioLine)
                    }

                    AnimatedVisibility(visible = authMode == AuthMode.SIGN_UP, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            AuthTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = stringResource(R.string.full_name),
                                leadingIcon = Icons.Rounded.Person,
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = stringResource(R.string.email),
                        leadingIcon = Icons.Rounded.Email,
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(Modifier.height(12.dp))

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = stringResource(R.string.password),
                        leadingIcon = Icons.Rounded.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible },
                    )

                    AnimatedVisibility(visible = authMode == AuthMode.SIGN_UP, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            Spacer(Modifier.height(12.dp))
                            AuthTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = stringResource(R.string.confirm_password),
                                leadingIcon = Icons.Rounded.Lock,
                                isPassword = true,
                                passwordVisible = confirmPasswordVisible,
                                onTogglePassword = { confirmPasswordVisible = !confirmPasswordVisible },
                            )
                        }
                    }

                    if (authMode == AuthMode.SIGN_IN) {
                        TextButton(
                            onClick = { },
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text(
                                stringResource(R.string.forgot_password),
                                color = StudioInk,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == AuthMode.SIGN_IN) StudioBlue else StudioInk,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(
                            if (authMode == AuthMode.SIGN_IN) stringResource(R.string.sign_in)
                            else stringResource(R.string.create_account),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        if (authMode == AuthMode.SIGN_IN) stringResource(R.string.auth_data_protected)
                        else stringResource(R.string.auth_free_to_start),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    if (authMode == AuthMode.SIGN_IN) stringResource(R.string.no_account_yet)
                    else stringResource(R.string.already_have_account),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    if (authMode == AuthMode.SIGN_IN) stringResource(R.string.sign_up)
                    else stringResource(R.string.sign_in),
                    color = StudioInk,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        authMode = if (authMode == AuthMode.SIGN_IN) AuthMode.SIGN_UP else AuthMode.SIGN_IN
                    },
                )
            }
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF5F3F0),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(leadingIcon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            )
            if (isPassword && onTogglePassword != null) {
                IconButton(onClick = onTogglePassword, modifier = Modifier.size(40.dp)) {
                    Icon(
                        if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = stringResource(R.string.toggle_password_visibility),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}
