package kmp.edu.leafon_kmp.presentation.pots.routines.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay
import kmp.edu.leafon_kmp.presentation.pots.routines.model.shortLabel

@Composable
fun RoutineFormContent(
    name: String,
    time: String,
    selectedDays: Set<WeekDay>,
    durationSec: Int,
    enabled: Boolean,
    isSaving: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onToggleDay: (WeekDay) -> Unit,
    onDurationChange: (String) -> Unit,
    onToggleEnabled: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancelClick: (() -> Unit)? = null,
    title: String = "Nova rotina",
    subtitle: String = "Defina quando o Smart Pot deve irrigar a planta.",
    submitLabel: String = "Salvar rotina",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 620.dp)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        RoutineFormHeader(
            title = title,
            subtitle = subtitle,
        )

        RoutineTextField(
            label = "Nome da rotina",
            value = name,
            onValueChange = onNameChange,
            placeholder = "Opcional",
            enabled = !isSaving,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
        )

        RoutineTextField(
            label = "Horario",
            value = time,
            onValueChange = onTimeChange,
            placeholder = "Ex: 08:00",
            enabled = !isSaving,
            keyboardOptions = KeyboardOptions.Default,
        )

        WeekDaySelector(
            selectedDays = selectedDays,
            enabled = !isSaving,
            onToggleDay = onToggleDay,
        )

        RoutineTextField(
            label = "Duracao em segundos",
            value = durationSec.takeIf { it > 0 }?.toString().orEmpty(),
            onValueChange = onDurationChange,
            placeholder = "Ex: 20",
            enabled = !isSaving,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        EnabledToggle(
            enabled = enabled,
            controlsEnabled = !isSaving,
            onToggleEnabled = onToggleEnabled,
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = LeafOnColors.Error,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }

        RoutineFormActions(
            isSaving = isSaving,
            submitLabel = submitLabel,
            onSubmitClick = onSubmitClick,
            onCancelClick = onCancelClick,
        )
    }
}

@Composable
private fun RoutineFormHeader(
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
private fun RoutineTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions,
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
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(8.dp),
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
private fun WeekDaySelector(
    selectedDays: Set<WeekDay>,
    enabled: Boolean,
    onToggleDay: (WeekDay) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Dias da semana",
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WeekDay.entries.forEach { day ->
                WeekDayButton(
                    label = day.shortLabel(),
                    selected = day in selectedDays,
                    enabled = enabled,
                    onClick = { onToggleDay(day) },
                )
            }
        }
    }
}

@Composable
private fun WeekDayButton(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (selected) LeafOnColors.GreenPrimary else LeafOnColors.BgMain
    val contentColor = if (selected) LeafOnColors.TextOnDark else LeafOnColors.TextSecondary

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = LeafOnColors.BgSecondary,
            disabledContentColor = LeafOnColors.TextSecondary,
        ),
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun EnabledToggle(
    enabled: Boolean,
    controlsEnabled: Boolean,
    onToggleEnabled: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LeafOnColors.BgMain, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Rotina ativa",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
            )
            Text(
                text = "Permite executar esta automacao no horario definido.",
                fontSize = 12.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        Switch(
            checked = enabled,
            enabled = controlsEnabled,
            onCheckedChange = { onToggleEnabled() },
        )
    }
}

@Composable
private fun RoutineFormActions(
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
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            RoutineSubmitContent(
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
            shape = RoundedCornerShape(8.dp),
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
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            RoutineSubmitContent(
                isSaving = isSaving,
                submitLabel = submitLabel,
            )
        }
    }
}

@Composable
private fun RoutineSubmitContent(
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
