package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.MockDataProvider
import com.example.data.model.BuyerDemandItem
import com.example.data.model.MarketPrice
import com.example.data.model.TanzaniaLocations
import com.example.data.remote.supabase.SupabaseRepository
import com.example.ui.components.MkulimaBottomNav
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForecastAmberBg
import com.example.ui.theme.ForecastAmberBorder
import com.example.ui.theme.ForecastAmberText
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.MarketAdviceBorder
import com.example.ui.theme.MarketAdviceMint
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

/**
 * Market Prices Screen matching Proposed Design (Image 2)
 * Features:
 * - Maize vs Tomato segmented pill tabs
 * - Region & Market filters
 * - Latest market price card with crop photo
 * - Next month's estimate card with experimental forecast amber badge
 * - Selling advice card with mint container
 * - "Buyers seeking maize / tomatoes" with contact actions
 * - "+ Post your produce" sticky button
 * - Full bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketPricesScreen(
  onBackClick: () -> Unit,
  onPostProduceClick: () -> Unit = {},
  onNavigateHome: () -> Unit = {},
  onNavigateServices: () -> Unit = {},
  onNavigateMarketplace: () -> Unit = {},
  onNavigateProfile: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  // Selected crop: "Maize" or "Tomato"
  var selectedCropTab by remember { mutableStateOf("Maize") }

  // Filter state
  var selectedRegion by remember { mutableStateOf("Dodoma") }
  var selectedMarket by remember { mutableStateOf("Select market") }

  var regionMenuExpanded by remember { mutableStateOf(false) }
  var marketMenuExpanded by remember { mutableStateOf(false) }

  // Contact buyer dialog state
  var contactBuyerTarget by remember { mutableStateOf<BuyerDemandItem?>(null) }

  val regionOptions = listOf("All Regions") + TanzaniaLocations.allRegions
  val marketOptions = listOf("Select market", "Kariakoo", "Tandale", "Majengo", "Kibaigwa", "Mwanjelwa", "Arusha Kuu")

  val isMaize = selectedCropTab.equals("Maize", ignoreCase = true)

  // Live Supabase integration with automatic regional data
  val supabaseRepo = remember { SupabaseRepository() }
  var livePrices by remember { mutableStateOf<List<MarketPrice>>(emptyList()) }

  LaunchedEffect(Unit) {
    val fetched = supabaseRepo.fetchMarketPrices()
    if (fetched.isNotEmpty()) {
      livePrices = fetched
    }
  }

  // Dynamic price calculation based on region and crop
  val currentPricePerKg = remember(selectedCropTab, selectedRegion) {
    if (isMaize) {
      when (selectedRegion) {
        "Dar es Salaam" -> 920
        "Arusha" -> 880
        "Mbeya" -> 820
        "Morogoro" -> 840
        else -> 850
      }
    } else {
      when (selectedRegion) {
        "Dar es Salaam" -> 1450
        "Arusha" -> 1150
        "Mbeya" -> 1100
        "Morogoro" -> 1250
        else -> 1200
      }
    }
  }

  val nextMonthEstimate = remember(currentPricePerKg, isMaize) {
    if (isMaize) (currentPricePerKg * 1.08).toInt() else (currentPricePerKg * 1.12).toInt()
  }

  val activeMarketName = if (selectedMarket != "Select market") selectedMarket else (if (isMaize) "Tandale / Kibaigwa" else "Majengo")
  val observedDate = "09 Oct 2026"
  val forecastMonth = "November 2026"

  val buyersList: List<BuyerDemandItem> = if (isMaize) MockDataProvider.buyersSeekingMaize else MockDataProvider.buyersSeekingTomatoes

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
              text = lang.t("Market Prices", "Bei za Masoko"),
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
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
        actions = {
          // Spacer for visual balance
          Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = ForestGreenDark)
      )
    },
    bottomBar = {
      MkulimaBottomNav(
        currentRoute = "marketplace",
        onNavigate = { route ->
          when (route) {
            "home" -> onNavigateHome()
            "services" -> onNavigateServices()
            "marketplace" -> onNavigateMarketplace()
            "profile" -> onNavigateProfile()
          }
        }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFF9FBF9)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .testTag("market_prices_screen")
    ) {

      // =========================================================================
      // 1. CROP SELECTOR TABS (Segmented pill: Maize | Tomato) (Image 2)
      // =========================================================================
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(Color.White)
          .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Maize Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isMaize) ForestGreenDark else Color.Transparent)
            .clickable { selectedCropTab = "Maize" }
            .testTag("tab_maize"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = lang.t("Maize", "Mahindi"),
            fontSize = 15.sp,
            fontWeight = if (isMaize) FontWeight.Bold else FontWeight.Medium,
            color = if (isMaize) Color.White else TextSecondary
          )
        }

        // Tomato Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (!isMaize) ForestGreenDark else Color.Transparent)
            .clickable { selectedCropTab = "Tomato" }
            .testTag("tab_tomato"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = lang.t("Tomato", "Nyanya"),
            fontSize = 15.sp,
            fontWeight = if (!isMaize) FontWeight.Bold else FontWeight.Medium,
            color = if (!isMaize) Color.White else TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // =========================================================================
      // 2. FILTERS ROW: Region Dropdown & Market Dropdown (Image 2)
      // =========================================================================
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Region Filter
        Box(modifier = Modifier.weight(1f)) {
          Card(
            onClick = { regionMenuExpanded = true },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("filter_region_button")
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = TextSecondary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = selectedRegion,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = TextPrimaryDark,
                  maxLines = 1
                )
              }
              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          DropdownMenu(
            expanded = regionMenuExpanded,
            onDismissRequest = { regionMenuExpanded = false }
          ) {
            regionOptions.forEach { reg ->
              DropdownMenuItem(
                text = { Text(reg) },
                onClick = {
                  selectedRegion = reg
                  regionMenuExpanded = false
                }
              )
            }
          }
        }

        // Market Filter
        Box(modifier = Modifier.weight(1.1f)) {
          Card(
            onClick = { marketMenuExpanded = true },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("filter_market_button")
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Storefront,
                  contentDescription = null,
                  tint = TextSecondary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = selectedMarket,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = TextPrimaryDark,
                  maxLines = 1
                )
              }
              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          DropdownMenu(
            expanded = marketMenuExpanded,
            onDismissRequest = { marketMenuExpanded = false }
          ) {
            marketOptions.forEach { mkt ->
              DropdownMenuItem(
                text = { Text(mkt) },
                onClick = {
                  selectedMarket = mkt
                  marketMenuExpanded = false
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // =========================================================================
      // 3. LATEST MARKET PRICE CARD (Image 2)
      // =========================================================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("latest_price_card"),
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
          // Left: Crop Image
          val cropDrawable = if (isMaize) R.drawable.img_corn_cob else R.drawable.img_tomatoes
          Image(
            painter = painterResource(id = cropDrawable),
            contentDescription = if (isMaize) "Maize cob" else "Ripe tomatoes",
            modifier = Modifier
              .size(90.dp)
              .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
          )

          Spacer(modifier = Modifier.width(16.dp))

          // Right: Price details
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = lang.t("Latest market price", "Bei ya sasa sokoni"),
              fontSize = 13.sp,
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "$currentPricePerKg TZS/kg",
              fontSize = 22.sp,
              fontWeight = FontWeight.ExtraBold,
              color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Market: $activeMarketName",
              fontSize = 12.sp,
              color = TextMuted,
              fontWeight = FontWeight.Normal
            )

            Text(
              text = "Observed: $observedDate",
              fontSize = 11.5.sp,
              color = TextMuted,
              fontWeight = FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // =========================================================================
      // 4. NEXT MONTH'S ESTIMATE CARD (Image 2)
      // =========================================================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("next_month_estimate_card"),
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
          // Left: Green Trending Bar Chart Illustration
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFEFF7F2)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.BarChart,
              contentDescription = "Forecast Chart",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(46.dp)
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = lang.t("Next month's estimate", "Makadirio ya mwezi ujao"),
              fontSize = 12.5.sp,
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )

            Text(
              text = forecastMonth,
              fontSize = 11.sp,
              color = TextMuted
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = "$nextMonthEstimate TZS/kg",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )

            Text(
              text = lang.t("Regional monthly average", "Wastani wa mwezi wa kikanda"),
              fontSize = 11.sp,
              color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Warning Badge: Experimental forecast (Image 2 Amber badge)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ForecastAmberBg)
                .border(1.dp, ForecastAmberBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = "Experimental",
                  tint = ForecastAmberText,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = lang.t("Experimental forecast", "Makadirio ya majaribio"),
                  fontSize = 10.5.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForecastAmberText
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // =========================================================================
      // 5. SELLING ADVICE CARD (Image 2)
      // =========================================================================
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("selling_advice_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MarketAdviceMint),
        border = BorderStroke(1.dp, MarketAdviceBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.Top
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(ForestGreenPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = "Advice",
              tint = ForestGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = lang.t("Selling advice", "Ushauri wa kuuza"),
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            val adviceText = if (isMaize) {
              lang.t(
                "Compare buyer offers with expected storage costs before deciding to sell or store.",
                "Linganisha ofa za wanunuzi na gharama za ghala kabla ya kuamua kuuza au kuhifadhi mahindi."
              )
            } else {
              lang.t(
                "Aim to sell within 2–3 days. Sell sooner if very ripe. This is a selling target, not a shelf-life guarantee.",
                "Lenga kuuza ndani ya siku 2–3. Uza mapema zaidi kama nyanya zimeiva sana. Hili ni lengo la mauzo."
              )
            }
            Text(
              text = adviceText,
              fontSize = 12.5.sp,
              color = TextPrimaryDark.copy(alpha = 0.9f),
              lineHeight = 17.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // =========================================================================
      // 6. BUYERS SECTION ("Buyers seeking maize / tomatoes") (Image 2)
      // =========================================================================
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Group,
          contentDescription = null,
          tint = ForestGreenDark,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        val buyersHeader = if (isMaize) {
          lang.t("Buyers seeking maize", "Wanunuzi wanaotafuta mahindi")
        } else {
          lang.t("Buyers seeking tomatoes", "Wanunuzi wanaotafuta nyanya")
        }
        Text(
          text = buyersHeader,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Buyer cards list
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        buyersList.forEach { buyer ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("buyer_card_${buyer.id}"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Left: Storefront circular badge
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFF1F5F2)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Storefront,
                  contentDescription = null,
                  tint = ForestGreenDark,
                  modifier = Modifier.size(22.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              // Middle: Buyer info
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "${buyer.buyerName} • ${buyer.location}",
                  fontSize = 13.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark,
                  maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Needs: ${buyer.neededQuantityKg} kg",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
                Text(
                  text = "Offer: ${buyer.offerPricePerKgTzs} TZS/kg",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForestGreenPrimary
                )
              }

              Spacer(modifier = Modifier.width(8.dp))

              // Right: "Contact buyer" outlined pill button
              OutlinedButton(
                onClick = { contactBuyerTarget = buyer },
                modifier = Modifier
                  .height(36.dp)
                  .testTag("contact_buyer_${buyer.id}"),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, ForestGreenPrimary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
              ) {
                Text(
                  text = lang.t("Contact buyer", "Wasiliana"),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForestGreenDark
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(22.dp))

      // =========================================================================
      // 7. BOTTOM ACTION: "+ Post your produce" BUTTON (Image 2)
      // =========================================================================
      Button(
        onClick = onPostProduceClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("post_produce_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Post",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = lang.t("+ Post your produce", "+ Weka tangazo la zao lako"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
    }
  }

  // =========================================================================
  // Contact Buyer Dialog
  // =========================================================================
  if (contactBuyerTarget != null) {
    val b = contactBuyerTarget!!
    AlertDialog(
      onDismissRequest = { contactBuyerTarget = null },
      title = {
        Text(
          text = lang.t("Contact ${b.buyerName}", "Wasiliana na ${b.buyerName}"),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
      },
      text = {
        Column {
          Text(
            text = "Location: ${b.location}\nNeeds: ${b.neededQuantityKg} kg of ${b.cropType}\nOffer: ${b.offerPricePerKgTzs} TZS/kg",
            fontSize = 13.5.sp,
            color = TextSecondary,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Phone: ${b.contactPhone}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenDark
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${b.contactPhone}"))
              context.startActivity(intent)
            } catch (e: Exception) {
              coroutineScope.launch {
                snackbarHostState.showSnackbar("Call ${b.contactPhone}")
              }
            }
            contactBuyerTarget = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(lang.t("Call", "Piga Simu"))
        }
      },
      dismissButton = {
        TextButton(onClick = { contactBuyerTarget = null }) {
          Text(lang.t("Cancel", "Funga"))
        }
      }
    )
  }
}
