package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AppDatabase
import com.example.data.local.FarmerProfileManager
import com.example.data.local.MockDataProvider
import com.example.data.remote.supabase.SupabaseRepository
import com.example.ui.components.MkulimaBottomNav
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
  onBackClick: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToServices: () -> Unit,
  onNavigateToMarketplace: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  val db = remember { AppDatabase.getDatabase(context) }
  val savedDiagnoses by db.diagnosisDao().getAllDiagnoses().collectAsState(initial = emptyList())
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  // Dynamic profile states backed by FarmerProfileManager
  val currentProfile by FarmerProfileManager.currentProfile.collectAsState()
  var farmerName by remember(currentProfile) { mutableStateOf(currentProfile.name) }
  var farmerRegion by remember(currentProfile) { mutableStateOf("${currentProfile.district}, ${currentProfile.region}") }
  var farmSize by remember(currentProfile) { mutableStateOf(currentProfile.farmSizeAcres.toString()) }
  var farmerPhone by remember(currentProfile) { mutableStateOf(currentProfile.phone) }

  // Dialog State
  var showEditProfileDialog by remember { mutableStateOf(false) }
  var editName by remember { mutableStateOf(farmerName) }
  var editRegion by remember { mutableStateOf(farmerRegion) }
  var editFarmSize by remember { mutableStateOf(farmSize) }
  var editPhone by remember { mutableStateOf(farmerPhone) }

  LaunchedEffect(Unit) {
    try {
      SupabaseRepository().syncCloudDiagnosesToLocal(db.diagnosisDao(), currentProfile.phone)
    } catch (_: Exception) {}
  }

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("Farmer Profile", "Wasifu wa Mkulima"),
        onBackClick = onBackClick
      )
    },
    bottomBar = {
      MkulimaBottomNav(
        currentRoute = "profile",
        onNavigate = { route ->
          when (route) {
            "home" -> onNavigateToHome()
            "services" -> onNavigateToServices()
            "marketplace" -> onNavigateToMarketplace()
          }
        }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color.White
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .testTag("profile_screen"),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Profile Info Card with Edit button
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(BrandSurfaceGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Farmer Avatar",
                tint = ForestGreenPrimary,
                modifier = Modifier.size(36.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = farmerName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
              Text(
                text = farmerRegion,
                fontSize = 13.sp,
                color = TextSecondary
              )
              Text(
                text = "${lang.t("Farm", "Shamba")}: $farmSize ${lang.t("Acres", "Ekari")} • Maize & Tomato",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = ForestGreenDark,
                modifier = Modifier.padding(top = 2.dp)
              )
              Text(
                text = farmerPhone,
                fontSize = 12.sp,
                color = TextMuted
              )
            }

            // Edit Profile Button (Popup trigger)
            IconButton(
              onClick = {
                editName = farmerName
                editRegion = farmerRegion
                editFarmSize = farmSize
                editPhone = farmerPhone
                showEditProfileDialog = true
              },
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F2))
                .testTag("edit_profile_icon_button")
            ) {
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Saved Diagnoses from Room Database
      item {
        Text(
          text = "${lang.t("Saved Crop Diagnoses", "Rekodi za Afya ya Mazao")} (${savedDiagnoses.size})",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark,
          modifier = Modifier.padding(top = 6.dp)
        )
      }

      if (savedDiagnoses.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBF9))
          ) {
            Text(
              text = lang.t(
                "No saved crop scan records yet. Scans you save will appear here automatically.",
                "Hakuna rekodi za uchunguzi wa mimea zilizohifadhiwa bado. Vipimo utakavyohifadhi vitaonekana hapa."
              ),
              fontSize = 13.sp,
              color = TextMuted,
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      } else {
        items(savedDiagnoses) { item ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, CardBorder),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = item.diseaseName,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
                Text(
                  text = "${item.cropName} • ${lang.t("Confidence", "Uhakika")}: ${item.confidence}%",
                  fontSize = 12.sp,
                  color = ForestGreenDark
                )
              }
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }

      // Account Options
      item {
        Text(
          text = lang.t("Settings & Support", "Mipangilio & Msaada"),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark,
          modifier = Modifier.padding(top = 10.dp)
        )
      }

      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column {
            ProfileMenuRow(
              icon = Icons.Default.Language,
              title = lang.t("Language", "Lugha"),
              subtitle = if (lang.isSwahili) "Kiswahili (Imewashwa) • Gusa kubadili kwenda English" else "English (Active) • Tap to switch to Kiswahili",
              onClick = {
                lang.toggleLanguage()
                coroutineScope.launch {
                  val msg = if (lang.isSwahili) "Lugha imebadilishwa kuwa Kiswahili kikamilifu!" else "Language successfully switched to English!"
                  snackbarHostState.showSnackbar(msg)
                }
              }
            )
            HorizontalDivider(color = Color(0xFFF0F4F1))
            ProfileMenuRow(
              icon = Icons.Default.Settings,
              title = lang.t("Farm Preferences", "Mapendeleo ya Shamba"),
              subtitle = lang.t("Soil type, irrigation, seed varieties", "Aina ya udongo, umwagiliaji, mbegu"),
              onClick = {
                coroutineScope.launch {
                  snackbarHostState.showSnackbar(lang.t("Farm preferences: Central Zone Dodoma.", "Mapendeleo ya shamba: Kanda ya Kati Dodoma."))
                }
              }
            )
            HorizontalDivider(color = Color(0xFFF0F4F1))
            ProfileMenuRow(
              icon = Icons.Default.HelpOutline,
              title = lang.t("Farming Support & Hotline", "Msaada wa Kilimo & Nambari ya Dharura"),
              subtitle = lang.t("Call agricultural extension officer", "Piga simu kwa afisa ugani"),
              onClick = {
                coroutineScope.launch {
                  snackbarHostState.showSnackbar("Tanzania Agriculture Hotline: 0800 110 099")
                }
              }
            )
            HorizontalDivider(color = Color(0xFFF0F4F1))
            ProfileMenuRow(
              icon = Icons.Default.ExitToApp,
              title = lang.t("Log Out", "Ondoka"),
              subtitle = lang.t("Switch account or sign out", "Badili akaunti au toka"),
              textColor = Color(0xFFD32F2F),
              onClick = onLogout
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Profile Edit Popup Dialog
  if (showEditProfileDialog) {
    AlertDialog(
      onDismissRequest = { showEditProfileDialog = false },
      title = {
        Text(
          text = lang.t("Edit Farmer Profile", "Hariri Wasifu wa Mkulima"),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text(lang.t("Full Name", "Jina Kamili")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = editRegion,
            onValueChange = { editRegion = it },
            label = { Text(lang.t("Region / Location", "Mkoa / Eneo")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = editFarmSize,
            onValueChange = { editFarmSize = it },
            label = { Text(lang.t("Farm Size (Acres)", "Ukubwa wa Shamba (Ekari)")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = editPhone,
            onValueChange = { editPhone = it },
            label = { Text(lang.t("Phone Number", "Namba ya Simu")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (editName.isNotBlank()) {
              farmerName = editName
              farmerRegion = editRegion
              farmSize = editFarmSize
              farmerPhone = editPhone

              val regionParts = editRegion.split(",")
              val reg = if (regionParts.size > 1) regionParts[1].trim() else editRegion.trim()
              val dist = regionParts[0].trim()

              FarmerProfileManager.updateProfile(
                currentProfile.copy(
                  name = editName.trim(),
                  phone = editPhone.trim(),
                  region = reg,
                  district = dist,
                  farmSizeAcres = editFarmSize.toDoubleOrNull() ?: currentProfile.farmSizeAcres
                ),
                context
              )

              showEditProfileDialog = false
              coroutineScope.launch {
                snackbarHostState.showSnackbar(
                  lang.t("Farmer profile updated successfully!", "Wasifu wa mkulima umehifadhiwa kikamilifu!")
                )
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Text(lang.t("Save Changes", "Hifadhi Mabadiliko"))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showEditProfileDialog = false }) {
          Text(lang.t("Cancel", "Ghairi"))
        }
      }
    )
  }
}

@Composable
fun ProfileMenuRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  textColor: Color = TextPrimaryDark
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = if (textColor == TextPrimaryDark) ForestGreenPrimary else textColor,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
      Text(text = subtitle, fontSize = 12.sp, color = TextMuted)
    }
    Icon(
      imageVector = Icons.Default.ChevronRight,
      contentDescription = null,
      tint = TextMuted,
      modifier = Modifier.size(18.dp)
    )
  }
}
