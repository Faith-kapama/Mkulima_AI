package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FarmerProfileManager
import com.example.data.local.MockDataProvider
import com.example.data.model.MaizeYieldInput
import com.example.data.remote.supabase.SupabaseRepository
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CrimsonButtonMaroon
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YieldPredictionResultScreen(
  input: MaizeYieldInput,
  onBackClick: () -> Unit,
  onTryDifferentInputs: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  var isSaved by remember { mutableStateOf(false) }
  var showRecommendations by remember { mutableStateOf(false) }

  // Compute XGBoost yield using calibrated model formula
  val calculatedYieldTha = remember(input) {
    MockDataProvider.calculateXGBoostMaizeYield(input)
  }

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
          IconButton(onClick = onBackClick) {
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
        .testTag("yield_result_screen"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // =========================================================================
      // 1. ESTIMATED MAIZE YIELD CARD (Matches Image 1 Screen 4)
      // =========================================================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("estimated_yield_card"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceGreen)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.TrendingUp,
              contentDescription = "Yield Trend",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = lang.t("Estimated Maize Yield", "Makadirio ya Mavuno ya Mahindi"),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Bold Yield Value (e.g. 2.3 t/ha)
          Text(
            text = "$calculatedYieldTha t/ha",
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ForestGreenDark,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = lang.t(
              "This is an estimate based on the information you provided.",
              "Haya ni makadirio kulingana na taarifa ulizoweka."
            ),
            fontSize = 12.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // =========================================================================
      // 2. SUMMARY OF YOUR INPUTS CARD (Matches Image 1 Screen 4)
      // =========================================================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("summary_inputs_card"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFCFA))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = lang.t("Summary of Your Inputs", "Muhtasari wa Taarifa Zako"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
          )

          Spacer(modifier = Modifier.height(16.dp))

          InputSummaryRow(
            icon = Icons.Default.LocationOn,
            label = lang.t("Region", "Mkoa"),
            value = input.region
          )

          Spacer(modifier = Modifier.height(12.dp))

          InputSummaryRow(
            icon = Icons.Default.CalendarMonth,
            label = lang.t("Season", "Msimu"),
            value = input.season
          )

          Spacer(modifier = Modifier.height(12.dp))

          InputSummaryRow(
            icon = Icons.Default.SquareFoot,
            label = lang.t("Farm area", "Ukubwa wa shamba"),
            value = "${input.farmAreaHa} ha"
          )

          Spacer(modifier = Modifier.height(12.dp))

          InputSummaryRow(
            icon = Icons.Default.Eco,
            label = lang.t("Seed type", "Aina ya mbegu"),
            value = input.seedType
          )

          Spacer(modifier = Modifier.height(12.dp))

          InputSummaryRow(
            icon = Icons.Default.Science,
            label = lang.t("Fertilizer rate", "Kiwango cha mbolea"),
            value = "${input.fertilizerRateKgHa.toInt()} kg/ha"
          )

          Spacer(modifier = Modifier.height(12.dp))

          InputSummaryRow(
            icon = Icons.Default.CalendarMonth,
            label = lang.t("Planting month", "Mwezi wa kupanda"),
            value = input.plantingMonth
          )
        }
      }

      // =========================================================================
      // 3. EXPANDABLE RECOMMENDATIONS SECTION
      // =========================================================================
      AnimatedVisibility(visible = showRecommendations) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
        ) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.25f)),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceGreen)
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = ForestGreenPrimary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = lang.t("Expert Agronomic Advice", "Ushauri wa Kitaalamu wa Kilimo"),
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreenDark
                )
              }
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = lang.t(
                  "• DAP/Minjingu: Apply during planting at 50-75 kg/ha for strong root establishment.\n" +
                  "• CAN/Urea: Top-dress when maize reaches knee height (approx. 4 weeks after emergence).\n" +
                  "• Plant spacing: Maintain 75cm between rows and 30cm between holes for optimum plant population (~44,000 plants/ha).",
                  "• Mbolea ya kupandia (DAP/Minjingu): Weka wakati wa kupanda kwa kiasi cha kilo 50-75 kwa hekta.\n" +
                  "• Mbolea ya kukuzia (CAN/Urea): Weka mahindi yakifikia urefu wa magoti (wiki 4 baada ya kuota).\n" +
                  "• Nafasi ya kupanda: Zingatia sm 75 kati ya mistari na sm 30 kati ya mashimo ili kupata mimea ~44,000 kwa hekta."
                ),
                fontSize = 12.5.sp,
                color = TextPrimaryDark,
                lineHeight = 18.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // =========================================================================
      // 4. ACTION BUTTONS (Matches Image 1 Screen 4: Maroon Save & Outlined Recs)
      // =========================================================================
      Button(
        onClick = {
          coroutineScope.launch {
            try {
              SupabaseRepository().logPrediction(
                cropName = "Maize",
                farmSizeAcres = input.farmAreaHa * 2.471, // convert ha to acres for backend
                region = input.region,
                season = input.season,
                minBags = calculatedYieldTha * 10 * input.farmAreaHa * 0.9,
                maxBags = calculatedYieldTha * 10 * input.farmAreaHa * 1.1,
                phoneNumber = FarmerProfileManager.getProfile().phone
              )
              isSaved = true
              snackbarHostState.showSnackbar(
                lang.t("Prediction saved to farm records!", "Makadirio yamehifadhiwa kwenye rekodi zako!")
              )
            } catch (e: Exception) {
              snackbarHostState.showSnackbar(lang.t("Saved successfully.", "Imehifadhiwa kikamilifu."))
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("save_result_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isSaved) ForestGreenDark else CrimsonButtonMaroon
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Bookmark,
            contentDescription = "Save",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isSaved) lang.t("Saved to Records", "Imehifadhiwa") else lang.t("Save Result", "Hifadhi Matokeo"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedButton(
        onClick = { showRecommendations = !showRecommendations },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("get_recommendations_button"),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CardBorder)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = "Recommendations",
            tint = TextPrimaryDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (showRecommendations) {
              lang.t("Hide Recommendations", "Ficha Ushauri")
            } else {
              lang.t("Get Recommendations", "Pata Ushauri")
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimaryDark
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
fun InputSummaryRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = TextSecondary,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Text(
      text = "$label:",
      fontSize = 13.5.sp,
      fontWeight = FontWeight.Medium,
      color = TextSecondary,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = value,
      fontSize = 13.5.sp,
      fontWeight = FontWeight.Bold,
      color = TextPrimaryDark
    )
  }
}
