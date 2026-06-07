package com.ismail.homedecorai.ui.auth

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

@Composable
fun AuthSheet(
    onClose: () -> Unit,
    onAuth: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isFormValid = email.isNotBlank() && password.length >= 6
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
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = StudioInk)
                }
            }

            Spacer(Modifier.height(4.dp))

            Surface(shape = CircleShape, color = StudioMist, modifier = Modifier.size(72.dp)) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(36.dp), tint = StudioInk)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                stringResource(R.string.auth_welcome_back),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.auth_sign_in_subtitle),
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
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4285F4).copy(alpha = 0.3f)),
                    ) {
                        Text(
                            "\uD83D\uDD35",
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4285F4),
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            stringResource(R.string.continue_with_google),
                            color = StudioInk,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = StudioLine)
                        Text(
                            stringResource(R.string.or),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = StudioLine)
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

                    TextButton(
                        onClick = { },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(
                            stringResource(R.string.forgot_password),
                            color = StudioBlue,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) StudioBlue else StudioLine.copy(alpha = 0.5f),
                            contentColor = Color.White,
                        ),
                        enabled = isFormValid,
                    ) {
                        Text(
                            stringResource(R.string.sign_in_button),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        stringResource(R.string.auth_data_protected),
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
                    stringResource(R.string.no_account_yet),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(R.string.sign_up),
                    color = StudioInk,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { },
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
