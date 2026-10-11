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
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MkulimaBottomNav
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager

@Composable
fun ServicesScreen(
  onNavigateToCropHealth: () -> Unit,
  onNavigateToYieldPrediction: () -> Unit,
  onNavigateToMarketPrices: () -> Unit,
  onNavigateToMarketplace: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToProfile: () -> Unit,
  onNavigateToAiCompanion: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("AI Farming Services", "Huduma za Kilimo cha AI")
      )
    },
    bottomBar = {
      MkulimaBottomNav(
        currentRoute = "services",
        onNavigate = { route ->
          when (route) {
            "home" -> onNavigateToHome()
            "marketplace" -> onNavigateToMarketplace()
            "profile" -> onNavigateToProfile()
          }
        }
      )
    },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color.White
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 18.dp)
        .testTag("services_screen")
    ) {
      Text(
        text = lang.t("Smart Farming Solutions", "Suluhisho za Kilimo Bora"),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark
      )
      Text(
        text = lang.t(
          "Select an AI tool to empower your harvest decisions.",
          "Chagua chombo cha AI ili kufanya maamuzi bora ya mavuno."
        ),
        fontSize = 13.sp,
        color = TextSecondary,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // AI Companion Feature in Services
      ServiceListItem(
        title = lang.t("AI Agribusiness Companion", "Msaidizi wa Biashara Mkulima AI"),
        description = lang.t(
          "Chat with AI expert on crop business strategies, profit maximization, and regional price trends.",
          "Ongea na AI mtaalamu kuhusu mikakati ya biashara, kuongeza faida na mwenendo wa masoko."
        ),
        icon = Icons.Default.AutoAwesome,
        onClick = onNavigateToAiCompanion
      )

      Spacer(modifier = Modifier.height(14.dp))

      ServiceListItem(
        title = lang.t("Crop Health & Disease Scanner", "Uchunguzi wa Afya ya Mazao"),
        description = lang.t(
          "Detect tomato early blight, maize leaf blight, and fungal infections from photos.",
          "Gundua magonjwa ya nyanya, mahindi na fangasi kwa kutumia picha za kamera."
        ),
        icon = Icons.Default.Eco,
        onClick = onNavigateToCropHealth
      )

      Spacer(modifier = Modifier.height(14.dp))

      ServiceListItem(
        title = lang.t("Yield Prediction & Harvest Forecast", "Makadirio ya Mavuno"),
        description = lang.t(
          "Forecast crop yield per acre using historical regional rain and soil data.",
          "Kadiria tani za mazao kwa ekari kulingana na mvua na udongo wa eneo lako."
        ),
        icon = Icons.Default.BarChart,
        onClick = onNavigateToYieldPrediction
      )

      Spacer(modifier = Modifier.height(14.dp))

      ServiceListItem(
        title = lang.t("Regional Market Prices & Trends", "Bei za Masoko & Mwenendo"),
        description = lang.t(
          "Live farmgate and wholesale market rates across Dodoma, Dar es Salaam, Arusha & Mwanza.",
          "Bei halisi za jumla na rejareja Dodoma, Dar es Salaam, Arusha na Mwanza."
        ),
        icon = Icons.AutoMirrored.Filled.TrendingUp,
        onClick = onNavigateToMarketPrices
      )

      Spacer(modifier = Modifier.height(14.dp))

      ServiceListItem(
        title = lang.t("Farmer Produce Marketplace", "Soko la Mazao ya Wakulima"),
        description = lang.t(
          "Sell maize, tomatoes, and grains directly to verified buyers with no intermediaries.",
          "Uza mahindi, nyanya na nafaka moja kwa moja kwa wanunuzi bila madalali."
        ),
        icon = Icons.Default.Storefront,
        onClick = onNavigateToMarketplace
      )

      Spacer(modifier = Modifier.height(14.dp))

      ServiceListItem(
        title = lang.t("Micro-Climate Weather Forecast", "Utabiri wa Hali ya Hewa"),
        description = lang.t(
          "Agricultural rain likelihood, soil moisture, and planting window advisories.",
          "Uwezekano wa mvua, unyevu wa ardhi na ushauri wa upandaji."
        ),
        icon = Icons.Default.Cloud,
        onClick = onNavigateToHome
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun ServiceListItem(
  title: String,
  description: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, CardBorder),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(BrandSurfaceGreen),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = ForestGreenPrimary,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = description,
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 16.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = TextSecondary,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}
