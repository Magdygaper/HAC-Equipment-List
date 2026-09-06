package com.example.hacequipment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hacequipment.data.Screen
import com.example.hacequipment.ui.EquipmentViewModel
import com.example.hacequipment.ui.dialogs.AddEditEquipmentDialog
import com.example.hacequipment.ui.dialogs.CategoryBreakdownDialog
import com.example.hacequipment.ui.dialogs.EquipmentHistoryDialog
import com.example.hacequipment.ui.dialogs.TransferDialog
import com.example.hacequipment.ui.screens.DashboardScreen
import com.example.hacequipment.ui.screens.EquipmentListScreen
import com.example.hacequipment.ui.screens.TransferLogScreen
import com.example.hacequipment.ui.theme.HacAccentAmber
import com.example.hacequipment.ui.theme.HacBg
import com.example.hacequipment.ui.theme.HacBorder
import com.example.hacequipment.ui.theme.HacEquipmentTheme
import com.example.hacequipment.ui.theme.HacGreen
import com.example.hacequipment.ui.theme.HacRed
import com.example.hacequipment.ui.theme.HacSurface
import com.example.hacequipment.ui.theme.HacSurfaceElevated
import com.example.hacequipment.ui.theme.HacText
import com.example.hacequipment.ui.theme.HacTextMuted

class MainActivity : ComponentActivity() {

    private val viewModel: EquipmentViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HacEquipmentTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val filterState by viewModel.filterState.collectAsState()
                val isAdmin by viewModel.isAdmin.collectAsState()

                val allEquipment by viewModel.equipmentList.collectAsState()
                val filteredEquipment by viewModel.filteredEquipment.collectAsState()
                val transfers by viewModel.transfers.collectAsState()

                val equipmentToTransfer by viewModel.equipmentToTransfer.collectAsState()
                val equipmentToEdit by viewModel.equipmentToEdit.collectAsState()
                val isAddEquipmentOpen by viewModel.isAddEquipmentOpen.collectAsState()
                val selectedEquipmentForHistory by viewModel.selectedEquipmentForHistory.collectAsState()
                val selectedCategoryForBreakdown by viewModel.selectedCategoryForBreakdown.collectAsState()
                val userMessage by viewModel.userMessage.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(userMessage) {
                    userMessage?.let {
                        snackbarHostState.showSnackbar(it)
                        viewModel.dismissMessage()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HacBg),
                    containerColor = HacBg,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .background(HacAccentAmber, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "HAC · PMV",
                                            color = HacBg,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Equipment List",
                                        color = HacText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            actions = {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .background(
                                            if (isAdmin) HacAccentAmber.copy(alpha = 0.15f) else HacSurfaceElevated,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isAdmin) HacAccentAmber.copy(alpha = 0.4f) else HacBorder,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { viewModel.toggleAdmin() }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                        .testTag("role_badge_btn")
                                ) {
                                    Text(
                                        text = if (isAdmin) "Admin" else "Viewer",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isAdmin) HacAccentAmber else HacTextMuted
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = HacSurface,
                                titleContentColor = HacText
                            ),
                            modifier = Modifier.testTag("app_top_bar")
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = HacSurface,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == Screen.DASHBOARD,
                                onClick = { viewModel.setScreen(Screen.DASHBOARD) },
                                icon = {
                                    Icon(
                                        imageVector = if (currentScreen == Screen.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                                        contentDescription = "Dashboard"
                                    )
                                },
                                label = { Text("Dashboard", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = HacBg,
                                    selectedTextColor = HacAccentAmber,
                                    indicatorColor = HacAccentAmber,
                                    unselectedIconColor = HacTextMuted,
                                    unselectedTextColor = HacTextMuted
                                ),
                                modifier = Modifier.testTag("nav_dashboard")
                            )

                            NavigationBarItem(
                                selected = currentScreen == Screen.EQUIPMENT,
                                onClick = { viewModel.setScreen(Screen.EQUIPMENT) },
                                icon = {
                                    Icon(
                                        imageVector = if (currentScreen == Screen.EQUIPMENT) Icons.Filled.Construction else Icons.Outlined.Construction,
                                        contentDescription = "Equipment List"
                                    )
                                },
                                label = { Text("Equipment", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = HacBg,
                                    selectedTextColor = HacAccentAmber,
                                    indicatorColor = HacAccentAmber,
                                    unselectedIconColor = HacTextMuted,
                                    unselectedTextColor = HacTextMuted
                                ),
                                modifier = Modifier.testTag("nav_equipment")
                            )

                            NavigationBarItem(
                                selected = currentScreen == Screen.TRANSFERS,
                                onClick = { viewModel.setScreen(Screen.TRANSFERS) },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (transfers.isNotEmpty()) {
                                                Badge(
                                                    containerColor = HacRed,
                                                    contentColor = HacText
                                                ) {
                                                    Text("${transfers.size}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (currentScreen == Screen.TRANSFERS) Icons.Filled.SyncAlt else Icons.Outlined.SyncAlt,
                                            contentDescription = "Transfer Log"
                                        )
                                    }
                                },
                                label = { Text("Transfers", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = HacBg,
                                    selectedTextColor = HacAccentAmber,
                                    indicatorColor = HacAccentAmber,
                                    unselectedIconColor = HacTextMuted,
                                    unselectedTextColor = HacTextMuted
                                ),
                                modifier = Modifier.testTag("nav_transfers")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.DASHBOARD -> {
                                DashboardScreen(
                                    equipmentList = allEquipment,
                                    transfers = transfers,
                                    filterState = filterState,
                                    onCategoryFilter = { viewModel.setCategoryFilter(it) },
                                    onOdFilter = { viewModel.setOdFilter(it) },
                                    onProjectFilter = { viewModel.setProjectFilter(it) },
                                    onClearAllFilters = { viewModel.clearAllFilters() },
                                    onBreakdownClick = { viewModel.openCategoryBreakdown(it) },
                                    onNavigateToEquipment = { viewModel.setScreen(Screen.EQUIPMENT) }
                                )
                            }
                            Screen.EQUIPMENT -> {
                                EquipmentListScreen(
                                    equipmentList = filteredEquipment,
                                    filterState = filterState,
                                    isAdmin = isAdmin,
                                    onSearchChange = { viewModel.setSearchQuery(it) },
                                    onCategoryFilter = { viewModel.setCategoryFilter(it) },
                                    onOdFilter = { viewModel.setOdFilter(it) },
                                    onProjectFilter = { viewModel.setProjectFilter(it) },
                                    onClearAllFilters = { viewModel.clearAllFilters() },
                                    onAddEquipmentClick = { viewModel.openAddEquipment() },
                                    onEquipmentClick = { viewModel.openEquipmentHistory(it) },
                                    onTransferClick = { viewModel.openTransferDialog(it) },
                                    onEditClick = { viewModel.openEditEquipment(it) },
                                    onDeleteClick = { viewModel.deleteEquipment(it.id, it.code) }
                                )
                            }
                            Screen.TRANSFERS -> {
                                TransferLogScreen(
                                    transfers = transfers,
                                    isAdmin = isAdmin,
                                    onDeleteTransfer = { viewModel.deleteTransfer(it) }
                                )
                            }
                        }
                    }
                }

                // Modal: Transfer Equipment Dialog
                equipmentToTransfer?.let { eq ->
                    TransferDialog(
                        equipment = eq,
                        allProjects = viewModel.getActiveProjects(),
                        onDismiss = { viewModel.closeTransferDialog() },
                        onConfirm = { toProj, date, req, notes, att ->
                            viewModel.recordTransfer(eq.id, toProj, date, req, notes, att)
                        }
                    )
                }

                // Modal: Add / Edit Equipment Dialog
                if (isAddEquipmentOpen || equipmentToEdit != null) {
                    AddEditEquipmentDialog(
                        equipment = equipmentToEdit,
                        allProjects = viewModel.getActiveProjects(),
                        onDismiss = { viewModel.closeAddEditDialog() },
                        onSave = { item, isNew ->
                            viewModel.saveEquipment(item, isNew)
                        }
                    )
                }

                // Modal: Equipment Specs and Transfer Timeline
                selectedEquipmentForHistory?.let { eq ->
                    EquipmentHistoryDialog(
                        equipment = eq,
                        transfers = transfers,
                        isAdmin = isAdmin,
                        onDismiss = { viewModel.closeEquipmentHistory() },
                        onTransferClick = { viewModel.openTransferDialog(it) }
                    )
                }

                // Modal: Category Breakdown
                selectedCategoryForBreakdown?.let { cat ->
                    CategoryBreakdownDialog(
                        category = cat,
                        allEquipment = allEquipment,
                        onDismiss = { viewModel.closeCategoryBreakdown() },
                        onApplyFilter = {
                            viewModel.setCategoryFilter(it)
                            viewModel.setScreen(Screen.EQUIPMENT)
                        }
                    )
                }
            }
        }
    }
}
