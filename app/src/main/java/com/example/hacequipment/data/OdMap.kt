package com.example.hacequipment.data

import androidx.compose.ui.graphics.Color

object OdConfig {
    val OD_MAP: Map<String, String> = mapOf(
        "Bab Masr" to "Eng. Mostafa Morsi",
        "El Bank El Araby" to "Eng. Mostafa Morsi",
        "Kayan" to "Eng. Mostafa Morsi",
        "Luxor" to "Eng. Mostafa Morsi",
        "Mansoura 2" to "Eng. Mostafa Morsi",
        "R05" to "Eng. Mostafa Morsi",
        "SCB" to "Eng. Mostafa Morsi",
        "Al Shohadaa Central hospital" to "Eng. Tamer Hagag",
        "Alamein Station" to "Eng. Tamer Hagag",
        "Haptowen" to "Eng. Tamer Hagag",
        "Locanda" to "Eng. Tamer Hagag",
        "Naser Institute" to "Eng. Tamer Hagag",
        "Silver Sand" to "Eng. Tamer Hagag",
        "Sofitel Hotel" to "Eng. Tamer Hagag",
        "Soul" to "Eng. Tamer Hagag",
        "swan Lake - 2" to "Eng. Tamer Hagag",
        "Wadi Natroun" to "Eng. Tamer Hagag",
        "Abou Oweikal" to "Eng. Ahmed Nezar",
        "Abou Qir JV" to "Eng. Ahmed Nezar",
        "Alex Tram" to "Eng. Ahmed Nezar",
        "El Ayaat Project" to "Eng. Ahmed Nezar",
        "intake mostakbal Misr" to "Eng. Ahmed Nezar",
        "Metro Line 4" to "Eng. Ahmed Nezar",
        "Mostakbal Masr Zone (G)" to "Eng. Ahmed Nezar",
        "Safaga Port" to "Eng. Ahmed Nezar",
        "Sokhna Port" to "Eng. Ahmed Nezar",
        "TBM - Metro L4" to "Eng. Ahmed Nezar",
        "Toshka" to "Eng. Ahmed Nezar",
        "Benban 200 MW" to "Eng. Hany Fayd",
        "El Dabaa" to "Eng. Hany Fayd",
        "marasy" to "Eng. Essam Ibrahim",
        "Ras Elhekma" to "Eng. Waled Fahmy",
        "Allam Block ( 10th of ramadan )" to "Eng. Mohamed Mostafa",
        "Allam Block ( Safaga Port )" to "Eng. Mohamed Mostafa",
        "Central Workshop" to "Eng. Mohamed Mostafa",
        "hassan allam complex" to "Eng. Mohamed Mostafa",
        "Heavy Transportation Workshop" to "Eng. Mohamed Mostafa",
        "Scaffolding" to "Eng. Mohamed Mostafa",
        "Ware House( 10th of ramadan )" to "Eng. Mohamed Mostafa",
        "10th OF RAMADAN" to "Eng. Mohamed Mostafa",
        "Sant Catreen" to "Eng. Abdelaziz Ibrahim",
        "Cairo Festival" to "Eng. Hamdy Swillam",
        "Hard 350" to "Eng. Hamdy Swillam",
        "Mansoura 1" to "Eng. Hamdy Swillam",
        "Mavida Garden" to "Eng. Hamdy Swillam",
        "Allam steel" to "Eng. Salah Shaker",
        "New Capital Tran-Hub" to "Eng. Mohamed Salem",
        "Afrexim Bank" to "Eng. Islam El Sayed",
        "Al Alamen Towers JV" to "Eng. Mohamed Abdellatif"
    )

    fun getOdForProject(project: String): String {
        if (project.isBlank()) return ""
        OD_MAP[project]?.let { return it }
        val lower = project.trim().lowercase()
        for ((k, v) in OD_MAP) {
            if (k.lowercase() == lower) return v
        }
        return ""
    }

    val CATEGORIES = listOf(
        "Earthmoving Equipment",
        "Compaction Equipment",
        "Power & Utilities",
        "Concrete Equipment",
        "Lifting Equipment",
        "Ground Engineering",
        "Special Equipment",
        "Pumping Equipment"
    )

    val CAT_COLOR_MAP: Map<String, Color> = mapOf(
        "Earthmoving Equipment" to Color(0xFFF0A500),
        "Compaction Equipment" to Color(0xFF58A6FF),
        "Power & Utilities" to Color(0xFF3FB950),
        "Concrete Equipment" to Color(0xFFE05C00),
        "Lifting Equipment" to Color(0xFFA371F7),
        "Ground Engineering" to Color(0xFF39D353),
        "Special Equipment" to Color(0xFFFF7B72),
        "Pumping Equipment" to Color(0xFFFFA657)
    )

    fun getCategoryColor(category: String): Color {
        return CAT_COLOR_MAP[category] ?: Color(0xFFF0A500)
    }
}
