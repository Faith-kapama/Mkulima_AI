package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.local.FarmerProfileManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.MkulimaBottomNav
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WeatherSunYellow
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

data class DayForecast(
  val day: String,
  val temp: String,
  val condition: String,
  val rainChance: String,
  val icon: ImageVector
)

@Composable
fun HomeScreen(
  onNavigateToCropHealth: () -> Unit,
  onNavigateToYieldPrediction: () -> Unit,
  onNavigateToMarketPrices: () -> Unit,
  onNavigateToMarketplace: () -> Unit,
  onNavigateToServices: () -> Unit,
  onNavigateToProfile: () -> Unit,
  onNavigateToAiCompanion: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val farmerProfile by FarmerProfileManager.currentProfile.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  var showFullForecast by remember { mutableStateOf(false) }

  val forecastDays = if (lang.isSwahili) {
    listOf(
      DayForecast("Jumatano", "29°C", "Jua na Mawingu", "15%", Icons.Default.WbSunny),
      DayForecast("Alhamisi", "27°C", "Mvua za Rasharasha", "65%", Icons.Default.Grain),
      DayForecast("Ijumaa", "26°C", "Mvua ya Wastani", "70%", Icons.Default.Cloud),
      DayForecast("Jumamosi", "28°C", "Mawingu Kiasi", "20%", Icons.Default.WbCloudy)
    )
  } else {
    listOf(
      DayForecast("Wednesday", "29°C", "Partly Cloudy", "15%", Icons.Default.WbSunny),
      DayForecast("Thursday", "27°C", "Light Showers", "65%", Icons.Default.Grain),
      DayForecast("Friday", "26°C", "Moderate Rain", "70%", Icons.Default.Cloud),
      DayForecast("Saturday", "28°C", "Sunny Spells", "20%", Icons.Default.WbCloudy)
    )
  }

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = "MkulimaAI",
        isHomeHeader = true,
        showNotification = true,
        onNotificationClick = {
          coroutineScope.launch {
            snackbarHostState.showSnackbar(
              lang.t("No new alerts. Your crops in Dodoma are healthy!", "Hakuna taarifa mpya. Mazao yako Dodoma yana afya nzuri!")
            )
          }
        }
      )
    },
    bottomBar = {
      MkulimaBottomNav(
        currentRoute = "home",
        onNavigate = { route ->
          when (route) {
            "services" -> onNavigateToServices()
            "marketplace" -> onNavigateToMarketplace()
            "profile" -> onNavigateToProfile()
          }
        }
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
        .padding(horizontal = 20.dp, vertical = 14.dp)
        .testTag("home_screen")
    ) {
      // Greeting & Farmer Location Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = lang.t("Hello, ${farmerProfile.name}!", "Habari, ${farmerProfile.name}!"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = ForestGreenPrimary,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${farmerProfile.district}, ${farmerProfile.region} • ${farmerProfile.farmSizeAcres} Ekari",
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              color = ForestGreenDark
            )
          }

          Text(
            text = lang.t(
              "Primary Crops: ${farmerProfile.primaryCrops.joinToString(", ")}",
              "Mazao Yako: ${farmerProfile.primaryCrops.joinToString(", ")}"
            ),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
          )
        }

        Surface(
          shape = CircleShape,
          color = BrandSurfaceGreen,
          border = BorderStroke(1.5.dp, ForestGreenPrimary.copy(alpha = 0.35f)),
          modifier = Modifier
            .size(46.dp)
            .clickable { onNavigateToProfile() }
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = farmerProfile.name.take(1).uppercase().ifEmpty { "M" },
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = ForestGreenDark
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Promotional Banner Card (Matches Image 1 Screen 1: Build a Better Harvest)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToYieldPrediction() }
          .testTag("home_promo_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenDark)
      ) {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
          Image(
            painter = painterResource(id = R.drawable.img_healthy_crops),
            contentDescription = "Lush green maize farm",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          // Gradient scrim
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                androidx.compose.ui.graphics.Brush.horizontalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.78f),
                    Color.Black.copy(alpha = 0.45f),
                    Color.Transparent
                  )
                )
              )
          )

          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(18.dp),
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = lang.t("Build a\nBetter Harvest", "Kuza Mavuno\nYenye Tija"),
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = lang.t(
                "Get yield estimates and expert\nadvice for your farm.",
                "Pata makadirio ya mavuno na\nushauri wa kitaalamu."
              ),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = Color.White.copy(alpha = 0.92f),
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 4 Quick Action Cards Grid (2x2) - Matches Image 1 Screen 1
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // 1. Maize Yield Prediction (Highlighted active style with light green tint)
        Card(
          onClick = onNavigateToYieldPrediction,
          modifier = Modifier
            .weight(1f)
            .height(115.dp)
            .testTag("service_card_yield_prediction"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.5.dp, ForestGreenPrimary.copy(alpha = 0.45f)),
          colors = CardDefaults.cardColors(containerColor = BrandSurfaceGreen)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = "Maize Yield Prediction",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = lang.t("Maize Yield\nPrediction", "Utabiri wa\nMavuno ya Mahindi"),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 16.sp
            )
          }
        }

        // 2. Disease Detection
        Card(
          onClick = onNavigateToCropHealth,
          modifier = Modifier
            .weight(1f)
            .height(115.dp)
            .testTag("service_card_crop_health"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Disease Detection",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = lang.t("Disease\nDetection", "Uchunguzi wa\nMagonjwa"),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // 3. Market Prices
        Card(
          onClick = onNavigateToMarketPrices,
          modifier = Modifier
            .weight(1f)
            .height(115.dp)
            .testTag("service_card_market_prices"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.TrendingUp,
              contentDescription = "Market Prices",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = lang.t("Market\nPrices", "Bei za\nMasoko"),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 16.sp
            )
          }
        }

        // 4. Farming Advice
        Card(
          onClick = onNavigateToAiCompanion,
          modifier = Modifier
            .weight(1f)
            .height(115.dp)
            .testTag("service_card_farming_advice"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.Grain,
              contentDescription = "Farming Advice",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = lang.t("Farming\nAdvice", "Ushauri wa\nKilimo"),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Weather Today Section with Simulated Forecast Plate
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { showFullForecast = !showFullForecast }
          .testTag("weather_today_card"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = lang.t("Weather Today", "Hali ya Hewa Leo"),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Dodoma (Central Zone)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary
              )
              Text(
                text = "28°C • " + lang.t("Sunny & Humid 62%", "Jua & Unyevu 62%"),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0288D1),
                modifier = Modifier.padding(top = 1.dp)
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.WbSunny,
                  contentDescription = "Sunny weather",
                  tint = WeatherSunYellow,
                  modifier = Modifier.size(38.dp)
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = if (showFullForecast) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Toggle Forecast",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
              )
            }
          }

          // Forecast Simulation Plate
          Spacer(modifier = Modifier.height(12.dp))
          HorizontalDivider(color = Color(0xFFF1F5F2))
          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = lang.t("4-Day Farming Forecast Simulation", "Makadirio ya Siku 4 za Hali ya Hewa"),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            forecastDays.forEach { dayItem ->
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Text(
                  text = dayItem.day.take(3),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                  imageVector = dayItem.icon,
                  contentDescription = dayItem.condition,
                  tint = if (dayItem.condition.contains("Rain", true) || dayItem.condition.contains("Mvua", true))
                    Color(0xFF0288D1) else WeatherSunYellow,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = dayItem.temp,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
                Text(
                  text = dayItem.rainChance,
                  fontSize = 10.sp,
                  color = Color(0xFF0288D1)
                )
              }
            }
          }

          AnimatedVisibility(visible = showFullForecast) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF7FAF7))
                .padding(10.dp)
            ) {
              Text(
                text = lang.t(
                  "🌱 Farming Advisory: Thursday and Friday bring 65-70% rain probability. Ideal for post-weeding fertilizer application (CAN/Urea) and soil moisture preservation.",
                  "🌱 Ushauri wa Kilimo: Alhamisi na Ijumaa kuna uwezekano mkubwa wa mvua (65-70%). Wakati mzuri wa kuweka mbolea ya kukuzia (CAN/Urea) na kuhifadhi unyevu ardhini."
                ),
                fontSize = 11.5.sp,
                color = ForestGreenDark,
                lineHeight = 16.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun ServiceCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, CardBorder),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(BrandSurfaceGreen),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = ForestGreenPrimary,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = subtitle,
        fontSize = 11.5.sp,
        color = TextSecondary,
        lineHeight = 15.sp
      )
    }
  }
}
