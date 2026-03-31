package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.home.model.PlantStatusUi
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

// ─────────────────────────────────────────────────────────────────────────────
// PlantHeroCard.kt
//
// Card principal que destaca a planta selecionada. Exibe nome, badge de saúde,
// umidade do solo e o botão de irrigação manual.
//
// A imagem da planta é representada por um placeholder (Box cinza) — quando
// o backend retornar uma URL de imagem, substitua pelo componente de imagem
// assíncrona da sua escolha (Coil, Kamel, etc).
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PlantHeroCard(
    plantStatus: PlantStatusUi,
    onWaterNowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ── Informações da planta (coluna esquerda) ───────────────────────
            Column(modifier = Modifier.weight(1f)) {

                // Nome da planta
                Text(
                    text = plantStatus.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.TextPrimary,
                )

                Spacer(Modifier.height(10.dp))

                // Badge de saúde — cor varia conforme o status
                HealthBadge(status = plantStatus.healthStatus)

                Spacer(Modifier.height(20.dp))

                // Umidade do solo em destaque
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Soil Humidity: ",
                        fontSize = 16.sp,
                        color = LeafOnColors.TextPrimary,
                    )
                    Text(
                        text = "${plantStatus.soilHumidity}%",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = LeafOnColors.TextPrimary,
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Botão "Water Now" — apenas dispara o callback, sem lógica interna
                Button(
                    onClick = onWaterNowClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.GreenPrimary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                ) {
                    Text(
                        text = "Water Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.width(24.dp))

            // ── Imagem da planta (coluna direita) ─────────────────────────────
            // Placeholder: substitua por Image(painter = rememberAsyncImagePainter(url))
            // quando a URL da imagem estiver disponível via API.
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(LeafOnColors.BgSoftGreen, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🌿",
                    fontSize = 64.sp,
                )
            }
        }
    }
}

// ── Badge de saúde reutilizável ───────────────────────────────────────────────

/**
 * Exibe a badge de status de saúde da planta.
 * A cor do fundo é determinada pelo conteúdo do [status] — futuras
 * extensões podem usar um enum no lugar da String para maior segurança de tipos.
 */
@Composable
fun HealthBadge(status: String) {
    val bgColor = when (status.lowercase()) {
        "healthy"  -> LeafOnColors.GreenPrimary
        "warning"  -> LeafOnColors.Warning
        "critical" -> LeafOnColors.Error
        else       -> LeafOnColors.TextSecondary
    }
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
    ) {
        Text(
            text = status,
            color = LeafOnColors.TextOnDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

