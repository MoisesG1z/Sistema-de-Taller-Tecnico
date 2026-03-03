package com.example.zafire.sistemadetallertecnico

import androidx.compose.ui.graphics.Color

data class StatCard(
    val title: String,
    val value: Int,
    val color: Color,
    val iconLabel: String,
)

data class EquipmentRow(
    val client: String,
    val phone: String,
    val brand: String,
    val model: String,
    val issue: String,
    val serial: String,
    val status: RepairStatus,
    val intakeDate: String,
    val cost: String,
)

data class AppointmentRow(
    val dateTime: String,
    val client: String,
    val reason: String,
    val status: String,
)

enum class RepairStatus(val label: String, val tone: Color) {
    RECEIVED("Recibido", Color(0xFF4C6EF5)),
    DIAGNOSIS("En Diagnostico", Color(0xFF228BE6)),
    WAITING_PARTS("Esperando Repuesto", Color(0xFFF08C00)),
    REPAIRING("En Reparacion", Color(0xFF7950F2)),
    READY("Reparado (Listo)", Color(0xFF12B886)),
    DELIVERED("Entregado", Color(0xFF2F9E44)),
    NO_REPAIR("Sin Reparacion", Color(0xFF868E96)),
    DISMISSED("Desestimado", Color(0xFFFA5252)),
    FINISHED("Finalizado", Color(0xFF0B7285)),
}

object MockData {
    val stats = listOf(
        StatCard("En Reparacion", 12, Color(0xFF7B2CBF), "ER"),
        StatCard("Pendientes", 8, Color(0xFF1C7ED6), "PE"),
        StatCard("Desestimados", 2, Color(0xFFFA5252), "DS"),
        StatCard("Listos (Sin Retirar)", 5, Color(0xFF12B886), "LR"),
        StatCard("Total Equipos del Mes", 37, Color(0xFFF08C00), "TM"),
    )

    val equipment = listOf(
        EquipmentRow(
            client = "Maria L",
            phone = "55 1122 3344",
            brand = "Samsung",
            model = "A52",
            issue = "Pantalla negra",
            serial = "SN-A52-9931",
            status = RepairStatus.REPAIRING,
            intakeDate = "2026-03-01",
            cost = "$1200",
        ),
        EquipmentRow(
            client = "Jose R",
            phone = "55 6677 2211",
            brand = "Apple",
            model = "iPhone 12",
            issue = "Bateria",
            serial = "IMEI-4511",
            status = RepairStatus.WAITING_PARTS,
            intakeDate = "2026-02-27",
            cost = "$2200",
        ),
        EquipmentRow(
            client = "Laura P",
            phone = "55 9988 4411",
            brand = "Lenovo",
            model = "IdeaPad",
            issue = "No enciende",
            serial = "SN-7781",
            status = RepairStatus.DIAGNOSIS,
            intakeDate = "2026-02-26",
            cost = "$1800",
        ),
        EquipmentRow(
            client = "Carlos M",
            phone = "55 7755 8800",
            brand = "Xiaomi",
            model = "Note 11",
            issue = "Carga lenta",
            serial = "IMEI-8899",
            status = RepairStatus.RECEIVED,
            intakeDate = "2026-03-02",
            cost = "$650",
        ),
        EquipmentRow(
            client = "Paola G",
            phone = "55 4455 6677",
            brand = "HP",
            model = "Pavilion 15",
            issue = "Teclado",
            serial = "SN-HP-4521",
            status = RepairStatus.READY,
            intakeDate = "2026-02-20",
            cost = "$950",
        ),
    )

    val billing = listOf(
        EquipmentRow(
            client = "Paola G",
            phone = "55 4455 6677",
            brand = "HP",
            model = "Pavilion 15",
            issue = "Teclado",
            serial = "SN-HP-4521",
            status = RepairStatus.READY,
            intakeDate = "2026-02-20",
            cost = "$950",
        ),
        EquipmentRow(
            client = "Mario V",
            phone = "55 2211 3344",
            brand = "Motorola",
            model = "G9",
            issue = "Camara",
            serial = "IMEI-3344",
            status = RepairStatus.READY,
            intakeDate = "2026-02-25",
            cost = "$780",
        ),
    )

    val appointments = listOf(
        AppointmentRow("2026-03-04 10:30", "Diana S", "Revision general", "Confirmada"),
        AppointmentRow("2026-03-04 13:00", "Luis A", "Pantalla rota", "Pendiente"),
        AppointmentRow("2026-03-05 09:00", "Fernanda T", "Mantenimiento", "Confirmada"),
    )
}
