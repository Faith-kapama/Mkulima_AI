package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.FarmerProfileManager
import com.example.data.local.MockDataProvider
import com.example.data.model.MarketplaceProduct
import com.example.data.remote.supabase.SupabaseRepository
import com.example.ui.components.MkulimaBottomNav
import com.example.ui.components.MkulimaGreenHeader
import androidx.compose.runtime.LaunchedEffect
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
  onBackClick: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToServices: () -> Unit,
  onNavigateToProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  var selectedTab by remember { mutableStateOf("For Sale") }
  var products by remember { mutableStateOf(MockDataProvider.defaultProducts) }
  var buyerRequests by remember { mutableStateOf(MockDataProvider.buyerRequests) }

  val supabaseRepo = remember { SupabaseRepository() }
  LaunchedEffect(Unit) {
    val liveProds = supabaseRepo.fetchMarketplaceProducts()
    if (liveProds.isNotEmpty()) {
      products = liveProds
    }
  }

  var activeContactProduct by remember { mutableStateOf<MarketplaceProduct?>(null) }
  var showPostDialog by remember { mutableStateOf(false) }
  var editingProduct by remember { mutableStateOf<MarketplaceProduct?>(null) }
  var productToDelete by remember { mutableStateOf<MarketplaceProduct?>(null) }

  // Form Fields
  var formTitle by remember { mutableStateOf("") }
  var formLocation by remember { mutableStateOf("Dodoma") }
  var formQuantity by remember { mutableStateOf("") }
  var formPrice by remember { mutableStateOf("") }
  var formImageUri by remember { mutableStateOf<Uri?>(null) }

  // Photo Picker Launcher
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      formImageUri = uri
    }
  }

  fun openCreateDialog() {
    editingProduct = null
    formTitle = ""
    formLocation = "Dodoma"
    formQuantity = ""
    formPrice = ""
    formImageUri = null
    showPostDialog = true
  }

  fun openEditDialog(item: MarketplaceProduct) {
    editingProduct = item
    formTitle = item.title
    formLocation = item.location
    formQuantity = item.quantityKg.toString()
    formPrice = item.pricePerKgTzs.toString()
    formImageUri = item.imageUri?.let { Uri.parse(it) }
    showPostDialog = true
  }

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("Marketplace", "Soko la Mazao"),
        onBackClick = onBackClick
      )
    },
    bottomBar = {
      MkulimaBottomNav(
        currentRoute = "marketplace",
        onNavigate = { route ->
          when (route) {
            "home" -> onNavigateToHome()
            "services" -> onNavigateToServices()
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
        .testTag("marketplace_screen")
    ) {
      // Top Segmented Tabs: "For Sale" | "Find Buyers"
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFFF1F5F2))
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val tab1 = lang.t("For Sale", "Mazao Yanayouzwa")
        val tab2 = lang.t("Find Buyers", "Wanunuzi Wanaotafuta")
        listOf("For Sale" to tab1, "Find Buyers" to tab2).forEach { (tabKey, tabLabel) ->
          val isSelected = selectedTab == tabKey
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) ForestGreenPrimary else Color.Transparent)
              .clickable { selectedTab = tabKey }
              .padding(vertical = 10.dp)
              .testTag("marketplace_tab_${tabKey.lowercase().replace(" ", "_")}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tabLabel,
              fontSize = 13.5.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else TextSecondary
            )
          }
        }
      }

      // Products List (Scrollable)
      val currentList = if (selectedTab == "For Sale") products else buyerRequests

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(currentList, key = { it.id }) { item ->
          ProductItemCard(
            product = item,
            onContactClick = { activeContactProduct = item },
            onEditClick = { openEditDialog(item) },
            onDeleteClick = { productToDelete = item }
          )
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }

      // Bottom Sticky Button: "+ Post Your Produce"
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Button(
          onClick = { openCreateDialog() },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("post_produce_button"),
          shape = RoundedCornerShape(26.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = lang.t("Post Your Produce", "Weka Mazao Yako Sokoni"),
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.White
            )
          }
        }
      }
    }
  }

  // Contact Bottom Sheet
  activeContactProduct?.let { product ->
    ModalBottomSheet(
      onDismissRequest = { activeContactProduct = null },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
      containerColor = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = lang.t("Contact Seller", "Wasiliana na Muuzaji"),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${product.sellerName} • ${product.location}",
          fontSize = 14.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(18.dp))

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAF7))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = product.title,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TextPrimaryDark
            )
            Text(
              text = "${lang.t("Quantity", "Kiasi")}: ${product.quantityKg} kg @ TZS ${product.pricePerKgTzs}/kg",
              fontSize = 13.sp,
              color = ForestGreenDark
            )
            Text(
              text = "${lang.t("Phone", "Simu")}: ${product.sellerPhone}",
              fontSize = 13.sp,
              color = TextSecondary,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = {
              val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${product.sellerPhone}")
              }
              context.startActivity(dialIntent)
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
          ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text(lang.t("Call", "Piga Simu"))
          }

          OutlinedButton(
            onClick = {
              val cleanPhone = product.sellerPhone.replace(" ", "").replace("+", "")
              val waIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=Hello%20${product.sellerName},%20I%20saw%20your%20listing%20on%20MkulimaAI")
              )
              try {
                context.startActivity(waIntent)
              } catch (e: Exception) {
                coroutineScope.launch {
                  snackbarHostState.showSnackbar(lang.t("Opening WhatsApp...", "Inafungua WhatsApp..."))
                }
              }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, ForestGreenPrimary)
          ) {
            Icon(imageVector = Icons.Default.Chat, contentDescription = "WhatsApp", tint = ForestGreenPrimary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("WhatsApp", color = ForestGreenPrimary)
          }
        }

        Spacer(modifier = Modifier.height(28.dp))
      }
    }
  }

  // Post / Edit Produce Dialog (With Picture Upload, Edit & Delete)
  if (showPostDialog) {
    AlertDialog(
      onDismissRequest = { showPostDialog = false },
      title = {
        Text(
          text = if (editingProduct == null) lang.t("Post Your Produce", "Weka Mazao Sokoni") else lang.t("Edit Produce Listing", "Hariri Tangazo la Mazao"),
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Picture Upload Preview & Picker Button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF1F5F2))
              .clickable {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
              .testTag("upload_produce_photo_button"),
            contentAlignment = Alignment.Center
          ) {
            if (formImageUri != null) {
              AsyncImage(
                model = formImageUri,
                contentDescription = "Uploaded crop photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              Box(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(8.dp)
                  .clip(CircleShape)
                  .background(Color.Black.copy(alpha = 0.6f))
                  .padding(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Change photo",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            } else {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                  imageVector = Icons.Default.AddPhotoAlternate,
                  contentDescription = "Upload Picture",
                  tint = ForestGreenPrimary,
                  modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = lang.t("Upload Crop Photo", "Pakia Picha ya Mazao"),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForestGreenDark
                )
                Text(
                  text = lang.t("Tap to select from gallery", "Gusa kuchagua kutoka kwenye picha"),
                  fontSize = 10.5.sp,
                  color = TextMuted
                )
              }
            }
          }

          OutlinedTextField(
            value = formTitle,
            onValueChange = { formTitle = it },
            label = { Text(lang.t("Title (e.g. Fresh Tomatoes)", "Kichwa (mf. Nyanya Safi)")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          OutlinedTextField(
            value = formQuantity,
            onValueChange = { formQuantity = it },
            label = { Text(lang.t("Quantity (kg)", "Kiasi (kilo)")) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          OutlinedTextField(
            value = formPrice,
            onValueChange = { formPrice = it },
            label = { Text(lang.t("Price per kg (TZS)", "Bei kwa kilo (TZS)")) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          OutlinedTextField(
            value = formLocation,
            onValueChange = { formLocation = it },
            label = { Text(lang.t("Location (e.g. Dodoma)", "Mahali (mf. Dodoma)")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (formTitle.isNotBlank() && formQuantity.isNotBlank() && formPrice.isNotBlank()) {
              val currentEditing = editingProduct
              if (currentEditing == null) {
                val profile = FarmerProfileManager.getProfile()
                val newProd = MarketplaceProduct(
                  id = "prod-${System.currentTimeMillis()}",
                  title = formTitle,
                  cropType = if (formTitle.contains("Maize", true)) "Maize" else "Tomato",
                  location = formLocation.ifBlank { "${profile.district}, ${profile.region}" },
                  quantityKg = formQuantity.toIntOrNull() ?: 500,
                  pricePerKgTzs = formPrice.toIntOrNull() ?: 800,
                  sellerName = profile.name,
                  sellerPhone = profile.phone,
                  imageUri = formImageUri?.toString()
                )
                products = listOf(newProd) + products
                coroutineScope.launch {
                  snackbarHostState.showSnackbar(
                    lang.t("Your produce has been published to marketplace!", "Mazao yako yamewekwa sokoni kikamilifu!")
                  )
                }
              } else {
                products = products.map { item ->
                  if (item.id == currentEditing.id) {
                    currentEditing.copy(
                      title = formTitle,
                      location = formLocation,
                      quantityKg = formQuantity.toIntOrNull() ?: currentEditing.quantityKg,
                      pricePerKgTzs = formPrice.toIntOrNull() ?: currentEditing.pricePerKgTzs,
                      imageUri = formImageUri?.toString() ?: currentEditing.imageUri
                    )
                  } else {
                    item
                  }
                }
                coroutineScope.launch {
                  snackbarHostState.showSnackbar(
                    lang.t("Product listing updated!", "Tangazo la mazao limesasishwa!")
                  )
                }
              }
              showPostDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Text(if (editingProduct == null) lang.t("Publish", "Chapisha") else lang.t("Save", "Hifadhi"))
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showPostDialog = false }) {
          Text(lang.t("Cancel", "Ghairi"))
        }
      }
    )
  }

  // Delete Confirmation Dialog
  productToDelete?.let { prod ->
    AlertDialog(
      onDismissRequest = { productToDelete = null },
      title = {
        Text(
          text = lang.t("Delete Produce Listing?", "Futa Tangazo hili la Mazao?"),
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      },
      text = {
        Text(
          text = lang.t(
            "Are you sure you want to remove '${prod.title}' from the marketplace?",
            "Je, una uhakika unataka kuondoa '${prod.title}' kwenye soko?"
          ),
          fontSize = 13.5.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            products = products.filter { it.id != prod.id }
            buyerRequests = buyerRequests.filter { it.id != prod.id }
            productToDelete = null
            coroutineScope.launch {
              snackbarHostState.showSnackbar(
                lang.t("Product removed from marketplace.", "Tangazo limeondolewa sokoni.")
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
          Text(lang.t("Delete", "Futa"), color = Color.White)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { productToDelete = null }) {
          Text(lang.t("Cancel", "Ghairi"))
        }
      }
    )
  }
}

@Composable
fun ProductItemCard(
  product: MarketplaceProduct,
  onContactClick: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("product_card_${product.id}"),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, CardBorder),
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left Thumbnail Image (Uploaded or bundled drawable)
      if (product.imageUri != null) {
        AsyncImage(
          model = product.imageUri,
          contentDescription = product.title,
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp)),
          contentScale = ContentScale.Crop
        )
      } else {
        val imgRes = if (product.cropType.equals("Maize", ignoreCase = true)) {
          R.drawable.img_maize_cobs
        } else {
          R.drawable.img_tomatoes
        }
        Image(
          painter = painterResource(id = imgRes),
          contentDescription = product.title,
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp)),
          contentScale = ContentScale.Crop
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Middle Details
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = product.title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
        Text(
          text = product.location,
          fontSize = 12.sp,
          fontWeight = FontWeight.Normal,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${String.format("%,d", product.quantityKg)} kg",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = TextSecondary
        )
        Text(
          text = "TZS ${product.pricePerKgTzs}/kg",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = ForestGreenDark
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Right: Edit, Delete Icons + Contact Button
      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Edit product button
          IconButton(
            onClick = onEditClick,
            modifier = Modifier
              .size(32.dp)
              .testTag("edit_product_${product.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Edit Product",
              tint = ForestGreenDark,
              modifier = Modifier.size(16.dp)
            )
          }

          // Delete product button
          IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
              .size(32.dp)
              .testTag("delete_product_${product.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Product",
              tint = Color(0xFFD32F2F),
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Contact Outlined Button
        OutlinedButton(
          onClick = onContactClick,
          modifier = Modifier
            .height(34.dp)
            .testTag("contact_button_${product.id}"),
          shape = RoundedCornerShape(18.dp),
          border = BorderStroke(1.dp, ForestGreenPrimary),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
          Text(
            text = lang.t("Contact", "Mawasiliano"),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenPrimary
          )
        }
      }
    }
  }
}
