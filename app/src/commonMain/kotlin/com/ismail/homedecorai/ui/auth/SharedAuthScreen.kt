package com.ismail.homedecorai.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.model.AuthMode
import com.ismail.homedecorai.ui.components.ResponsiveDialog
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled

@Composable
fun SharedAuthScreen(
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (email: String, password: String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onClose: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {},
) {
    var authMode by remember { mutableStateOf(AuthMode.SignIn) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateEmail(): Boolean {
        val trimmed = email.trim()
        if (trimmed.isBlank()) {
            emailError = Strings.authValidationEmailRequired
            return false
        }
        if (!trimmed.contains('@') || !trimmed.contains('.')) {
            emailError = Strings.authValidationEmailInvalid
            return false
        }
        emailError = null
        return true
    }

    fun validatePassword(): Boolean {
        if (password.isBlank()) {
            passwordError = Strings.authValidationPasswordRequired
            return false
        }
        if (password.length < 6) {
            passwordError = Strings.authValidationPasswordMin
            return false
        }
        passwordError = null
        return true
    }

    fun validateForm(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        return emailValid && passwordValid
    }

    val isFormValid = email.trim().isNotBlank() && password.length >= 6

    ResponsiveDialog(
        onDismissRequest = onClose,
        title = if (authMode == AuthMode.SignIn) Strings.authWelcomeBack else Strings.authCreateAccount,
        subtitle = if (authMode == AuthMode.SignIn) Strings.authSignInSubtitle else Strings.authSignUpSubtitle,
        maxWidth = 440.dp,
    ) {
        if (errorMessage != null) {
            AuthErrorBanner(
                message = errorMessage,
                onDismiss = onDismissError,
            )
        }

        OutlinedButton(
            onClick = onGoogleSignIn,
            shape = HomeDecorShape.Button,
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeDecorSpacing.ButtonHeight)
                .semantics { contentDescription = Strings.authContinueWithGoogle },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            enabled = !isLoading,
        ) {
            GoogleIcon(modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                Strings.authContinueWithGoogle,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            Text(
                Strings.authOr,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }

        AuthTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = Strings.email,
            leadingIcon = Icons.Rounded.Email,
            keyboardType = KeyboardType.Email,
            errorMessage = emailError,
            a11yLabel = Strings.email,
        )

        AuthTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = Strings.password,
            leadingIcon = Icons.Rounded.Shield,
            isPassword = true,
            passwordVisible = passwordVisible,
            onTogglePassword = { passwordVisible = !passwordVisible },
            errorMessage = passwordError,
            a11yLabel = Strings.password,
        )

        if (authMode == AuthMode.SignIn) {
            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    Strings.authForgotPassword,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(Modifier.height(HomeDecorSpacing.Xs))

        Button(
            onClick = {
                if (validateForm()) {
                    if (authMode == AuthMode.SignIn) onSignIn(email.trim(), password)
                    else onSignUp(email.trim(), password)
                }
            },
            shape = HomeDecorShape.ButtonLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeDecorSpacing.ButtonHeight)
                .semantics {
                    contentDescription = if (authMode == AuthMode.SignIn) {
                        Strings.authSignInButton
                    } else {
                        Strings.authSignUpButton
                    }
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            enabled = isFormValid && !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = if (authMode == AuthMode.SignIn) Strings.authSignInButton else Strings.authSignUpButton,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.Shield,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(6.dp))
            Text(
                Strings.authDataProtected,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = if (authMode == AuthMode.SignIn) Strings.authNoAccountYet else Strings.authHasAccount,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = if (authMode == AuthMode.SignIn) Strings.authSignUp else Strings.authSignIn,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        authMode = if (authMode == AuthMode.SignIn) AuthMode.SignUp else AuthMode.SignIn
                        email = ""
                        password = ""
                        passwordVisible = false
                        emailError = null
                        passwordError = null
                    }
                    .semantics {
                        contentDescription = if (authMode == AuthMode.SignIn) {
                            Strings.authSignUp
                        } else {
                            Strings.authSignIn
                        }
                    },
            )
        }
    }
}

@Composable
private fun AuthErrorBanner(
    message: String,
    onDismiss: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onDismiss) {
                Text(
                    Strings.dismiss,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
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
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    errorMessage: String? = null,
    a11yLabel: String,
) {
    Column {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (errorMessage != null) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = if (errorMessage != null) {
                        "$a11yLabel. Error: $errorMessage"
                    } else {
                        a11yLabel
                    }
                },
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = if (errorMessage != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(HomeDecorIconSize.Medium),
                )
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
                    placeholder = {
                        if (isPassword) {
                            Text(
                                Strings.password,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isPassword && !passwordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )
                if (isPassword && onTogglePassword != null) {
                    IconButton(
                        onClick = onTogglePassword,
                        modifier = Modifier
                            .size(40.dp)
                            .semantics { contentDescription = Strings.a11yPasswordToggle },
                    ) {
                        Icon(
                            if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(HomeDecorIconSize.Medium),
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
            if (errorMessage != null) {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        .semantics { contentDescription = "Error: $errorMessage" },
                )
            }
        }
    }
}

@Composable
private fun GoogleIcon(modifier: Modifier = Modifier) {
    val googleRed = Color(0xFFEA4335)
    val googleBlue = Color(0xFF4285F4)
    val googleYellow = Color(0xFFFBBC05)
    val googleGreen = Color(0xFF34A853)

    Box(
        modifier = modifier
            .clip(CircleShape)
            .drawBehind {
                drawCircle(color = Color.White)
                drawGoogleG(googleRed, googleBlue, googleYellow, googleGreen)
            },
    )
}

private fun DrawScope.drawGoogleG(red: Color, blue: Color, yellow: Color, green: Color) {
    val s = size.width
    val sw = s * 0.12f
    val half = s * 0.5f
    val strokeStyle = androidx.compose.ui.graphics.drawscope.Stroke(width = sw, cap = androidx.compose.ui.graphics.StrokeCap.Butt)
    val rect = androidx.compose.ui.geometry.Rect(Offset(sw / 2f, sw / 2f), Size(s - sw, s - sw))

    val bluePath = Path().apply {
        arcTo(rect, startAngleDegrees = 0f, sweepAngleDegrees = 270f, forceMoveTo = true)
        lineTo(half + s * 0.25f, half)
    }
    drawPath(bluePath, color = blue, style = strokeStyle)

    drawArc(color = red, startAngle = 180f, sweepAngle = 90f, useCenter = false, topLeft = Offset(sw / 2f, sw / 2f), size = Size(s - sw, s - sw), style = strokeStyle)
    drawArc(color = yellow, startAngle = 90f, sweepAngle = 90f, useCenter = false, topLeft = Offset(sw / 2f, sw / 2f), size = Size(s - sw, s - sw), style = strokeStyle)
    drawArc(color = green, startAngle = 0f, sweepAngle = 90f, useCenter = false, topLeft = Offset(sw / 2f, sw / 2f), size = Size(s - sw, s - sw), style = strokeStyle)
}
