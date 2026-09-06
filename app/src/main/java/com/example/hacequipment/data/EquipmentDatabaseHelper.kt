package com.example.hacequipment.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.UUID

class EquipmentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "hac_equipment.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_EQUIPMENT = "equipment"
        const val COL_EQ_ID = "id"
        const val COL_EQ_SN = "sn"
        const val COL_EQ_CATEGORY = "category"
        const val COL_EQ_EQUIPMENT = "equipment"
        const val COL_EQ_BRAND = "brand"
        const val COL_EQ_CODE = "code"
        const val COL_EQ_SAP = "sap"
        const val COL_EQ_CHASSIS = "chassis"
        const val COL_EQ_ENGINE = "engine"
        const val COL_EQ_ENGINE_SN = "engine_sn"
        const val COL_EQ_YEAR = "year"
        const val COL_EQ_POWER = "power"
        const val COL_EQ_PROJECT = "project"
        const val COL_EQ_OD = "od"
        const val COL_EQ_COST_CENTER = "cost_center"
        const val COL_EQ_VALUE = "value"
        const val COL_EQ_CREATED_AT = "created_at"

        const val TABLE_TRANSFERS = "transfers"
        const val COL_TR_ID = "id"
        const val COL_TR_EQ_ID = "equipment_id"
        const val COL_TR_EQ_NAME = "equipment_name"
        const val COL_TR_CODE = "code"
        const val COL_TR_FROM = "from_project"
        const val COL_TR_TO = "to_project"
        const val COL_TR_DATE = "date"
        const val COL_TR_REQUESTED_BY = "requested_by"
        const val COL_TR_ATTACHMENT = "attachment_url"
        const val COL_TR_NOTES = "notes"
        const val COL_TR_BY_USER = "by_user"
        const val COL_TR_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_EQUIPMENT (
                $COL_EQ_ID TEXT PRIMARY KEY,
                $COL_EQ_SN INTEGER,
                $COL_EQ_CATEGORY TEXT NOT NULL,
                $COL_EQ_EQUIPMENT TEXT NOT NULL,
                $COL_EQ_BRAND TEXT,
                $COL_EQ_CODE TEXT,
                $COL_EQ_SAP TEXT,
                $COL_EQ_CHASSIS TEXT,
                $COL_EQ_ENGINE TEXT,
                $COL_EQ_ENGINE_SN TEXT,
                $COL_EQ_YEAR TEXT,
                $COL_EQ_POWER TEXT,
                $COL_EQ_PROJECT TEXT,
                $COL_EQ_OD TEXT,
                $COL_EQ_COST_CENTER TEXT,
                $COL_EQ_VALUE TEXT,
                $COL_EQ_CREATED_AT INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_TRANSFERS (
                $COL_TR_ID TEXT PRIMARY KEY,
                $COL_TR_EQ_ID TEXT NOT NULL,
                $COL_TR_EQ_NAME TEXT NOT NULL,
                $COL_TR_CODE TEXT,
                $COL_TR_FROM TEXT,
                $COL_TR_TO TEXT NOT NULL,
                $COL_TR_DATE TEXT NOT NULL,
                $COL_TR_REQUESTED_BY TEXT,
                $COL_TR_ATTACHMENT TEXT,
                $COL_TR_NOTES TEXT,
                $COL_TR_BY_USER TEXT,
                $COL_TR_TIMESTAMP INTEGER
            )
            """.trimIndent()
        )

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSFERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EQUIPMENT")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val initialItems = listOf(
            Equipment(
                id = "eq_001",
                sn = 1,
                category = "Earthmoving Equipment",
                equipment = "Backhoe",
                brand = "Bobcat B730",
                code = "BH/R0002",
                sap = "REEBH61-BH/R0002",
                chassis = "B3S911044",
                engine = "Perkins 1104D",
                year = "2019",
                power = "100 HP",
                project = "Safaga Port",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ001",
                value = "85000"
            ),
            Equipment(
                id = "eq_002",
                sn = 2,
                category = "Earthmoving Equipment",
                equipment = "Backhoe",
                brand = "Bobcat B730",
                code = "BH/R0003",
                sap = "REEBH61-BH/R0003",
                chassis = "B3S911052",
                engine = "Perkins 1104D",
                year = "2020",
                power = "100 HP",
                project = "Safaga Port",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ001",
                value = "90000"
            ),
            Equipment(
                id = "eq_003",
                sn = 3,
                category = "Earthmoving Equipment",
                equipment = "Backhoe",
                brand = "Bobcat B730",
                code = "BH/R0005",
                sap = "REEBH61-BH/R0005",
                chassis = "B3S911060",
                engine = "Perkins 1104D",
                year = "2020",
                power = "100 HP",
                project = "Metro Line 4",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ001",
                value = "90000"
            ),
            Equipment(
                id = "eq_004",
                sn = 4,
                category = "Earthmoving Equipment",
                equipment = "Excavator",
                brand = "CAT 320D3",
                code = "EX0012",
                sap = "REEEX70-EX0012",
                chassis = "CAT0320DCJ301",
                engine = "CAT C4.4 ACERT",
                year = "2021",
                power = "143 HP",
                project = "El Dabaa",
                od = "Eng. Hany Fayd",
                costCenter = "HACEQ002",
                value = "175000"
            ),
            Equipment(
                id = "eq_005",
                sn = 5,
                category = "Earthmoving Equipment",
                equipment = "Wheel Loader",
                brand = "Komatsu WA380-6",
                code = "LD0008",
                sap = "REELD55-LD0008",
                chassis = "WA380-70231",
                engine = "Komatsu SAA6D107E-1",
                year = "2020",
                power = "191 HP",
                project = "Central Workshop",
                od = "Eng. Mohamed Mostafa",
                costCenter = "HACEQ002",
                value = "160000"
            ),
            Equipment(
                id = "eq_006",
                sn = 6,
                category = "Compaction Equipment",
                equipment = "Compactor",
                brand = "CASE 1107EX-D",
                code = "CT0045",
                sap = "REECT40-CT0045",
                chassis = "N4CA1107E0045",
                engine = "FPT S8000 Tier 3",
                year = "2021",
                power = "102 HP",
                project = "El Dabaa",
                od = "Eng. Hany Fayd",
                costCenter = "HACEQ003",
                value = "65000"
            ),
            Equipment(
                id = "eq_007",
                sn = 7,
                category = "Compaction Equipment",
                equipment = "Compactor",
                brand = "CASE 1107EX-D",
                code = "CT0046",
                sap = "REECT40-CT0046",
                chassis = "N4CA1107E0046",
                engine = "FPT S8000 Tier 3",
                year = "2021",
                power = "102 HP",
                project = "Ras Elhekma",
                od = "Eng. Waled Fahmy",
                costCenter = "HACEQ003",
                value = "65000"
            ),
            Equipment(
                id = "eq_008",
                sn = 8,
                category = "Compaction Equipment",
                equipment = "Compactor",
                brand = "BOMAG BW 211 D-5",
                code = "CT0048",
                sap = "REECT40-CT0048",
                chassis = "101584021048",
                engine = "Deutz TCD 3.6 L4",
                year = "2022",
                power = "128 HP",
                project = "Benban 200 MW",
                od = "Eng. Hany Fayd",
                costCenter = "HACEQ003",
                value = "72000"
            ),
            Equipment(
                id = "eq_009",
                sn = 9,
                category = "Power & Utilities",
                equipment = "Generator",
                brand = "Cummins C2500 D5A",
                code = "GN0121",
                sap = "REGN80-GN0121",
                chassis = "CUM-2500-8841",
                engine = "Cummins QSK60-G8",
                year = "2022",
                power = "2500 KVA",
                project = "Central Workshop",
                od = "Eng. Mohamed Mostafa",
                costCenter = "HACEQ004",
                value = "240000"
            ),
            Equipment(
                id = "eq_010",
                sn = 10,
                category = "Power & Utilities",
                equipment = "Generator",
                brand = "FG Wilson P550-3",
                code = "GN0089",
                sap = "REGN80-GN0089",
                chassis = "FGW550-99321",
                engine = "Perkins 2506A-E15TAG2",
                year = "2020",
                power = "500 KVA",
                project = "Haptowen",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ004",
                value = "68000"
            ),
            Equipment(
                id = "eq_011",
                sn = 11,
                category = "Power & Utilities",
                equipment = "Compressor",
                brand = "Doosan C185WKUB-EX-T2",
                code = "CS0034",
                sap = "RECS30-CS0034",
                chassis = "DOOS-C185-110",
                engine = "Kubota V2403",
                year = "2020",
                power = "185 CFM",
                project = "Silver Sand",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ004",
                value = "28000"
            ),
            Equipment(
                id = "eq_012",
                sn = 12,
                category = "Concrete Equipment",
                equipment = "Mobile Concrete Pump",
                brand = "Putzmeister BSF 42-5.16H",
                code = "CP0014",
                sap = "RECP90-CP0014",
                chassis = "PM42-88741",
                engine = "Mercedes OM501LA",
                year = "2021",
                power = "42m Boom / 160 m3/h",
                project = "Alamein Station",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ005",
                value = "380000"
            ),
            Equipment(
                id = "eq_013",
                sn = 13,
                category = "Concrete Equipment",
                equipment = "Batch Plant",
                brand = "Liebherr Betomix 2.5",
                code = "BP0003",
                sap = "REBP95-BP0003",
                chassis = "LB-BET-25-03",
                engine = "Electric 110 kW",
                year = "2019",
                power = "100 m3/h",
                project = "Central Workshop",
                od = "Eng. Mohamed Mostafa",
                costCenter = "HACEQ005",
                value = "450000"
            ),
            Equipment(
                id = "eq_014",
                sn = 14,
                category = "Lifting Equipment",
                equipment = "Mobile Crane",
                brand = "Liebherr LTM 1100-5.2",
                code = "CR0021",
                sap = "RECR99-CR0021",
                chassis = "LTM-1100-721",
                engine = "Liebherr D936L A6",
                year = "2021",
                power = "100 Ton",
                project = "Hard 350",
                od = "Eng. Hamdy Swillam",
                costCenter = "HACEQ006",
                value = "620000"
            ),
            Equipment(
                id = "eq_015",
                sn = 15,
                category = "Lifting Equipment",
                equipment = "Tower Crane",
                brand = "Potain MCT 205",
                code = "TC0007",
                sap = "RETC90-TC0007",
                chassis = "POT-205-007",
                engine = "Electric 45 kW",
                year = "2020",
                power = "10 Ton / 65m Jib",
                project = "swan Lake - 2",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ006",
                value = "290000"
            ),
            Equipment(
                id = "eq_016",
                sn = 16,
                category = "Lifting Equipment",
                equipment = "Forklift",
                brand = "Toyota 8FD30",
                code = "FL0018",
                sap = "REFL20-FL0018",
                chassis = "TOY-8FD-3018",
                engine = "Toyota 1DZ-II",
                year = "2022",
                power = "3.0 Ton",
                project = "Ware House( 10th of ramadan )",
                od = "Eng. Mohamed Mostafa",
                costCenter = "HACEQ006",
                value = "34000"
            ),
            Equipment(
                id = "eq_017",
                sn = 17,
                category = "Ground Engineering",
                equipment = "Clam shell Bucket",
                brand = "Peiner Smog Zs G115-1.8-4000",
                code = "HU0001",
                sap = "REHU75-HU0001",
                chassis = "PEIN-115-001",
                engine = "Hydraulic 250 bar",
                year = "2021",
                power = "1.8 m3 / 4.0m",
                project = "TBM - Metro L4",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ007",
                value = "115000"
            ),
            Equipment(
                id = "eq_018",
                sn = 18,
                category = "Ground Engineering",
                equipment = "Clam shell Bucket",
                brand = "Peiner Smog Zs G115-1.8-4000",
                code = "HU0002",
                sap = "REHU75-HU0002",
                chassis = "PEIN-115-002",
                engine = "Hydraulic 250 bar",
                year = "2021",
                power = "1.8 m3 / 4.0m",
                project = "TBM - Metro L4",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ007",
                value = "115000"
            ),
            Equipment(
                id = "eq_019",
                sn = 19,
                category = "Ground Engineering",
                equipment = "Vibratory Hammer",
                brand = "PTC 30HFV",
                code = "VH0002",
                sap = "REVH80-VH0002",
                chassis = "PTC-30HF-110",
                engine = "Hydraulic 350 bar",
                year = "2020",
                power = "600 kW",
                project = "Safaga Port",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ007",
                value = "195000"
            ),
            Equipment(
                id = "eq_020",
                sn = 20,
                category = "Pumping Equipment",
                equipment = "Dewatering Slurry Pump",
                brand = "Flygt 3153 HT",
                code = "PP0005",
                sap = "REPP15-PP0005",
                chassis = "FLYGT-3153-90",
                engine = "Electric Submersible 13.5 kW",
                year = "2021",
                power = "60 l/s",
                project = "Ras Elhekma",
                od = "Eng. Waled Fahmy",
                costCenter = "HACEQ008",
                value = "18000"
            ),
            Equipment(
                id = "eq_021",
                sn = 21,
                category = "Special Equipment",
                equipment = "TBM Auxiliary Gantry",
                brand = "Herrenknecht EPB S-1025",
                code = "TBM001",
                sap = "RETBM-1025",
                chassis = "HK-S1025-01",
                engine = "Electric 4200 kW",
                year = "2022",
                power = "Dia 9.8m",
                project = "TBM - Metro L4",
                od = "Eng. Ahmed Nezar",
                costCenter = "HACEQ009",
                value = "1850000"
            ),
            Equipment(
                id = "eq_022",
                sn = 22,
                category = "Earthmoving Equipment",
                equipment = "Backhoe",
                brand = "Bobcat B730",
                code = "BH0009",
                sap = "REEBH61-BH0009",
                chassis = "B3S911099",
                engine = "Perkins 1104D",
                year = "2020",
                power = "100 HP",
                project = "Locanda",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ001",
                value = "90000"
            ),
            Equipment(
                id = "eq_023",
                sn = 23,
                category = "Earthmoving Equipment",
                equipment = "Motor Grader",
                brand = "CAT 140K",
                code = "GR0004",
                sap = "REGR40-GR0004",
                chassis = "CAT140K-SZL04",
                engine = "CAT C7 ACERT",
                year = "2021",
                power = "190 HP",
                project = "Soul",
                od = "Eng. Tamer Hagag",
                costCenter = "HACEQ001",
                value = "190000"
            ),
            Equipment(
                id = "eq_024",
                sn = 24,
                category = "Special Equipment",
                equipment = "Sweeper Truck",
                brand = "Dulevo 5000 Evolution",
                code = "SW0002",
                sap = "RESW50-SW0002",
                chassis = "DUL-5000-02",
                engine = "VM R754EU6",
                year = "2020",
                power = "107 HP",
                project = "R05",
                od = "Eng. Mostafa Morsi",
                costCenter = "HACEQ009",
                value = "110000"
            )
        )

        for (eq in initialItems) {
            val cv = ContentValues().apply {
                put(COL_EQ_ID, eq.id)
                put(COL_EQ_SN, eq.sn)
                put(COL_EQ_CATEGORY, eq.category)
                put(COL_EQ_EQUIPMENT, eq.equipment)
                put(COL_EQ_BRAND, eq.brand)
                put(COL_EQ_CODE, eq.code)
                put(COL_EQ_SAP, eq.sap)
                put(COL_EQ_CHASSIS, eq.chassis)
                put(COL_EQ_ENGINE, eq.engine)
                put(COL_EQ_ENGINE_SN, eq.engineSn)
                put(COL_EQ_YEAR, eq.year)
                put(COL_EQ_POWER, eq.power)
                put(COL_EQ_PROJECT, eq.project)
                put(COL_EQ_OD, eq.od)
                put(COL_EQ_COST_CENTER, eq.costCenter)
                put(COL_EQ_VALUE, eq.value)
                put(COL_EQ_CREATED_AT, eq.createdAt)
            }
            db.insert(TABLE_EQUIPMENT, null, cv)
        }

        val initialTransfers = listOf(
            Transfer(
                id = "tr_001",
                equipmentId = "eq_001",
                equipmentName = "Backhoe",
                code = "BH/R0002",
                fromProject = "Soul",
                toProject = "Safaga Port",
                date = "2026-08-30",
                requestedBy = "Hamdy",
                attachmentUrl = "https://1drv.ms/b/c/d37ba50bf773fe0b/IQC-hyHB35ScQpohJN6CuchFAWhCkXjYs7sdPo3QBoJ3PG8",
                notes = "Mobilization to Safaga Port project",
                byUser = "magdygaperarqam@gmail.com",
                timestamp = System.currentTimeMillis() - 86400000L * 6
            ),
            Transfer(
                id = "tr_002",
                equipmentId = "eq_001",
                equipmentName = "Backhoe",
                code = "BH/R0002",
                fromProject = "Soul",
                toProject = "Soul",
                date = "2026-08-30",
                requestedBy = "Hamdy",
                attachmentUrl = "https://res.cloudinary.com/cpdekxev/raw/upload/fl_attachment/v1788127707/yowecxc8hfz3fesnpvis.pdf",
                notes = "Maintenance routine completed on site",
                byUser = "magdygaperarqam@gmail.com",
                timestamp = System.currentTimeMillis() - 86400000L * 6
            ),
            Transfer(
                id = "tr_003",
                equipmentId = "eq_001",
                equipmentName = "Backhoe",
                code = "BH/R0002",
                fromProject = "Ras Elhekma",
                toProject = "Soul",
                date = "2026-08-30",
                requestedBy = "Hamdy",
                attachmentUrl = "https://res.cloudinary.com/cpdekxev/raw/upload/fl_attachment/v1788127529/rc5n5gcmryjfwztexmme.pdf",
                notes = "Urgent transfer for excavation phase",
                byUser = "magdygaperarqam@gmail.com",
                timestamp = System.currentTimeMillis() - 86400000L * 7
            ),
            Transfer(
                id = "tr_004",
                equipmentId = "eq_001",
                equipmentName = "Backhoe",
                code = "BH/R0002",
                fromProject = "Ras Elhekma",
                toProject = "Ras Elhekma",
                date = "2026-08-30",
                requestedBy = "Morshed",
                attachmentUrl = "https://1drv.ms/b/c/d37ba50bf773fe0b/IQDyGCqGth0VQIGYnFzEXLCFAeegA0LRUTkowM2pJl6k2jk",
                notes = "Internal sector reassignment",
                byUser = "magdygaperarqam@gmail.com",
                timestamp = System.currentTimeMillis() - 86400000L * 7
            ),
            Transfer(
                id = "tr_005",
                equipmentId = "eq_006",
                equipmentName = "Compactor",
                code = "CT0045",
                fromProject = "Central Workshop",
                toProject = "El Dabaa",
                date = "2026-08-28",
                requestedBy = "Eng. Hany Fayd",
                attachmentUrl = "",
                notes = "Subgrade compaction works deployment",
                byUser = "magdygaperarqam@gmail.com",
                timestamp = System.currentTimeMillis() - 86400000L * 9
            )
        )

        for (tr in initialTransfers) {
            val cv = ContentValues().apply {
                put(COL_TR_ID, tr.id)
                put(COL_TR_EQ_ID, tr.equipmentId)
                put(COL_TR_EQ_NAME, tr.equipmentName)
                put(COL_TR_CODE, tr.code)
                put(COL_TR_FROM, tr.fromProject)
                put(COL_TR_TO, tr.toProject)
                put(COL_TR_DATE, tr.date)
                put(COL_TR_REQUESTED_BY, tr.requestedBy)
                put(COL_TR_ATTACHMENT, tr.attachmentUrl)
                put(COL_TR_NOTES, tr.notes)
                put(COL_TR_BY_USER, tr.byUser)
                put(COL_TR_TIMESTAMP, tr.timestamp)
            }
            db.insert(TABLE_TRANSFERS, null, cv)
        }
    }
}
