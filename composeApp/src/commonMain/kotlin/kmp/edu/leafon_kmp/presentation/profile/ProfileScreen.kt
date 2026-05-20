package kmp.edu.leafon_kmp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.state.collectAsState().value

    ProfileContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onPhoneChange = viewModel::onPhoneChange,
        onSaveClick = viewModel::saveChanges,
        onDiscardClick = viewModel::discardChanges,
        onLogoutClick = { viewModel.logout(onLoggedOut) },
        modifier = modifier,
    )
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
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
                name = uiState.name,
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
                        onNameChange = onNameChange,
                        onPhoneChange = onPhoneChange,
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
                            onNameChange = onNameChange,
                            onPhoneChange = onPhoneChange,
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
    name: String,
    hasChanges: Boolean,
    compact: Boolean,
) {
    val title = name.ifBlank { "Seu perfil" }
    val subtitle = if (hasChanges) {
        "Voce possui alteracoes pendentes para revisar."
    } else {
        "Mantenha seus dados pessoais atualizados."
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
        uiState.isLoading -> "Carregando dados do perfil..."
        uiState.errorMessage != null -> uiState.errorMessage
        uiState.saveSuccess -> "Alteracoes salvas com sucesso."
        uiState.hasUnsavedChanges -> "Revise os dados e clique em salvar para aplicar as mudancas."
        else -> "Nenhuma alteracao pendente no momento."
    }

    val containerColor = when {
        uiState.isLoading -> LeafOnColors.BgMain
        uiState.errorMessage != null -> LeafOnColors.Error.copy(alpha = 0.08f)
        uiState.saveSuccess -> LeafOnColors.BgSoftGreen
        else -> LeafOnColors.BgMain
    }

    val contentColor = when {
        uiState.isLoading -> LeafOnColors.TextSecondary
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
                        text = uiState.name.take(1).ifBlank { "U" }.uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = LeafOnColors.GreenPrimary,
                    )
                }

                Text(
                    text = uiState.name.ifBlank { "Usuario sem nome" },
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
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryMetric(
                    title = "Dados pessoais",
                    value = profileCompletion(uiState),
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
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
) {
    ProfileSectionCard(
        title = "Informacoes pessoais",
        subtitle = "Atualize os dados usados na identificacao e no contato principal.",
    ) {
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileInfoField(
                    label = "Nome",
                    value = uiState.name,
                    onValueChange = onNameChange,
                    showEditIcon = false,
                )
            }
        } else {
            ProfileInfoField(
                label = "Nome",
                value = uiState.name,
                onValueChange = onNameChange,
                showEditIcon = false,
            )
        }

        ProfileInfoField(
            label = "Email",
            value = uiState.email,
            onValueChange = {},
            keyboardType = KeyboardType.Email,
            enabled = false,
            showEditIcon = false,
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
fun ProfileInfoField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
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
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (showEditIcon && enabled) {
                {
                    Text(
                        text = "Edit",
                        fontSize = 12.sp,
                        color = LeafOnColors.TextSecondary,
                    )
                }
            } else {
                null
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LeafOnColors.BgMain,
                unfocusedContainerColor = LeafOnColors.BgMain,
                focusedBorderColor = LeafOnColors.GreenHover,
                unfocusedBorderColor = LeafOnColors.BorderDefault,
                focusedTextColor = LeafOnColors.TextPrimary,
                unfocusedTextColor = LeafOnColors.TextPrimary,
                focusedTrailingIconColor = LeafOnColors.GreenPrimary,
                disabledContainerColor = LeafOnColors.BgSecondary,
                disabledBorderColor = LeafOnColors.BorderDefault,
                disabledTextColor = LeafOnColors.TextSecondary,
            ),
        )
    }
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
        uiState.name,
        uiState.email,
        uiState.phone,
    )
    val filled = fields.count { it.isNotBlank() }
    return "$filled/3 campos"
}
