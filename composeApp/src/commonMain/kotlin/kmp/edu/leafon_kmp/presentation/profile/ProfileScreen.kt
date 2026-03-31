package kmp.edu.leafon_kmp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileContent(
        uiState = uiState,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onGenderChange = viewModel::onGenderChange,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSaveClick = viewModel::saveChanges,
        onDiscardClick = viewModel::discardChanges,
        onLogoutClick = { viewModel.logout(onLoggedOut) },
        modifier = modifier,
    )
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDiscardClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val compact = maxWidth < 980.dp
        val horizontalPadding = if (compact) 16.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProfileHeader(
                fullName = uiState.fullName,
                hasChanges = uiState.hasUnsavedChanges,
                compact = compact,
            )

            ProfileStatusBanner(
                uiState = uiState,
                compact = compact,
            )

            if (compact) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    ProfileSummaryCard(
                        uiState = uiState,
                        onLogoutClick = onLogoutClick,
                    )
                    PersonalInformationCard(
                        uiState = uiState,
                        compact = true,
                        onFirstNameChange = onFirstNameChange,
                        onLastNameChange = onLastNameChange,
                        onEmailChange = onEmailChange,
                        onPhoneChange = onPhoneChange,
                        onGenderChange = onGenderChange,
                    )
                    PasswordSecurityCard(
                        uiState = uiState,
                        onCurrentPasswordChange = onCurrentPasswordChange,
                        onNewPasswordChange = onNewPasswordChange,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                    )
                    BottomActionsCard(
                        compact = true,
                        isSaving = uiState.isSaving,
                        hasChanges = uiState.hasUnsavedChanges,
                        onDiscardClick = onDiscardClick,
                        onSaveClick = onSaveClick,
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    ProfileSummaryCard(
                        uiState = uiState,
                        onLogoutClick = onLogoutClick,
                        modifier = Modifier.width(300.dp),
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        PersonalInformationCard(
                            uiState = uiState,
                            compact = false,
                            onFirstNameChange = onFirstNameChange,
                            onLastNameChange = onLastNameChange,
                            onEmailChange = onEmailChange,
                            onPhoneChange = onPhoneChange,
                            onGenderChange = onGenderChange,
                        )
                        PasswordSecurityCard(
                            uiState = uiState,
                            onCurrentPasswordChange = onCurrentPasswordChange,
                            onNewPasswordChange = onNewPasswordChange,
                            onConfirmPasswordChange = onConfirmPasswordChange,
                        )
                        BottomActionsCard(
                            compact = false,
                            isSaving = uiState.isSaving,
                            hasChanges = uiState.hasUnsavedChanges,
                            onDiscardClick = onDiscardClick,
                            onSaveClick = onSaveClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    fullName: String,
    hasChanges: Boolean,
    compact: Boolean,
) {
    val title = fullName.ifBlank { "Seu perfil" }
    val subtitle = if (hasChanges) {
        "Voce possui alteracoes pendentes para revisar."
    } else {
        "Mantenha seus dados pessoais e credenciais atualizados."
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )

        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = LeafOnColors.TextSecondary,
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LeafOnColors.TextPrimary,
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = LeafOnColors.TextSecondary,
                    )
                }

                SurfaceTag(
                    text = if (hasChanges) "Edicao em andamento" else "Tudo sincronizado",
                    containerColor = if (hasChanges) LeafOnColors.BgSoftGreen else LeafOnColors.BgMain,
                    contentColor = if (hasChanges) LeafOnColors.GreenPrimary else LeafOnColors.TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun ProfileStatusBanner(
    uiState: ProfileUiState,
    compact: Boolean,
) {
    val message = when {
        uiState.errorMessage != null -> uiState.errorMessage
        uiState.saveSuccess -> "Alteracoes salvas com sucesso."
        uiState.hasUnsavedChanges -> "Revise os dados e clique em salvar para aplicar as mudancas."
        else -> "Nenhuma alteracao pendente no momento."
    }

    val containerColor = when {
        uiState.errorMessage != null -> LeafOnColors.Error.copy(alpha = 0.08f)
        uiState.saveSuccess -> LeafOnColors.BgSoftGreen
        else -> LeafOnColors.BgMain
    }

    val contentColor = when {
        uiState.errorMessage != null -> LeafOnColors.Error
        uiState.saveSuccess -> LeafOnColors.Success
        uiState.hasUnsavedChanges -> LeafOnColors.TextPrimary
        else -> LeafOnColors.TextSecondary
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = if (compact) 14.dp else 16.dp),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = contentColor,
        )
    }
}

@Composable
private fun ProfileSummaryCard(
    uiState: ProfileUiState,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(LeafOnColors.BgSoftGreen)
                        .border(1.dp, LeafOnColors.GreenPrimary.copy(alpha = 0.16f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.fullName.take(1).ifBlank { "U" }.uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = LeafOnColors.GreenPrimary,
                    )
                }

                Text(
                    text = uiState.fullName.ifBlank { "Usuario sem nome" },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.TextPrimary,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = uiState.email.ifBlank { "email nao informado" },
                    fontSize = 14.sp,
                    color = LeafOnColors.TextSecondary,
                    textAlign = TextAlign.Center,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SummaryPill(title = "Telefone", value = uiState.phone.ifBlank { "Nao informado" })
                SummaryPill(title = "Genero", value = uiState.gender)
                SummaryPill(
                    title = "Senha",
                    value = if (uiState.newPasswordValid) "Atualizacao pronta" else "Sem alteracao",
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryMetric(
                    title = "Dados pessoais",
                    value = profileCompletion(uiState),
                )
                SummaryMetric(
                    title = "Seguranca",
                    value = if (uiState.newPasswordValid && uiState.passwordsMatch) "Validada" else "A revisar",
                )
                SummaryMetric(
                    title = "Pendencias",
                    value = if (uiState.hasUnsavedChanges) "1 bloco com mudancas" else "Nenhuma",
                )
            }

            HorizontalDivider(color = LeafOnColors.BorderDefault)

            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Sair da conta",
                    color = LeafOnColors.Error,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun PersonalInformationCard(
    uiState: ProfileUiState,
    compact: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
) {
    ProfileSectionCard(
        title = "Informacoes pessoais",
        subtitle = "Atualize os dados usados na identificacao e no contato principal.",
    ) {
        GenderSelector(
            selected = uiState.gender,
            onGenderChange = onGenderChange,
        )

        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileInfoField(
                    label = "Nome",
                    value = uiState.firstName,
                    onValueChange = onFirstNameChange,
                    showEditIcon = false,
                )
                ProfileInfoField(
                    label = "Sobrenome",
                    value = uiState.lastName,
                    onValueChange = onLastNameChange,
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProfileInfoField(
                    label = "Nome",
                    value = uiState.firstName,
                    onValueChange = onFirstNameChange,
                    showEditIcon = false,
                    modifier = Modifier.weight(1f),
                )
                ProfileInfoField(
                    label = "Sobrenome",
                    value = uiState.lastName,
                    onValueChange = onLastNameChange,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        ProfileInfoField(
            label = "Email",
            value = uiState.email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
        )

        ProfileInfoField(
            label = "Telefone",
            value = uiState.phone,
            onValueChange = onPhoneChange,
            keyboardType = KeyboardType.Phone,
        )
    }
}

@Composable
private fun PasswordSecurityCard(
    uiState: ProfileUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
) {
    ProfileSectionCard(
        title = "Senha e seguranca",
        subtitle = "Use pelo menos 8 caracteres e confirme a nova senha antes de salvar.",
    ) {
        PasswordHints(
            currentPasswordFilled = uiState.currentPasswordFilled,
            newPasswordValid = uiState.newPasswordValid,
            passwordsMatch = uiState.passwordsMatch,
        )

        PasswordField(
            label = "Senha atual",
            value = uiState.currentPassword,
            onValueChange = onCurrentPasswordChange,
            isValid = uiState.currentPasswordFilled,
        )
        PasswordField(
            label = "Nova senha",
            value = uiState.newPassword,
            onValueChange = onNewPasswordChange,
            isValid = uiState.newPasswordValid,
        )
        PasswordField(
            label = "Confirmar nova senha",
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isValid = uiState.passwordsMatch,
        )
    }
}

@Composable
private fun BottomActionsCard(
    compact: Boolean,
    isSaving: Boolean,
    hasChanges: Boolean,
    onDiscardClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        if (compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ActionButtons(
                    fullWidth = true,
                    isSaving = isSaving,
                    hasChanges = hasChanges,
                    onDiscardClick = onDiscardClick,
                    onSaveClick = onSaveClick,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (hasChanges) "Mudancas prontas para salvar." else "Nenhuma mudanca pendente.",
                    fontSize = 14.sp,
                    color = LeafOnColors.TextSecondary,
                )
                ActionButtons(
                    fullWidth = false,
                    isSaving = isSaving,
                    hasChanges = hasChanges,
                    onDiscardClick = onDiscardClick,
                    onSaveClick = onSaveClick,
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    fullWidth: Boolean,
    isSaving: Boolean,
    hasChanges: Boolean,
    onDiscardClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    if (fullWidth) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onDiscardClick,
                enabled = hasChanges && !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LeafOnColors.TextPrimary),
            ) {
                Text("Descartar alteracoes", fontWeight = FontWeight.Medium)
            }

            SaveButton(
                isSaving = isSaving,
                hasChanges = hasChanges,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                onClick = onSaveClick,
            )
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onDiscardClick,
                enabled = hasChanges && !isSaving,
                modifier = Modifier.height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LeafOnColors.TextPrimary),
            ) {
                Text("Descartar", fontWeight = FontWeight.Medium)
            }

            SaveButton(
                isSaving = isSaving,
                hasChanges = hasChanges,
                modifier = Modifier.height(46.dp),
                onClick = onSaveClick,
            )
        }
    }
}

@Composable
private fun SaveButton(
    isSaving: Boolean,
    hasChanges: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = hasChanges && !isSaving,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LeafOnColors.GreenPrimary,
            contentColor = LeafOnColors.TextOnDark,
            disabledContainerColor = LeafOnColors.GreenPrimary.copy(alpha = 0.4f),
            disabledContentColor = LeafOnColors.TextOnDark.copy(alpha = 0.7f),
        ),
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                color = LeafOnColors.TextOnDark,
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
            )
            Spacer(Modifier.width(8.dp))
            Text("Salvando", fontWeight = FontWeight.SemiBold)
        } else {
            Text("Salvar alteracoes", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = LeafOnColors.TextSecondary,
                )
            }
            HorizontalDivider(color = LeafOnColors.BorderDefault)
            content()
        }
    }
}

@Composable
private fun GenderSelector(
    selected: String,
    onGenderChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Genero",
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )

        FlowRow(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf("Male" to "Masculino", "Female" to "Feminino").forEach { (value, label) ->
                GenderChip(
                    selected = selected == value,
                    label = label,
                    onClick = { onGenderChange(value) },
                )
            }
        }
    }
}

@Composable
private fun GenderChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) LeafOnColors.BgSoftGreen else LeafOnColors.BgSecondary)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = LeafOnColors.GreenPrimary,
                unselectedColor = LeafOnColors.BorderDefault,
            ),
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = LeafOnColors.TextPrimary,
        )
    }
}

@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    showEditIcon: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (showEditIcon) {
                {
                    Text(
                        text = "Edit",
                        fontSize = 12.sp,
                        color = LeafOnColors.TextSecondary,
                    )
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LeafOnColors.BgMain,
                unfocusedContainerColor = LeafOnColors.BgMain,
                focusedBorderColor = LeafOnColors.GreenHover,
                unfocusedBorderColor = LeafOnColors.BorderDefault,
                focusedTextColor = LeafOnColors.TextPrimary,
                unfocusedTextColor = LeafOnColors.TextPrimary,
                focusedTrailingIconColor = LeafOnColors.GreenPrimary,
            ),
        )
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(false) }
    val statusColor = if (value.isNotEmpty() && isValid) LeafOnColors.Success else LeafOnColors.TextSecondary
    val visibilityLabel = if (visible) "Hide" else "Show"

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor),
                    )
                    Text(
                        text = visibilityLabel,
                        fontSize = 12.sp,
                        color = LeafOnColors.TextSecondary,
                        modifier = Modifier
                            .clickable { visible = !visible },
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LeafOnColors.BgMain,
                unfocusedContainerColor = LeafOnColors.BgMain,
                focusedBorderColor = LeafOnColors.GreenHover,
                unfocusedBorderColor = LeafOnColors.BorderDefault,
                focusedTextColor = LeafOnColors.TextPrimary,
                unfocusedTextColor = LeafOnColors.TextPrimary,
            ),
        )
    }
}

@Composable
private fun PasswordHints(
    currentPasswordFilled: Boolean,
    newPasswordValid: Boolean,
    passwordsMatch: Boolean,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SecurityHint(
            text = "Senha atual preenchida",
            active = currentPasswordFilled,
        )
        SecurityHint(
            text = "Minimo de 8 caracteres",
            active = newPasswordValid,
        )
        SecurityHint(
            text = "Confirmacao confere",
            active = passwordsMatch,
        )
    }
}

@Composable
private fun SecurityHint(
    text: String,
    active: Boolean,
) {
    SurfaceTag(
        text = text,
        containerColor = if (active) LeafOnColors.BgSoftGreen else LeafOnColors.BgSecondary,
        contentColor = if (active) LeafOnColors.GreenPrimary else LeafOnColors.TextSecondary,
    )
}

@Composable
private fun SummaryPill(
    title: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LeafOnColors.BgSecondary)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = LeafOnColors.TextSecondary,
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )
    }
}

@Composable
private fun SummaryMetric(
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )
    }
}

@Composable
private fun SurfaceTag(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
        )
    }
}

private fun profileCompletion(uiState: ProfileUiState): String {
    val fields = listOf(
        uiState.firstName,
        uiState.lastName,
        uiState.email,
        uiState.phone,
    )
    val filled = fields.count { it.isNotBlank() }
    return "$filled/4 campos"
}

