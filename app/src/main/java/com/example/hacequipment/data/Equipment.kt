package com.example.hacequipment.data

data class Equipment(
    val id: String,
    val sn: Int = 0,
    val category: String,
    val equipment: String,
    val brand: String = "",
    val code: String = "",
    val sap: String = "",
    val chassis: String = "",
    val engine: String = "",
    val engineSn: String = "",
    val year: String = "",
    val power: String = "",
    val project: String = "",
    val od: String = "",
    val costCenter: String = "",
    val value: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class Transfer(
    val id: String,
    val equipmentId: String,
    val equipmentName: String,
    val code: String,
    val fromProject: String,
    val toProject: String,
    val date: String,
    val requestedBy: String = "",
    val attachmentUrl: String = "",
    val notes: String = "",
    val byUser: String = "magdygaperarqam@gmail.com",
    val timestamp: Long = System.currentTimeMillis()
)

enum class Screen(val label: String) {
    DASHBOARD("Dashboard"),
    EQUIPMENT("Equipment List"),
    TRANSFERS("Transfer Log")
}

data class FilterState(
    val category: String? = null,
    val od: String? = null,
    val project: String? = null,
    val searchQuery: String = ""
)
