package com.example.hacequipment.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EquipmentRepository(context: Context) {
    private val dbHelper = EquipmentDatabaseHelper(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _equipment = MutableStateFlow<List<Equipment>>(emptyList())
    val equipment: StateFlow<List<Equipment>> = _equipment.asStateFlow()

    private val _transfers = MutableStateFlow<List<Transfer>>(emptyList())
    val transfers: StateFlow<List<Transfer>> = _transfers.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        scope.launch {
            loadEquipment()
            loadTransfers()
        }
    }

    private suspend fun loadEquipment() = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            EquipmentDatabaseHelper.TABLE_EQUIPMENT,
            null, null, null, null, null,
            "${EquipmentDatabaseHelper.COL_EQ_SN} ASC"
        )
        val list = mutableListOf<Equipment>()
        cursor.use { c ->
            val idxId = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_ID)
            val idxSn = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_SN)
            val idxCat = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_CATEGORY)
            val idxEq = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_EQUIPMENT)
            val idxBrand = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_BRAND)
            val idxCode = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_CODE)
            val idxSap = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_SAP)
            val idxChassis = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_CHASSIS)
            val idxEngine = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_ENGINE)
            val idxEngineSn = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_ENGINE_SN)
            val idxYear = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_YEAR)
            val idxPower = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_POWER)
            val idxProj = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_PROJECT)
            val idxOd = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_OD)
            val idxCost = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_COST_CENTER)
            val idxVal = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_VALUE)
            val idxCreated = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_EQ_CREATED_AT)

            while (c.moveToNext()) {
                list.add(
                    Equipment(
                        id = c.getString(idxId),
                        sn = c.getInt(idxSn),
                        category = c.getString(idxCat),
                        equipment = c.getString(idxEq),
                        brand = c.getString(idxBrand) ?: "",
                        code = c.getString(idxCode) ?: "",
                        sap = c.getString(idxSap) ?: "",
                        chassis = c.getString(idxChassis) ?: "",
                        engine = c.getString(idxEngine) ?: "",
                        engineSn = c.getString(idxEngineSn) ?: "",
                        year = c.getString(idxYear) ?: "",
                        power = c.getString(idxPower) ?: "",
                        project = c.getString(idxProj) ?: "",
                        od = c.getString(idxOd) ?: "",
                        costCenter = c.getString(idxCost) ?: "",
                        value = c.getString(idxVal) ?: "",
                        createdAt = c.getLong(idxCreated)
                    )
                )
            }
        }
        _equipment.value = list
    }

    private suspend fun loadTransfers() = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            EquipmentDatabaseHelper.TABLE_TRANSFERS,
            null, null, null, null, null,
            "${EquipmentDatabaseHelper.COL_TR_TIMESTAMP} DESC"
        )
        val list = mutableListOf<Transfer>()
        cursor.use { c ->
            val idxId = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_ID)
            val idxEqId = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_EQ_ID)
            val idxEqName = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_EQ_NAME)
            val idxCode = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_CODE)
            val idxFrom = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_FROM)
            val idxTo = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_TO)
            val idxDate = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_DATE)
            val idxReq = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_REQUESTED_BY)
            val idxAtt = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_ATTACHMENT)
            val idxNotes = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_NOTES)
            val idxBy = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_BY_USER)
            val idxTime = c.getColumnIndexOrThrow(EquipmentDatabaseHelper.COL_TR_TIMESTAMP)

            while (c.moveToNext()) {
                list.add(
                    Transfer(
                        id = c.getString(idxId),
                        equipmentId = c.getString(idxEqId),
                        equipmentName = c.getString(idxEqName),
                        code = c.getString(idxCode) ?: "",
                        fromProject = c.getString(idxFrom) ?: "",
                        toProject = c.getString(idxTo) ?: "",
                        date = c.getString(idxDate) ?: "",
                        requestedBy = c.getString(idxReq) ?: "",
                        attachmentUrl = c.getString(idxAtt) ?: "",
                        notes = c.getString(idxNotes) ?: "",
                        byUser = c.getString(idxBy) ?: "",
                        timestamp = c.getLong(idxTime)
                    )
                )
            }
        }
        _transfers.value = list
    }

    suspend fun saveEquipment(item: Equipment, isNew: Boolean) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(EquipmentDatabaseHelper.COL_EQ_ID, item.id)
            put(EquipmentDatabaseHelper.COL_EQ_SN, item.sn)
            put(EquipmentDatabaseHelper.COL_EQ_CATEGORY, item.category)
            put(EquipmentDatabaseHelper.COL_EQ_EQUIPMENT, item.equipment)
            put(EquipmentDatabaseHelper.COL_EQ_BRAND, item.brand)
            put(EquipmentDatabaseHelper.COL_EQ_CODE, item.code)
            put(EquipmentDatabaseHelper.COL_EQ_SAP, item.sap)
            put(EquipmentDatabaseHelper.COL_EQ_CHASSIS, item.chassis)
            put(EquipmentDatabaseHelper.COL_EQ_ENGINE, item.engine)
            put(EquipmentDatabaseHelper.COL_EQ_ENGINE_SN, item.engineSn)
            put(EquipmentDatabaseHelper.COL_EQ_YEAR, item.year)
            put(EquipmentDatabaseHelper.COL_EQ_POWER, item.power)
            put(EquipmentDatabaseHelper.COL_EQ_PROJECT, item.project)
            val autoOd = OdConfig.getOdForProject(item.project)
            put(EquipmentDatabaseHelper.COL_EQ_OD, if (autoOd.isNotBlank()) autoOd else item.od)
            put(EquipmentDatabaseHelper.COL_EQ_COST_CENTER, item.costCenter)
            put(EquipmentDatabaseHelper.COL_EQ_VALUE, item.value)
            put(EquipmentDatabaseHelper.COL_EQ_CREATED_AT, item.createdAt)
        }
        if (isNew) {
            db.insert(EquipmentDatabaseHelper.TABLE_EQUIPMENT, null, cv)
        } else {
            db.update(
                EquipmentDatabaseHelper.TABLE_EQUIPMENT,
                cv,
                "${EquipmentDatabaseHelper.COL_EQ_ID} = ?",
                arrayOf(item.id)
            )
        }
        loadEquipment()
    }

    suspend fun deleteEquipment(id: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            EquipmentDatabaseHelper.TABLE_EQUIPMENT,
            "${EquipmentDatabaseHelper.COL_EQ_ID} = ?",
            arrayOf(id)
        )
        loadEquipment()
    }

    suspend fun recordTransfer(
        equipmentId: String,
        toProject: String,
        date: String,
        requestedBy: String,
        notes: String,
        attachmentUrl: String,
        byUser: String
    ): Boolean = withContext(Dispatchers.IO) {
        val currentEq = _equipment.value.find { it.id == equipmentId } ?: return@withContext false
        val db = dbHelper.writableDatabase
        val transferId = UUID.randomUUID().toString()
        val newOd = OdConfig.getOdForProject(toProject).ifBlank { currentEq.od }

        val trCv = ContentValues().apply {
            put(EquipmentDatabaseHelper.COL_TR_ID, transferId)
            put(EquipmentDatabaseHelper.COL_TR_EQ_ID, equipmentId)
            put(EquipmentDatabaseHelper.COL_TR_EQ_NAME, currentEq.equipment)
            put(EquipmentDatabaseHelper.COL_TR_CODE, currentEq.code)
            put(EquipmentDatabaseHelper.COL_TR_FROM, currentEq.project)
            put(EquipmentDatabaseHelper.COL_TR_TO, toProject)
            put(EquipmentDatabaseHelper.COL_TR_DATE, date)
            put(EquipmentDatabaseHelper.COL_TR_REQUESTED_BY, requestedBy)
            put(EquipmentDatabaseHelper.COL_TR_ATTACHMENT, attachmentUrl)
            put(EquipmentDatabaseHelper.COL_TR_NOTES, notes)
            put(EquipmentDatabaseHelper.COL_TR_BY_USER, byUser)
            put(EquipmentDatabaseHelper.COL_TR_TIMESTAMP, System.currentTimeMillis())
        }
        db.insert(EquipmentDatabaseHelper.TABLE_TRANSFERS, null, trCv)

        val eqCv = ContentValues().apply {
            put(EquipmentDatabaseHelper.COL_EQ_PROJECT, toProject)
            if (newOd.isNotBlank()) {
                put(EquipmentDatabaseHelper.COL_EQ_OD, newOd)
            }
        }
        db.update(
            EquipmentDatabaseHelper.TABLE_EQUIPMENT,
            eqCv,
            "${EquipmentDatabaseHelper.COL_EQ_ID} = ?",
            arrayOf(equipmentId)
        )

        loadEquipment()
        loadTransfers()
        true
    }

    suspend fun deleteTransfer(transferId: String) = withContext(Dispatchers.IO) {
        val transfer = _transfers.value.find { it.id == transferId }
        val db = dbHelper.writableDatabase

        if (transfer != null && transfer.fromProject.isNotBlank()) {
            val revertProject = transfer.fromProject
            val revertOd = OdConfig.getOdForProject(revertProject)
            val eqCv = ContentValues().apply {
                put(EquipmentDatabaseHelper.COL_EQ_PROJECT, revertProject)
                if (revertOd.isNotBlank()) {
                    put(EquipmentDatabaseHelper.COL_EQ_OD, revertOd)
                }
            }
            db.update(
                EquipmentDatabaseHelper.TABLE_EQUIPMENT,
                eqCv,
                "${EquipmentDatabaseHelper.COL_EQ_ID} = ?",
                arrayOf(transfer.equipmentId)
            )
        }

        db.delete(
            EquipmentDatabaseHelper.TABLE_TRANSFERS,
            "${EquipmentDatabaseHelper.COL_TR_ID} = ?",
            arrayOf(transferId)
        )

        loadEquipment()
        loadTransfers()
    }
}
