package com.example.hacequipment.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacequipment.data.Equipment
import com.example.hacequipment.data.EquipmentRepository
import com.example.hacequipment.data.FilterState
import com.example.hacequipment.data.OdConfig
import com.example.hacequipment.data.Screen
import com.example.hacequipment.data.Transfer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EquipmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EquipmentRepository(application)

    val equipmentList: StateFlow<List<Equipment>> = repository.equipment
    val transfers: StateFlow<List<Transfer>> = repository.transfers

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _isAdmin = MutableStateFlow(true)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _equipmentToTransfer = MutableStateFlow<Equipment?>(null)
    val equipmentToTransfer: StateFlow<Equipment?> = _equipmentToTransfer.asStateFlow()

    private val _equipmentToEdit = MutableStateFlow<Equipment?>(null)
    val equipmentToEdit: StateFlow<Equipment?> = _equipmentToEdit.asStateFlow()

    private val _isAddEquipmentOpen = MutableStateFlow(false)
    val isAddEquipmentOpen: StateFlow<Boolean> = _isAddEquipmentOpen.asStateFlow()

    private val _selectedEquipmentForHistory = MutableStateFlow<Equipment?>(null)
    val selectedEquipmentForHistory: StateFlow<Equipment?> = _selectedEquipmentForHistory.asStateFlow()

    private val _selectedCategoryForBreakdown = MutableStateFlow<String?>(null)
    val selectedCategoryForBreakdown: StateFlow<String?> = _selectedCategoryForBreakdown.asStateFlow()

    private val _selectedTypeForDetails = MutableStateFlow<String?>(null)
    val selectedTypeForDetails: StateFlow<String?> = _selectedTypeForDetails.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    val filteredEquipment: StateFlow<List<Equipment>> = combine(
        equipmentList,
        filterState
    ) { list, filter ->
        list.filter { eq ->
            val matchCat = filter.category == null || eq.category.equals(filter.category, ignoreCase = true)
            val matchOd = filter.od == null || eq.od.equals(filter.od, ignoreCase = true)
            val matchProj = filter.project == null || eq.project.equals(filter.project, ignoreCase = true)
            val matchQuery = if (filter.searchQuery.isBlank()) true else {
                val q = filter.searchQuery.trim().lowercase()
                eq.equipment.lowercase().contains(q) ||
                        eq.code.lowercase().contains(q) ||
                        eq.brand.lowercase().contains(q) ||
                        eq.sap.lowercase().contains(q) ||
                        eq.project.lowercase().contains(q) ||
                        eq.od.lowercase().contains(q) ||
                        eq.chassis.lowercase().contains(q) ||
                        eq.year.lowercase().contains(q)
            }
            matchCat && matchOd && matchProj && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setCategoryFilter(category: String?) {
        _filterState.value = _filterState.value.copy(
            category = if (_filterState.value.category == category) null else category
        )
    }

    fun setOdFilter(od: String?) {
        _filterState.value = _filterState.value.copy(
            od = if (_filterState.value.od == od) null else od
        )
    }

    fun setProjectFilter(project: String?) {
        _filterState.value = _filterState.value.copy(
            project = if (_filterState.value.project == project) null else project
        )
    }

    fun setSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun clearAllFilters() {
        _filterState.value = FilterState()
    }

    fun toggleAdmin() {
        _isAdmin.value = !_isAdmin.value
        _userMessage.value = if (_isAdmin.value) "Switched to Admin Mode" else "Switched to Viewer Mode"
    }

    fun openTransferDialog(equipment: Equipment) {
        _equipmentToTransfer.value = equipment
    }

    fun closeTransferDialog() {
        _equipmentToTransfer.value = null
    }

    fun openAddEquipment() {
        _isAddEquipmentOpen.value = true
    }

    fun openEditEquipment(equipment: Equipment) {
        _equipmentToEdit.value = equipment
    }

    fun closeAddEditDialog() {
        _isAddEquipmentOpen.value = false
        _equipmentToEdit.value = null
    }

    fun openEquipmentHistory(equipment: Equipment) {
        _selectedEquipmentForHistory.value = equipment
    }

    fun closeEquipmentHistory() {
        _selectedEquipmentForHistory.value = null
    }

    fun openCategoryBreakdown(category: String) {
        _selectedCategoryForBreakdown.value = category
    }

    fun closeCategoryBreakdown() {
        _selectedCategoryForBreakdown.value = null
    }

    fun openTypeDetails(type: String) {
        _selectedTypeForDetails.value = type
    }

    fun closeTypeDetails() {
        _selectedTypeForDetails.value = null
    }

    fun saveEquipment(equipment: Equipment, isNew: Boolean) {
        viewModelScope.launch {
            repository.saveEquipment(equipment, isNew)
            closeAddEditDialog()
            _userMessage.value = if (isNew) "Equipment ${equipment.code} added" else "Equipment ${equipment.code} updated"
        }
    }

    fun deleteEquipment(id: String, code: String) {
        viewModelScope.launch {
            repository.deleteEquipment(id)
            if (_selectedEquipmentForHistory.value?.id == id) {
                closeEquipmentHistory()
            }
            _userMessage.value = "Equipment $code deleted"
        }
    }

    fun recordTransfer(
        equipmentId: String,
        toProject: String,
        date: String,
        requestedBy: String,
        notes: String,
        attachmentUrl: String
    ) {
        viewModelScope.launch {
            val success = repository.recordTransfer(
                equipmentId = equipmentId,
                toProject = toProject,
                date = date,
                requestedBy = requestedBy,
                notes = notes,
                attachmentUrl = attachmentUrl,
                byUser = "magdygaperarqam@gmail.com"
            )
            if (success) {
                closeTransferDialog()
                _userMessage.value = "Transfer to $toProject logged successfully"
            }
        }
    }

    fun deleteTransfer(transferId: String) {
        viewModelScope.launch {
            repository.deleteTransfer(transferId)
            _userMessage.value = "Transfer deleted and equipment location reverted"
        }
    }

    fun dismissMessage() {
        _userMessage.value = null
    }

    fun getActiveProjects(): List<String> {
        val projectsFromDb = equipmentList.value.map { it.project }.filter { it.isNotBlank() }
        val projectsFromMap = OdConfig.OD_MAP.keys
        return (projectsFromDb + projectsFromMap).distinct().sorted()
    }

    fun getEquipmentTypes(): List<String> {
        return equipmentList.value.map { it.equipment }.filter { it.isNotBlank() }.distinct().sorted()
    }
}
