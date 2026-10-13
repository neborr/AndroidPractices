package com.example.androidpractices.data

data class Launch(
    val id: String,
    val name: String,
    val provider: String,
    val status: String,
    val windowStart: String,
    val rocketName: String,
    val location: String,
    val description: String,
    val imageUrl: String
)

object MockData {
    val sampleLaunches = listOf(
        Launch(
            id = "1",
            name = "Falcon 9 Block 5 | Starlink Group 6-42",
            provider = "SpaceX",
            status = "Успешный запуск",
            windowStart = "2024-03-24 02:30 UTC",
            rocketName = "Falcon 9",
            location = "Space Launch Complex 40, Cape Canaveral, FL, USA",
            description = "Партия из 23 спутников Starlink v2 Mini для глобальной интернет-группировки SpaceX.",
            imageUrl = "https://images.unsplash.com/photo-1517976487492-5750f3195933"
        ),
        Launch(
            id = "2",
            name = "Starship | Integrated Flight Test 3",
            provider = "SpaceX",
            status = "Частичный успех",
            windowStart = "2024-03-14 13:25 UTC",
            rocketName = "Starship",
            location = "Starbase, Boca Chica, TX, USA",
            description = "Третий испытательный полёт сверхтяжелой ракеты Starship. Достигнута орбитальная скорость и проведён ряд тестов в космосе.",
            imageUrl = "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2"
        ),
        Launch(
            id = "3",
            name = "Electron | Owl Night Long",
            provider = "Rocket Lab",
            status = "Запланирован",
            windowStart = "2024-04-10 10:00 UTC",
            rocketName = "Electron",
            location = "Launch Complex 1, Mahia Peninsula, New Zealand",
            description = "Миссия по выводу радиолокационного спутника наблюдения Земли для компании Synspective.",
            imageUrl = "https://images.unsplash.com/photo-1516849841032-87cbac4d88f7"
        ),
        Launch(
            id = "4",
            name = "Atlas V 541 | Silentbarker / NROL-107",
            provider = "United Launch Alliance (ULA)",
            status = "Успешный запуск",
            windowStart = "2023-09-10 12:47 UTC",
            rocketName = "Atlas V 541",
            location = "Space Launch Complex 41, Cape Canaveral, FL, USA",
            description = "Секретная миссия в интересах Космических сил США и Национального управления военно-космической разведки.",
            imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa"
        )
    )
}