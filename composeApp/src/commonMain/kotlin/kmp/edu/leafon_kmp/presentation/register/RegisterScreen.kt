package kmp.edu.leafon_kmp.presentation.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import leafon_kmp.composeapp.generated.resources.Res
import leafon_kmp.composeapp.generated.resources.hero_register
import leafon_kmp.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

private val RegisterGreenPrimary = Color(0xFF2E7D32)
private val RegisterGreenHover = Color(0xFF4CAF50)
private val RegisterGreenHighlight = Color(0xFF8BC34A)
private val RegisterBgSecondary = Color(0xFFF5F5F5)
private val RegisterTextOnDark = Color(0xFFFFFFFF)
private val RegisterTextMuted = Color(0xB3FFFFFF)
private val RegisterErrorRed = Color(0xFFE53935)
private val RegisterInfoGreen = Color(0xFFC8E6C9)
private val RegisterPlaceholderGray = Color(0xFF9E9E9E)
private val RegisterFieldText = Color(0xFF212121)

@Composable
fun RegisterScreen(
    state: RegisterState,
    onNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onBackToLoginClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth > 840.dp) {
            WideRegisterLayout(
                state = state,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPhoneChange = onPhoneChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onRegisterClick = onRegisterClick,
                onBackToLoginClick = onBackToLoginClick
            )
        } else {
            NarrowRegisterLayout(
                state = state,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPhoneChange = onPhoneChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onRegisterClick = onRegisterClick,
                onBackToLoginClick = onBackToLoginClick
            )
        }
    }
}

@Composable
private fun WideRegisterLayout(
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RegisterBgSecondary)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val anchor = width * 0.40f

            val path = Path().apply {
                moveTo(anchor, 0f)
                cubicTo(
                    anchor + width * 0.11f, height * 0.25f,
                    anchor - width * 0.05f, height * 0.75f,
                    anchor, height
                )
                lineTo(width, height)
                lineTo(width, 0f)
                close()
            }

            drawPath(path, color = RegisterGreenPrimary)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            RegisterLeftPanel(
                modifier = Modifier
                    .weight(0.40f)
                    .fillMaxHeight()
                    .padding(24.dp)
            )

            Box(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxHeight()
                    .padding(horizontal = 42.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                RegisterForm(
                    state = state,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPhoneChange = onPhoneChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onRegisterClick = onRegisterClick,
                    onBackToLoginClick = onBackToLoginClick,
                    modifier = Modifier
                        .widthIn(max = 460.dp)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
private fun NarrowRegisterLayout(
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RegisterGreenPrimary)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Leaf.ON logo",
            modifier = Modifier.size(52.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(Res.drawable.hero_register),
            contentDescription = "Ilustracao de cadastro",
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterForm(
            state = state,
            onNameChange = onNameChange,
            onEmailChange = onEmailChange,
            onPhoneChange = onPhoneChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onRegisterClick = onRegisterClick,
            onBackToLoginClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RegisterLeftPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Leaf.ON logo",
            modifier = Modifier.size(52.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(Res.drawable.hero_register),
                    contentDescription = "Ilustracao de cadastro",
                    modifier = Modifier.size(280.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Configure seu ecossistema",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = RegisterGreenPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cadastre sua conta e a primeira planta para centralizar o cuidado em um so lugar.",
                    color = Color(0xFF4F4F4F),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.widthIn(max = 320.dp)
                )
            }
        }
    }
}

@Composable
private fun RegisterForm(
    state: RegisterState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vamos criar sua conta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = RegisterTextOnDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Preencha os dados abaixo para comecar a usar o app.",
            fontSize = 14.sp,
            color = RegisterTextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        RegisterField(
            label = "Seu nome:",
            value = state.name,
            onValueChange = onNameChange,
            placeholder = "Seu nome completo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterField(
            label = "Seu email:",
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "seuemail@mail.com",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterField(
            label = "Seu telefone:",
            value = state.phone,
            onValueChange = onPhoneChange,
            placeholder = "(00) 00000-0000",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterField(
            label = "Sua senha:",
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "**************",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        RegisterField(
            label = "Confirmar senha:",
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "**************",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.error != null) {
            Text(
                text = state.error,
                color = RegisterErrorRed,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        if (state.infoMessage != null) {
            Text(
                text = state.infoMessage,
                color = RegisterInfoGreen,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        OutlinedButton(
            onClick = onRegisterClick,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = RegisterTextOnDark,
                disabledContentColor = RegisterTextOnDark.copy(alpha = 0.5f)
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(
                    if (state.isLoading) RegisterTextOnDark.copy(alpha = 0.4f) else RegisterTextOnDark
                )
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = RegisterTextOnDark,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Cadastrar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 0.4.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val backText = buildAnnotatedString {
            withStyle(SpanStyle(color = RegisterTextOnDark, fontSize = 14.sp)) {
                append("Ja tem conta? ")
            }
            withStyle(
                SpanStyle(
                    color = RegisterGreenHighlight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Voltar para login.")
            }
        }

        Text(
            text = backText,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { onBackToLoginClick() }
        )
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RegisterGreenHighlight,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = RegisterPlaceholderGray,
                    fontSize = 14.sp
                )
            },
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = RegisterGreenHover,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = RegisterFieldText,
                unfocusedTextColor = RegisterFieldText
            )
        )
    }
}
