package com.example.hacequipment

import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.FilterState
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.data.Transfer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EquipmentLogicTest {

    @Test
    fun testOdMappingResolution() {
        // Test exact and case-insensitive resolution for major Hassan Allam projects
        assertEquals("Eng. Ahmed Nezar", OdConfig.getOdForProject("Safaga Port"))
        assertEquals("Eng. Ahmed Nezar", OdConfig.getOdForProject("safaga port"))
        assertEquals("Eng. Ahmed Nezar", OdConfig.getOdForProject("TBM - Metro L4"))
        assertEquals("Eng. Hany Fayd", OdConfig.getOdForProject("El Dabaa"))
        assertEquals("Eng. Mohamed Mostafa", OdConfig.getOdForProject("Central Workshop"))
        assertEquals("Eng. Tamer Hagag", OdConfig.getOdForProject("Locanda"))
        assertEquals("Eng. Tamer Hagag", OdConfig.getOdForProject("Haptowen"))
        assertEquals("Eng. Mostafa Morsi", OdConfig.getOdForProject("Bab Masr"))
        assertEquals("Eng. Waled Fahmy", OdConfig.getOdForProject("Ras Elhekma"))
        assertEquals("", OdConfig.getOdForProject(""))
        assertEquals("", OdConfig.getOdForProject("Unknown Nonexistent Site"))
    }

    @Test
    fun testCategoriesAndColors() {
        assertEquals(8, OdConfig.CATEGORIES.size)
        assertTrue(OdConfig.CATEGORIES.contains("Earthmoving Equipment"))
        assertTrue(OdConfig.CATEGORIES.contains("Compaction Equipment"))
        assertTrue(OdConfig.CATEGORIES.contains("Power & Utilities"))
        assertTrue(OdConfig.CATEGORIES.contains("Concrete Equipment"))
        assertTrue(OdConfig.CATEGORIES.contains("Lifting Equipment"))
        assertTrue(OdConfig.CATEGORIES.contains("Ground Engineering"))
        assertTrue(OdConfig.CATEGORIES.contains("Special Equipment"))
        assertTrue(OdConfig.CATEGORIES.contains("Pumping Equipment"))

        for (cat in OdConfig.CATEGORIES) {
            val color = OdConfig.getCategoryColor(cat)
            assertNotNull(color)
        }
    }

    @Test
    fun testEquipmentFiltering() {
        val sampleList = listOf(
            Equipment(
                id = "1",
                category = "Earthmoving Equipment",
                equipment = "Backhoe",
                brand = "Bobcat B730",
                code = "BH/R0002",
                project = "Safaga Port",
                od = "Eng. Ahmed Nezar"
            ),
            Equipment(
                id = "2",
                category = "Compaction Equipment",
                equipment = "Compactor",
                brand = "CASE 1107EX-D",
                code = "CT0045",
                project = "El Dabaa",
                od = "Eng. Hany Fayd"
            ),
            Equipment(
                id = "3",
                category = "Power & Utilities",
                equipment = "Generator",
                brand = "Cummins C2500",
                code = "GN0121",
                project = "Central Workshop",
                od = "Eng. Mohamed Mostafa"
            )
        )

        // Filter by Category
        val earthmovingOnly = sampleList.filter { it.category == "Earthmoving Equipment" }
        assertEquals(1, earthmovingOnly.size)
        assertEquals("BH/R0002", earthmovingOnly.first().code)

        // Filter by Project
        val dabaaOnly = sampleList.filter { it.project == "El Dabaa" }
        assertEquals(1, dabaaOnly.size)
        assertEquals("CT0045", dabaaOnly.first().code)

        // Search query
        val searchGen = sampleList.filter {
            it.equipment.contains("gen", ignoreCase = true) || it.code.contains("gen", ignoreCase = true)
        }
        assertEquals(1, searchGen.size)
        assertEquals("GN0121", searchGen.first().code)
    }

    @Test
    fun testTransferHistoryDataModel() {
        val transfer = Transfer(
            id = "tr_test_1",
            equipmentId = "eq_1",
            equipmentName = "Backhoe",
            code = "BH/R0002",
            fromProject = "Soul",
            toProject = "Safaga Port",
            date = "2026-08-30",
            requestedBy = "Hamdy",
            attachmentUrl = "https://1drv.ms/test",
            notes = "Mobilization to site"
        )

        assertEquals("Soul", transfer.fromProject)
        assertEquals("Safaga Port", transfer.toProject)
        assertEquals("Hamdy", transfer.requestedBy)
        assertEquals("BH/R0002", transfer.code)
    }
}
