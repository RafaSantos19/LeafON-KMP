package kmp.edu.leafon_kmp.presentation.pots.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun PotFormContent(
    name: String,
    plantName: String,
    deviceId: String,
    isSaving: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onPlantNameChange: (String) -> Unit,
    onDeviceIdChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancelClick: (() -> Unit)? = null,
    title: String = "Cadastrar novo pot",
    subtitle: String = "Informe os dados iniciais para conectar um novo vaso inteligente.",
    submitLabel: String = "Salvar pot",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 560.dp)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        FormHeader(
            title = title,
            subtitle = subtitle,
        )

        PotTextField(
            label = "Nome do pot",
            value = name,
            onValueChange = onNameChange,
            placeholder = "Ex: Vaso da sala",
            enabled = !isSaving,
        )

        PotTextField(
            label = "Nome da planta",
            value = plantName,
            onValueChange = onPlantNameChange,
            placeholder = "Ex: Jiboia",
            enabled = !isSaving,
        )

        PotTextField(
            label = "Device ID",
            value = deviceId,
            onValueChange = onDeviceIdChange,
            placeholder = "Opcional",
            enabled = !isSaving,
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = LeafOnColors.Error,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }

        FormActions(
            isSaving = isSaving,
            submitLabel = submitLabel,
            onSubmitClick = onSubmitClick,
            onCancelClick = onCancelClick,
        )
    }
}

@Composable
private fun FormHeader(
    title: String,
    subtitle: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun PotTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = LeafOnColors.TextSecondary,
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LeafOnColors.BgMain,
                unfocusedContainerColor = LeafOnColors.BgMain,
                disabledContainerColor = LeafOnColors.BgSecondary,
                focusedBorderColor = LeafOnColors.GreenHover,
                unfocusedBorderColor = LeafOnColors.BorderDefault,
                focusedTextColor = LeafOnColors.TextPrimary,
                unfocusedTextColor = LeafOnColors.TextPrimary,
            ),
        )
    }
}

@Composable
private fun FormActions(
    isSaving: Boolean,
    submitLabel: String,
    onSubmitClick: () -> Unit,
    onCancelClick: (() -> Unit)?,
) {
    if (onCancelClick == null) {
        Button(
            onClick = onSubmitClick,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            SubmitButtonContent(
                isSaving = isSaving,
                submitLabel = submitLabel,
            )
        }
        return
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onCancelClick,
            enabled = !isSaving,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
        ) {
            Text(
                text = "Voltar",
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.width(12.dp))

        Button(
            onClick = onSubmitClick,
            enabled = !isSaving,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            SubmitButtonContent(
                isSaving = isSaving,
                submitLabel = submitLabel,
            )
        }
    }
}

@Composable
private fun SubmitButtonContent(
    isSaving: Boolean,
    submitLabel: String,
) {
    if (isSaving) {
        CircularProgressIndicator(
            color = LeafOnColors.TextOnDark,
            strokeWidth = 2.dp,
            modifier = Modifier
                .height(18.dp)
                .width(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text("Salvando")
    } else {
        Text(
            text = submitLabel,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
