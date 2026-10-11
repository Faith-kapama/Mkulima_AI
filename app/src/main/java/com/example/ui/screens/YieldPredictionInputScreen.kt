package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MaizeYieldInput
import com.example.data.model.TanzaniaLocations
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CrimsonButtonMaroon
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YieldPredictionInputScreen(
  onBackClick: () -> Unit,
  onPredictionReady: (MaizeYieldInput) -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val snackbarHostState = remember { SnackbarHostState() }

  // Step 1: Farm and management information
  // Step 2: Fertilizer, seed and planting details
  var currentStep by remember { mutableIntStateOf(1) }

  // Form State (Initialized to values matching Image 1 spec)
  var region by remember { mutableStateOf("Dodoma") }
  var district by remember { mutableStateOf("Chamwino") }
  var season by remember { mutableStateOf("Long rains (Masika)") }
  var farmAreaHaText by remember { mutableStateOf("2") }
  var seedType by remember { mutableStateOf("Improved") }
  var irrigationUsed by remember { mutableStateOf(false) }
  var fertilizerUsed by remember { mutableStateOf(true) }

  // Part 2 fields
  var fertilizerRateKgHaText by remember { mutableStateOf("100") }
  var improvedSeedUsed by remember { mutableStateOf(true) }
  var traditionalSeedUsed by remember { mutableStateOf(false) }
  var recycledSeedUsed by remember { mutableStateOf(false) }
  var seedQuantityKgHaText by remember { mutableStateOf("30") }
  var plantingMonth by remember { mutableStateOf("December") }

  // Dropdown menus
  var regionExpanded by remember { mutableStateOf(false) }
  var districtExpanded by remember { mutableStateOf(false) }
  var seasonExpanded by remember { mutableStateOf(false) }
  var seedTypeExpanded by remember { mutableStateOf(false) }
  var monthExpanded by remember { mutableStateOf(false) }

  val regionOptions = TanzaniaLocations.allRegions

  val seasonOptions = listOf(
    "Long rains (Masika)",
    "Short rains (Vuli)"
  )

  val seedTypeOptions = listOf("Improved", "Local", "Hybrid")

  val monthOptions = listOf(
    "December", "January", "February", "March", "April",
    "May", "June", "July", "August", "September", "October", "November"
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = lang.t("Maize Yield Prediction", "Utabiri wa Mavuno ya Mahindi"),
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "🌽", fontSize = 18.sp)
          }
        },
        navigationIcon = {
          IconButton(
            onClick = {
              if (currentStep == 2) {
                currentStep = 1
              } else {
                onBackClick()
              }
            }
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = ForestGreenDark)
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color.White
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp)
        .testTag("yield_input_screen")
    ) {
      if (currentStep == 1) {
        // =========================================================================
        // PART 1: FARM AND MANAGEMENT INFORMATION (Image 1 Screen 2)
        // =========================================================================
        Text(
          text = lang.t("Farm Information", "Taarifa za Shamba"),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 1. Region Dropdown
        Text(
          text = lang.t("Region *", "Mkoa *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = region,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { regionExpanded = true }
              .testTag("region_dropdown_field"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = CardBorder,
              focusedBorderColor = ForestGreenPrimary
            )
          )
          DropdownMenu(
            expanded = regionExpanded,
            onDismissRequest = { regionExpanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
          ) {
            regionOptions.forEach { opt ->
              DropdownMenuItem(
                text = { Text(opt) },
                onClick = {
                  region = opt
                  regionExpanded = false
                  val districts = TanzaniaLocations.getDistricts(opt)
                  district = districts.firstOrNull() ?: "Mjini"
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // District Dropdown
        Text(
          text = lang.t("District / Wilaya *", "Wilaya *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = district,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { districtExpanded = true }
              .testTag("district_dropdown_field"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = CardBorder,
              focusedBorderColor = ForestGreenPrimary
            )
          )
          DropdownMenu(
            expanded = districtExpanded,
            onDismissRequest = { districtExpanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
          ) {
            TanzaniaLocations.getDistricts(region).forEach { distOpt ->
              DropdownMenuItem(
                text = { Text(distOpt) },
                onClick = {
                  district = distOpt
                  districtExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Season Dropdown
        Text(
          text = lang.t("Season *", "Msimu *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = season,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { seasonExpanded = true }
              .testTag("season_dropdown_field"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = CardBorder,
              focusedBorderColor = ForestGreenPrimary
            )
          )
          DropdownMenu(
            expanded = seasonExpanded,
            onDismissRequest = { seasonExpanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
          ) {
            seasonOptions.forEach { opt ->
              DropdownMenuItem(
                text = { Text(opt) },
                onClick = {
                  season = opt
                  seasonExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Farm area (hectares) Number Field
        Text(
          text = lang.t("Farm area (hectares) *", "Ukubwa wa shamba (hekta) *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = farmAreaHaText,
          onValueChange = { farmAreaHaText = it },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("farm_area_field"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = CardBorder,
            focusedBorderColor = ForestGreenPrimary
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Seed Type Dropdown
        Text(
          text = lang.t("Seed type *", "Aina ya mbegu *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = seedType,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { seedTypeExpanded = true }
              .testTag("seed_type_dropdown_field"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = CardBorder,
              focusedBorderColor = ForestGreenPrimary
            )
          )
          DropdownMenu(
            expanded = seedTypeExpanded,
            onDismissRequest = { seedTypeExpanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
          ) {
            seedTypeOptions.forEach { opt ->
              DropdownMenuItem(
                text = { Text(opt) },
                onClick = {
                  seedType = opt
                  seedTypeExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Irrigation used? Radio Buttons
        Text(
          text = lang.t("Irrigation used? *", "Je, umemwagilia? *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        YesNoRadioGroup(
          selectedValue = irrigationUsed,
          onSelectedChange = { irrigationUsed = it },
          testTagPrefix = "irrigation"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Fertilizer used? Radio Buttons
        Text(
          text = lang.t("Fertilizer used? *", "Je, umetumia mbolea? *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        YesNoRadioGroup(
          selectedValue = fertilizerUsed,
          onSelectedChange = { fertilizerUsed = it },
          testTagPrefix = "fertilizer"
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Next Button (Image 1 Screen 2: Maroon/Crimson Next button)
        Button(
          onClick = { currentStep = 2 },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("next_step_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonButtonMaroon)
        ) {
          Text(
            text = lang.t("Next →", "Mbele →"),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }

      } else {
        // =========================================================================
        // PART 2: FERTILIZER AND SEED DETAILS (Image 1 Screen 3)
        // =========================================================================
        Text(
          text = lang.t("Fertilizer and Seed Details", "Maelezo ya Mbolea na Mbegu"),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 7. Fertilizer rate (kg/ha) *
        Text(
          text = lang.t("Fertilizer rate (kg/ha) *", "Kiasi cha mbolea (kg/hekta) *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = fertilizerRateKgHaText,
          onValueChange = { fertilizerRateKgHaText = it },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("fertilizer_rate_field"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = CardBorder,
            focusedBorderColor = ForestGreenPrimary
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 12. Improved seed used?
        Text(
          text = lang.t("Improved seed used? *", "Je, umetumia mbegu bora? *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        YesNoRadioGroup(
          selectedValue = improvedSeedUsed,
          onSelectedChange = { improvedSeedUsed = it },
          testTagPrefix = "improved_seed"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 13. Traditional seed used?
        Text(
          text = lang.t("Traditional seed used? *", "Je, umetumia mbegu ya asili? *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        YesNoRadioGroup(
          selectedValue = traditionalSeedUsed,
          onSelectedChange = { traditionalSeedUsed = it },
          testTagPrefix = "traditional_seed"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 14. Recycled seed used?
        Text(
          text = lang.t("Recycled seed used? *", "Je, umetumia mbegu ya kuhifadhiwa (recycled)? *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        YesNoRadioGroup(
          selectedValue = recycledSeedUsed,
          onSelectedChange = { recycledSeedUsed = it },
          testTagPrefix = "recycled_seed"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 15. Seed quantity (kg/ha) *
        Text(
          text = lang.t("Seed quantity (kg/ha) *", "Kiasi cha mbegu (kg/hekta) *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = seedQuantityKgHaText,
          onValueChange = { seedQuantityKgHaText = it },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("seed_quantity_field"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = CardBorder,
            focusedBorderColor = ForestGreenPrimary
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 11. Planting month Dropdown
        Text(
          text = lang.t("Planting month *", "Mwezi wa kupanda *"),
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = plantingMonth,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { monthExpanded = true }
              .testTag("planting_month_dropdown_field"),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = CardBorder,
              focusedBorderColor = ForestGreenPrimary
            )
          )
          DropdownMenu(
            expanded = monthExpanded,
            onDismissRequest = { monthExpanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
          ) {
            monthOptions.forEach { opt ->
              DropdownMenuItem(
                text = { Text(opt) },
                onClick = {
                  plantingMonth = opt
                  monthExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons Row: [<- Back] and [Predict Yield]
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          OutlinedButton(
            onClick = { currentStep = 1 },
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
              .testTag("back_to_part1_button"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, CardBorder)
          ) {
            Text(
              text = lang.t("← Back", "← Rudi"),
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextPrimaryDark
            )
          }

          Button(
            onClick = {
              val area = farmAreaHaText.toDoubleOrNull() ?: 2.0
              val fertRate = fertilizerRateKgHaText.toDoubleOrNull() ?: 100.0
              val seedQty = seedQuantityKgHaText.toDoubleOrNull() ?: 30.0

              val input = MaizeYieldInput(
                region = region,
                season = season,
                farmAreaHa = area,
                seedType = seedType,
                irrigationUsed = irrigationUsed,
                fertilizerUsed = fertilizerUsed,
                fertilizerRateKgHa = fertRate,
                improvedSeedUsed = improvedSeedUsed,
                traditionalSeedUsed = traditionalSeedUsed,
                recycledSeedUsed = recycledSeedUsed,
                seedQuantityKgHa = seedQty,
                plantingMonth = plantingMonth
              )
              onPredictionReady(input)
            },
            modifier = Modifier
              .weight(1.3f)
              .height(52.dp)
              .testTag("predict_yield_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonButtonMaroon)
          ) {
            Text(
              text = lang.t("Predict Yield", "Kadiria Mavuno"),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

/**
 * Reusable Yes/No Radio Group matching Image 1 Screenshots
 */
@Composable
fun YesNoRadioGroup(
  selectedValue: Boolean,
  onSelectedChange: (Boolean) -> Unit,
  testTagPrefix: String
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(24.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // YES option
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .selectable(
          selected = selectedValue,
          onClick = { onSelectedChange(true) }
        )
        .testTag("${testTagPrefix}_yes")
    ) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .border(
            width = 2.dp,
            color = if (selectedValue) ForestGreenPrimary else Color.Gray,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (selectedValue) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .background(ForestGreenPrimary, CircleShape)
          )
        }
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Yes",
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimaryDark
      )
    }

    // NO option
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .selectable(
          selected = !selectedValue,
          onClick = { onSelectedChange(false) }
        )
        .testTag("${testTagPrefix}_no")
    ) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .border(
            width = 2.dp,
            color = if (!selectedValue) ForestGreenPrimary else Color.Gray,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (!selectedValue) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .background(ForestGreenPrimary, CircleShape)
          )
        }
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "No",
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimaryDark
      )
    }
  }
}
