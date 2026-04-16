package kmp.edu.leafon_kmp.presentation.pots.model

data class PotUi(
    val id: String,
    val name: String,
    val plantType: String,
    val status: PotStatus,
    val humidityPercent: Int?,
    val temperatureCelsius: Int?,
    val lastUpdateLabel: String,
)

enum class PotStatus {
    ONLINE,
    OFFLINE,
    ATTENTION,
}

fun potPreviewItems() = listOf(
    PotUi(
        id = "1",
        name = "Aloe Vera",
        plantType = "Suculenta",
        status = PotStatus.ONLINE,
        humidityPercent = 42,
        temperatureCelsius = 24,
        lastUpdateLabel = "2 min atras",
    ),
    PotUi(
        id = "2",
        name = "Monstera Deliciosa",
        plantType = "Tropical",
        status = PotStatus.ONLINE,
        humidityPercent = 68,
        temperatureCelsius = 22,
        lastUpdateLabel = "5 min atras",
    ),
    PotUi(
        id = "3",
        name = "Cacto Sao Jorge",
        plantType = "Cactaceae",
        status = PotStatus.ATTENTION,
        humidityPercent = 18,
        temperatureCelsius = 29,
        lastUpdateLabel = "12 min atras",
    ),
    PotUi(
        id = "4",
        name = "Samambaia",
        plantType = "Pteridofita",
        status = PotStatus.OFFLINE,
        humidityPercent = null,
        temperatureCelsius = null,
        lastUpdateLabel = "3 horas atras",
    ),
    PotUi(
        id = "5",
        name = "Orquidea Phalaenopsis",
        plantType = "Orchidaceae",
        status = PotStatus.ONLINE,
        humidityPercent = 55,
        temperatureCelsius = 23,
        lastUpdateLabel = "1 min atras",
    ),
)
