
package com.example.zafire.sistemadetallertecnico

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF0B7285),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD0EBFF),
    onPrimaryContainer = Color(0xFF102A43),
    secondary = Color(0xFF5F3DC4),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF7F4EF),
    onBackground = Color(0xFF1F2A37),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2A37),
    error = Color(0xFFB42318),
    onError = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF66D9E8),
    onPrimary = Color(0xFF003640),
    primaryContainer = Color(0xFF0B3954),
    onPrimaryContainer = Color(0xFFE6F7FF),
    secondary = Color(0xFFB197FC),
    onSecondary = Color(0xFF2F195F),
    background = Color(0xFF0B1116),
    onBackground = Color(0xFFE6E8EB),
    surface = Color(0xFF111A22),
    onSurface = Color(0xFFE6E8EB),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF2B0909)
)

private val AppTypography = Typography(
    headlineSmall = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif)
)

@Composable
fun App() {
    var isAuthenticated by remember { mutableStateOf(FirebaseAuthClient.hasActiveSession()) }
    var showRegister by remember { mutableStateOf(false) }
    var isDark by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = if (isDark) DarkColors else LightColors,
        typography = AppTypography
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (!isAuthenticated) {
                LoginScreen(
                    showRegister = showRegister,
                    onToggleRegister = { showRegister = !showRegister },
                    onLogin = { isAuthenticated = true }
                )
            } else {
                MainApp(
                    isDark = isDark,
                    onToggleDark = { isDark = !isDark },
                    onLogout = {
                        FirebaseAuthClient.signOut()
                        isAuthenticated = false
                    }
                )
            }
        }
    }
}

@Composable
private fun LoginScreen(
    showRegister: Boolean,
    onToggleRegister: () -> Unit,
    onLogin: () -> Unit
) {
    val firebaseEnabled = remember { FirebaseAuthClient.isConfigured() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChipLogo()
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (showRegister) "Crear Cuenta" else "Iniciar SesiÃ³n",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Pon tus credenciales para tener acceso a la base de datos",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("ContraseÃ±a") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(errorMessage != null) {
                        Text(
                            text = errorMessage.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = {
                            errorMessage = when {
                                email.isBlank() -> "Ingresa tu email."
                                password.isBlank() -> "Ingresa tu contrasena."
                                !firebaseEnabled -> "Configura Firebase para poder autenticar."
                                else -> null
                            }
                            if (errorMessage == null) {
                                isSubmitting = true
                                val authAction = if (showRegister) {
                                    FirebaseAuthClient::signUp
                                } else {
                                    FirebaseAuthClient::signIn
                                }
                                authAction(email.trim(), password) { firebaseError ->
                                    isSubmitting = false
                                    if (firebaseError == null) {
                                        onLogin()
                                    } else {
                                        errorMessage = firebaseError
                                    }
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (isSubmitting) {
                                "Procesando..."
                            } else if (showRegister) {
                                "Registrarse"
                            } else {
                                "Iniciar Sesion"
                            }
                        )
                    }

                    TextButton(
                        onClick = onToggleRegister,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (showRegister) {
                                "Ya tienes cuenta? Inicia sesion"
                            } else {
                                "Registrar nuevo usuario"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainApp(
    isDark: Boolean,
    onToggleDark: () -> Unit,
    onLogout: () -> Unit
) {
    var activeScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    var showEquipmentModal by remember { mutableStateOf(false) }
    var showPrintModal by remember { mutableStateOf(false) }
    var selectedRow by remember { mutableStateOf<EquipmentRow?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 900.dp
        var sidebarOpen by remember { mutableStateOf(!isMobile) }
        LaunchedEffect(isMobile) {
            sidebarOpen = !isMobile
        }
        val sidebarWidth = 260.dp
        val sidebarOffset by animateDpAsState(if (sidebarOpen) 0.dp else -sidebarWidth)

        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                if (!isMobile) {
                    Sidebar(
                        isDark = isDark,
                        activeScreen = activeScreen,
                        onSelect = { activeScreen = it },
                        onToggleDark = onToggleDark,
                        onLogout = onLogout,
                        modifier = Modifier.width(sidebarWidth)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (isMobile) {
                        MobileTopBar(onMenu = { sidebarOpen = true })
                    }
                    MainContent(
                        screen = activeScreen,
                        onNewEquipment = { showEquipmentModal = true },
                        onViewSheet = { row ->
                            selectedRow = row
                            showPrintModal = true
                        },
                        onEditEquipment = { row ->
                            selectedRow = row
                            showEquipmentModal = true
                        }
                    )
                }
            }

            if (isMobile) {
                AnimatedVisibility(sidebarOpen) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .noRippleClickable { sidebarOpen = false }
                    )
                }
                Sidebar(
                    isDark = isDark,
                    activeScreen = activeScreen,
                    onSelect = {
                        activeScreen = it
                        sidebarOpen = false
                    },
                    onToggleDark = onToggleDark,
                    onLogout = onLogout,
                    modifier = Modifier
                        .width(sidebarWidth)
                        .offset(x = sidebarOffset)
                        .fillMaxHeight(),
                    elevated = true
                )
            }

            if (showEquipmentModal) {
                EquipmentModal(
                    initialRow = selectedRow,
                    onDismiss = {
                        showEquipmentModal = false
                        selectedRow = null
                    }
                )
            }
            if (showPrintModal) {
                PrintModal(
                    row = selectedRow,
                    onDismiss = { showPrintModal = false }
                )
            }
        }
    }
}

@Composable
private fun MobileTopBar(onMenu: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("GlzRepair", style = MaterialTheme.typography.titleMedium)
            Text(
                "Panel Principal",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
        }
        FilledTonalButton(onClick = onMenu) {
            Text("Menu")
        }
    }
}

@Composable
private fun Sidebar(
    isDark: Boolean,
    activeScreen: AppScreen,
    onSelect: (AppScreen) -> Unit,
    onToggleDark: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    elevated: Boolean = false
) {
    val menuItems = remember {
        listOf(
            NavItem(AppScreen.DASHBOARD, "Dashboard", "DB"),
            NavItem(AppScreen.EQUIPMENT, "Equipos", "EQ"),
            NavItem(AppScreen.BILLING, "Cobrar", "CB"),
            NavItem(AppScreen.AGENDA, "Agenda", "AG"),
            NavItem(AppScreen.REPORTS, "Reportes", "RP"),
            NavItem(AppScreen.HELP, "Ayuda", "AY")
        )
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .then(if (elevated) Modifier.shadow(18.dp) else Modifier)
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ChipLogo(compact = true)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("GlzRepair", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Sistema de Taller Tecnico",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                menuItems.forEach { item ->
                    SidebarItem(
                        item = item,
                        selected = item.screen == activeScreen,
                        onClick = { onSelect(item.screen) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "usuario@glzrepair.mx",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "ID: GLZ-2048",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Modo Oscuro", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = isDark, onCheckedChange = { onToggleDark() })
            }

            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar Sesion")
            }
        }
    }
}

@Composable
private fun SidebarItem(item: NavItem, selected: Boolean, onClick: () -> Unit) {
    val background by animateFloatAsState(if (selected) 1f else 0f)
    val containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f * background)
    val textColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.iconLabel, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
        }
        Text(item.label, color = textColor, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun MainContent(
    screen: AppScreen,
    onNewEquipment: () -> Unit,
    onViewSheet: (EquipmentRow) -> Unit,
    onEditEquipment: (EquipmentRow) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (screen) {
            AppScreen.DASHBOARD -> DashboardView()
            AppScreen.EQUIPMENT -> EquipmentView(onNewEquipment, onViewSheet, onEditEquipment)
            AppScreen.BILLING -> BillingView(onViewSheet)
            AppScreen.AGENDA -> AgendaView()
            AppScreen.REPORTS -> ReportsView()
            AppScreen.HELP -> HelpView()
        }
    }
}

@Composable
private fun DashboardView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MockData.stats.take(3).forEach { stat ->
                StatCardView(stat, modifier = Modifier.weight(1f))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MockData.stats.drop(3).forEach { stat ->
                StatCardView(stat, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCardView(stat: StatCard, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.heightIn(min = 130.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = stat.color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stat.title, style = MaterialTheme.typography.bodyMedium)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(stat.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stat.iconLabel, fontSize = 11.sp, color = stat.color)
                }
            }
            Text(
                stat.value.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
                color = stat.color
            )
        }
    }
}

@Composable
private fun EquipmentView(
    onNewEquipment: () -> Unit,
    onViewSheet: (EquipmentRow) -> Unit,
    onEditEquipment: (EquipmentRow) -> Unit
) {
    var searchClient by remember { mutableStateOf("") }
    var searchPhone by remember { mutableStateOf("") }
    var searchBrand by remember { mutableStateOf("") }
    var searchModel by remember { mutableStateOf("") }
    var searchIssue by remember { mutableStateOf("") }
    var pageIndex by remember { mutableStateOf(0) }
    val pageSize = 4

    val filtered = MockData.equipment.filter { row ->
        row.client.contains(searchClient, ignoreCase = true) &&
            row.phone.contains(searchPhone, ignoreCase = true) &&
            row.brand.contains(searchBrand, ignoreCase = true) &&
            row.model.contains(searchModel, ignoreCase = true) &&
            row.issue.contains(searchIssue, ignoreCase = true)
    }
    val pageCount = (filtered.size + pageSize - 1) / pageSize
    val pageItems = filtered.drop(pageIndex * pageSize).take(pageSize)
    val rangeStart = (pageIndex * pageSize + 1).coerceAtMost(filtered.size)
    val rangeEnd = (pageIndex * pageSize + pageItems.size).coerceAtLeast(rangeStart)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Gestion de Equipos", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onNewEquipment) { Text("Nuevo Equipo") }
        }

        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = searchClient,
                        onValueChange = {
                            searchClient = it
                            pageIndex = 0
                        },
                        label = { Text("Cliente") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = searchPhone,
                        onValueChange = {
                            searchPhone = it
                            pageIndex = 0
                        },
                        label = { Text("Telefono") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = searchBrand,
                        onValueChange = {
                            searchBrand = it
                            pageIndex = 0
                        },
                        label = { Text("Marca") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = searchModel,
                        onValueChange = {
                            searchModel = it
                            pageIndex = 0
                        },
                        label = { Text("Modelo") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = searchIssue,
                    onValueChange = {
                        searchIssue = it
                        pageIndex = 0
                    },
                    label = { Text("Falla") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        EquipmentTable(
            rows = pageItems,
            onEdit = onEditEquipment,
            onPrint = onViewSheet
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { pageIndex = (pageIndex - 1).coerceAtLeast(0) },
                    enabled = pageIndex > 0
                ) {
                    Text("Anterior")
                }
                OutlinedButton(
                    onClick = { pageIndex = (pageIndex + 1).coerceAtMost(pageCount - 1) },
                    enabled = pageIndex < pageCount - 1
                ) {
                    Text("Siguiente")
                }
            }
            Text(
                "Mostrando $rangeStart - $rangeEnd de ${filtered.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun EquipmentTable(
    rows: List<EquipmentRow>,
    onEdit: (EquipmentRow) -> Unit,
    onPrint: (EquipmentRow) -> Unit
) {
    Card(shape = RoundedCornerShape(22.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            TableHeader(
                listOf(
                    "Cliente",
                    "Equipo",
                    "Serie / IMEI",
                    "Estado",
                    "Fecha de ingreso",
                    "Acciones"
                )
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            rows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(row.client, width = 160.dp)
                    TableCell("${row.brand} ${row.model}", width = 180.dp)
                    TableCell(row.serial, width = 160.dp)
                    StatusPill(row.status)
                    TableCell(row.intakeDate, width = 140.dp)
                    Row(
                        modifier = Modifier.width(220.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(onClick = { onEdit(row) }) { Text("Editar") }
                        OutlinedButton(onClick = { onPrint(row) }) { Text("Ficha") }
                    }
                }
            }
        }
    }
}

@Composable
private fun BillingView(onViewSheet: (EquipmentRow) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Cobrar", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Equipos listos para facturar.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Card(shape = RoundedCornerShape(22.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                TableHeader(listOf("Cliente", "Equipo", "Estado", "Costo actual", "Accion"))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                MockData.billing.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(row.client, width = 160.dp)
                        TableCell("${row.brand} ${row.model}", width = 180.dp)
                        StatusPill(row.status)
                        TableCell(row.cost, width = 120.dp)
                        OutlinedButton(onClick = { onViewSheet(row) }, modifier = Modifier.width(160.dp)) {
                            Text("Cobrar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AgendaView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Agenda", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { }) { Text("Nueva Cita") }
        }
        Card(shape = RoundedCornerShape(22.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                TableHeader(listOf("Fecha y Hora", "Cliente", "Motivo", "Estado", "Acciones"))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                MockData.appointments.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(row.dateTime, width = 160.dp)
                        TableCell(row.client, width = 160.dp)
                        TableCell(row.reason, width = 220.dp)
                        TableCell(row.status, width = 120.dp)
                        Row(
                            modifier = Modifier.width(160.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(onClick = { }) { Text("Editar") }
                            OutlinedButton(onClick = { }) { Text("Ver") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportsView() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Reportes", style = MaterialTheme.typography.headlineSmall)
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Modulo en construccion", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Aqui se mostraran reportes operativos, financieros y de rendimiento.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun HelpView() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Ayuda", style = MaterialTheme.typography.headlineSmall)
        Card(shape = RoundedCornerShape(22.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "GlzRepair es un sistema para seguimiento de equipos, agenda de citas, cobranza y reportes.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Usa el menu lateral para navegar y gestiona estados y fichas tecnicas desde el modulo de equipos.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun EquipmentModal(
    initialRow: EquipmentRow?,
    onDismiss: () -> Unit
) {
    var client by remember { mutableStateOf(initialRow?.client.orEmpty()) }
    var phone by remember { mutableStateOf(initialRow?.phone.orEmpty()) }
    var email by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Telefono") }
    var brand by remember { mutableStateOf(initialRow?.brand.orEmpty()) }
    var model by remember { mutableStateOf(initialRow?.model.orEmpty()) }
    var serial by remember { mutableStateOf(initialRow?.serial.orEmpty()) }
    var imei by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf(initialRow?.issue.orEmpty()) }
    var diagnosis by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(initialRow?.status ?: RepairStatus.RECEIVED) }

    ModalShell(title = if (initialRow == null) "Registrar Equipo" else "Editar Equipo", onDismiss = onDismiss) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SectionTitle("Datos del Cliente")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = client,
                    onValueChange = { client = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefono") },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            SectionTitle("Datos del Equipo")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipo") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = serial,
                    onValueChange = { serial = it },
                    label = { Text("Numero de serie") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = imei,
                    onValueChange = { imei = it },
                    label = { Text("IMEI") },
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle("Detalles de Reparacion")
            OutlinedTextField(
                value = issue,
                onValueChange = { issue = it },
                label = { Text("Falla reportada") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = diagnosis,
                onValueChange = { diagnosis = it },
                label = { Text("Diagnostico preliminar") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas adicionales") },
                modifier = Modifier.fillMaxWidth()
            )

            SectionTitle("Estado actual")
            StatusFlowRow(items = RepairStatus.values().toList(), selected = status) { value ->
                status = value
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDismiss) { Text("Guardar") }
            }
        }
    }
}

@Composable
private fun PrintModal(row: EquipmentRow?, onDismiss: () -> Unit) {
    ModalShell(title = "Ficha Tecnica", onDismiss = onDismiss, maxWidth = 760.dp) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                "Impresion profesional con doble copia automatica.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            PrintSheet(row, copyLabel = "Copia Cliente")
            HorizontalDivider(thickness = 1.dp)
            PrintSheet(row, copyLabel = "Copia Taller")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Cerrar") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { }) { Text("Imprimir") }
            }
        }
    }
}

@Composable
private fun PrintSheet(row: EquipmentRow?, copyLabel: String) {
    Card(
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("GlzRepair", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Ficha Tecnica", color = Color.Black)
                }
                Text(copyLabel, color = Color.Black)
            }
            HorizontalDivider(color = Color.Black)
            Text("Cliente: ${row?.client ?: "N/A"}", color = Color.Black)
            Text("Equipo: ${row?.brand ?: ""} ${row?.model ?: ""}", color = Color.Black)
            Text("Serie / IMEI: ${row?.serial ?: ""}", color = Color.Black)
            Text("Estado: ${row?.status?.label ?: ""}", color = Color.Black)
            Text("Costo actual: ${row?.cost ?: ""}", color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Firma del cliente:", color = Color.Black)
            HorizontalDivider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            Text(
                "El equipo se entrega en las condiciones descritas. GlzRepair no se responsabiliza por datos perdidos.",
                fontSize = 11.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun TableHeader(columns: List<String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        columns.forEach { title ->
            TableCell(
                text = title,
                width = when (title) {
                    "Cliente" -> 160.dp
                    "Equipo" -> 180.dp
                    "Serie / IMEI" -> 160.dp
                    "Estado" -> 120.dp
                    "Fecha de ingreso" -> 140.dp
                    "Acciones" -> 220.dp
                    "Costo actual" -> 120.dp
                    "Accion" -> 160.dp
                    "Fecha y Hora" -> 160.dp
                    "Motivo" -> 220.dp
                    else -> 160.dp
                },
                isHeader = true
            )
        }
    }
}

@Composable
private fun TableCell(text: String, width: Dp, isHeader: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        fontWeight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun StatusPill(status: RepairStatus) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(status.tone.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(status.label, fontSize = 12.sp, color = status.tone)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun StatusFlowRow(
    items: List<RepairStatus>,
    selected: RepairStatus,
    onSelect: (RepairStatus) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.take(4).forEach { status ->
                StatusSelectChip(status, selected == status) { onSelect(status) }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.drop(4).forEach { status ->
                StatusSelectChip(status, selected == status) { onSelect(status) }
            }
        }
    }
}

@Composable
private fun StatusSelectChip(status: RepairStatus, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) status.tone.copy(alpha = 0.2f) else Color.Transparent
    val borderColor = if (selected) status.tone else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(status.label, fontSize = 12.sp)
    }
}

@Composable
private fun ModalShell(
    title: String,
    onDismiss: () -> Unit,
    maxWidth: Dp = 820.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .noRippleClickable(onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .padding(24.dp)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, style = MaterialTheme.typography.titleLarge)
                    TextButton(onClick = onDismiss) { Text("Cerrar") }
                }
                content()
            }
        }
    }
}

@Composable
private fun ChipLogo(compact: Boolean = false) {
    val size = if (compact) 38.dp else 56.dp
    val chipColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.75f)) {
            drawChipIcon(chipColor)
        }
    }
}

private fun DrawScope.drawChipIcon(color: Color) {
    val stroke = Stroke(width = 2f)
    drawRoundRect(
        color = color,
        size = size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f),
        style = stroke
    )
    val pinLength = size.width * 0.12f
    val step = size.width / 4f
    for (i in 1..3) {
        val offset = step * i
        drawLine(
            color = color,
            start = Offset(offset, 0f),
            end = Offset(offset, -pinLength),
            strokeWidth = 2f
        )
        drawLine(
            color = color,
            start = Offset(offset, size.height),
            end = Offset(offset, size.height + pinLength),
            strokeWidth = 2f
        )
    }
}

@Composable
private fun LoginBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(12000), repeatMode = RepeatMode.Reverse)
    )
    val drift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(18000), repeatMode = RepeatMode.Reverse)
    )
    val particles = remember {
        List(18) { index ->
            Particle(
                x = 0.1f + (index * 0.04f) % 0.8f,
                y = 0.1f + (index * 0.07f) % 0.8f,
                radius = 6f + (index % 4) * 3f,
                speed = 0.2f + (index % 3) * 0.15f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            Brush.linearGradient(
                colors = listOf(Color(0xFF0B3954), Color(0xFF5F3DC4), Color(0xFF0CA678))
            )
        )
        drawLiquidBlob(
            center = Offset(size.width * (0.2f + 0.2f * drift), size.height * 0.3f),
            radius = size.minDimension * (0.35f + 0.05f * pulse),
            color = Color(0xFF66D9E8).copy(alpha = 0.25f)
        )
        drawLiquidBlob(
            center = Offset(size.width * (0.8f - 0.25f * drift), size.height * 0.2f),
            radius = size.minDimension * (0.25f + 0.08f * pulse),
            color = Color(0xFFF783AC).copy(alpha = 0.2f)
        )
        drawLiquidBlob(
            center = Offset(size.width * 0.6f, size.height * (0.7f + 0.1f * drift)),
            radius = size.minDimension * 0.4f,
            color = Color(0xFF82C91E).copy(alpha = 0.22f)
        )

        particles.forEach { particle ->
            val y = (particle.y + pulse * particle.speed) % 1f
            drawCircle(
                color = Color.White.copy(alpha = 0.22f),
                radius = particle.radius,
                center = Offset(size.width * particle.x, size.height * y)
            )
        }
    }
}

private fun DrawScope.drawLiquidBlob(center: Offset, radius: Float, color: Color) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color, color.copy(alpha = 0.02f)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float
)

private data class NavItem(val screen: AppScreen, val label: String, val iconLabel: String)

private enum class AppScreen {
    DASHBOARD,
    EQUIPMENT,
    BILLING,
    AGENDA,
    REPORTS,
    HELP
}

private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    return this.pointerInput(onClick) {
        detectTapGestures { onClick() }
    }
}




