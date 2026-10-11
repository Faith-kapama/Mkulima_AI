package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.data.remote.supabase.SupabaseRepository
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FarmerProfileManager
import com.example.data.model.FarmerProfile
import com.example.data.model.TanzaniaLocations
import com.example.ui.components.MkulimaBrandHeader
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
  onRegisterSuccess: () -> Unit,
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = androidx.compose.ui.platform.LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  // Form Fields - Personal
  var fullName by remember { mutableStateOf("") }
  var phoneNumber by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  // Form Fields - Location (Tanzania Regions & Districts)
  var selectedRegion by remember { mutableStateOf("Dodoma") }
  var regionDropdownExpanded by remember { mutableStateOf(false) }

  var selectedDistrict by remember { mutableStateOf("Chamwino") }
  var districtDropdownExpanded by remember { mutableStateOf(false) }

  var villageOrWard by remember { mutableStateOf("") }

  // Form Fields - Farm Essentials
  var farmSizeAcres by remember { mutableStateOf("2.5") }
  var selectedFarmingType by remember { mutableStateOf("Smallholder Farmer") }
  var selectedWaterSource by remember { mutableStateOf("Rainfed") }

  // Primary Crops (Multi-select)
  val selectedCrops = remember { mutableStateListOf("Maize", "Tomato") }

  // Preferences
  var receiveSmsAlerts by remember { mutableStateOf(true) }
  var agreedToTerms by remember { mutableStateOf(true) }

  // When Region changes, automatically update district to first of the region
  fun onRegionSelected(region: String) {
    selectedRegion = region
    regionDropdownExpanded = false
    val districts = TanzaniaLocations.getDistricts(region)
    selectedDistrict = districts.firstOrNull() ?: "Mjini"
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
      .testTag("register_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Navigation Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNavigateToLogin,
          modifier = Modifier.testTag("register_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = lang.t("Back", "Rudi"),
            tint = ForestGreenDark
          )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = lang.t("Step 1 of 1", "Hatua 1 ya 1"),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = TextMuted
        )
      }

      // Brand Header
      MkulimaBrandHeader(
        logoSize = 52.dp,
        fontSize = 26,
        textColor = ForestGreenDark,
        showTagline = false
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = lang.t("Farmer Registration", "Usajili wa Mkulima"),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark,
        textAlign = TextAlign.Center
      )

      Text(
        text = lang.t(
          "Join Tanzania's smart farming network for AI crop diagnostics, live markets, and yield forecasts.",
          "Jiunge na mtandao wa wakulima mahiri Tanzania kupata ushauri wa AI, masoko na utabiri wa mavuno."
        ),
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
      )

      // ==========================================
      // SECTION 1: PERSONAL DETAILS
      // ==========================================
      SectionCard(
        title = lang.t("1. Personal Details", "1. Taarifa Binafsi"),
        icon = Icons.Default.Person
      ) {
        // Full Name
        OutlinedTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = { Text(lang.t("Full Name", "Jina Kamili")) },
          placeholder = { Text(lang.t("e.g. Juma Rashidi", "Mfano: Juma Rashidi")) },
          leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreenPrimary)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_name"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone Number (+255)
        OutlinedTextField(
          value = phoneNumber,
          onValueChange = { phoneNumber = it },
          label = { Text(lang.t("Phone Number", "Namba ya Simu")) },
          placeholder = { Text(lang.t("0754 123 456 or +255 754 123 456", "0754 123 456 au +255 754 123 456")) },
          leadingIcon = {
            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = ForestGreenPrimary)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_phone"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email (Optional)
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text(lang.t("Email (Optional)", "Barua Pepe (Si lazima)")) },
          placeholder = { Text("juma@mkulima.co.tz") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_email"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text(lang.t("Password", "Neno la Siri")) },
          placeholder = { Text(lang.t("At least 6 characters", "Kiwango cha chini herufi 6")) },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = ForestGreenPrimary)
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = TextMuted
              )
            }
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_password"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Confirm Password
        OutlinedTextField(
          value = confirmPassword,
          onValueChange = { confirmPassword = it },
          label = { Text(lang.t("Confirm Password", "Rudia Neno la Siri")) },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = ForestGreenPrimary)
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_confirm_password"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // =========================================================
      // SECTION 2: FARM LOCATION (TANZANIA REGIONS & DISTRICTS)
      // =========================================================
      SectionCard(
        title = lang.t("2. Tanzania Location", "2. Eneo la Shamba"),
        icon = Icons.Default.LocationOn
      ) {
        Text(
          text = lang.t(
            "Select your region and district in Tanzania for localized weather alerts and nearby market prices.",
            "Chagua mkoa na wilaya yako Tanzania kwa utabiri sahihi wa hali ya hewa na bei za masoko ya karibu."
          ),
          fontSize = 12.sp,
          color = TextSecondary,
          modifier = Modifier.padding(bottom = 12.dp)
        )

        // Region Dropdown
        ExposedDropdownMenuBox(
          expanded = regionDropdownExpanded,
          onExpandedChange = { regionDropdownExpanded = !regionDropdownExpanded },
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = selectedRegion,
            onValueChange = {},
            readOnly = true,
            label = { Text(lang.t("Tanzania Region", "Mkoa wa Tanzania")) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionDropdownExpanded) },
            leadingIcon = {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = ForestGreenPrimary)
            },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .testTag("register_region_dropdown"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ForestGreenPrimary,
              unfocusedBorderColor = CardBorder,
              focusedContainerColor = Color(0xFFFAFCFA),
              unfocusedContainerColor = Color(0xFFFAFCFA)
            )
          )

          ExposedDropdownMenu(
            expanded = regionDropdownExpanded,
            onDismissRequest = { regionDropdownExpanded = false }
          ) {
            TanzaniaLocations.allRegions.forEach { regionName ->
              DropdownMenuItem(
                text = { Text(regionName, fontWeight = if (regionName == selectedRegion) FontWeight.Bold else FontWeight.Normal) },
                onClick = { onRegionSelected(regionName) }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // District Dropdown (Filtered by selected Region)
        val availableDistricts = TanzaniaLocations.getDistricts(selectedRegion)
        ExposedDropdownMenuBox(
          expanded = districtDropdownExpanded,
          onExpandedChange = { districtDropdownExpanded = !districtDropdownExpanded },
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = selectedDistrict,
            onValueChange = {},
            readOnly = true,
            label = { Text(lang.t("District ($selectedRegion)", "Wilaya ($selectedRegion)")) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtDropdownExpanded) },
            leadingIcon = {
              Icon(Icons.Default.Landscape, contentDescription = null, tint = ForestGreenPrimary)
            },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .testTag("register_district_dropdown"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ForestGreenPrimary,
              unfocusedBorderColor = CardBorder,
              focusedContainerColor = Color(0xFFFAFCFA),
              unfocusedContainerColor = Color(0xFFFAFCFA)
            )
          )

          ExposedDropdownMenu(
            expanded = districtDropdownExpanded,
            onDismissRequest = { districtDropdownExpanded = false }
          ) {
            availableDistricts.forEach { distName ->
              DropdownMenuItem(
                text = { Text(distName, fontWeight = if (distName == selectedDistrict) FontWeight.Bold else FontWeight.Normal) },
                onClick = {
                  selectedDistrict = distName
                  districtDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Village / Ward
        OutlinedTextField(
          value = villageOrWard,
          onValueChange = { villageOrWard = it },
          label = { Text(lang.t("Village or Ward (Optional)", "Kijiji au Kata (Si lazima)")) },
          placeholder = { Text(lang.t("e.g. Buigiri, Kijichi", "Mfano: Buigiri, Kijichi")) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_village"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // ==============================================================
      // SECTION 3: FARM ESSENTIALS (SIZE, CROPS, WATER)
      // ==============================================================
      SectionCard(
        title = lang.t("3. Farm Essentials", "3. Taarifa za Shamba"),
        icon = Icons.Default.Agriculture
      ) {
        Text(
          text = lang.t(
            "These details customize your Yield Predictions and personalized farming advice.",
            "Taarifa hizi zitatumika kukupa utabiri wa mavuno na ushauri wa mbolea."
          ),
          fontSize = 12.sp,
          color = TextSecondary,
          modifier = Modifier.padding(bottom = 12.dp)
        )

        // Farm Size (Acres)
        OutlinedTextField(
          value = farmSizeAcres,
          onValueChange = { farmSizeAcres = it },
          label = { Text(lang.t("Farm Size (Acres)", "Ukubwa wa Shamba (Ekari)")) },
          placeholder = { Text(lang.t("e.g. 2.5", "Mfano: 2.5")) },
          leadingIcon = {
            Icon(Icons.Default.Landscape, contentDescription = null, tint = ForestGreenPrimary)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_input_acres"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ForestGreenPrimary,
            unfocusedBorderColor = CardBorder,
            focusedContainerColor = Color(0xFFFAFCFA),
            unfocusedContainerColor = Color(0xFFFAFCFA)
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        // Quick acreage suggestion chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 14.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("1.0", "2.5", "5.0", "10.0").forEach { preset ->
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (farmSizeAcres == preset) ForestGreenPrimary else Color(0xFFF1F5F9),
              modifier = Modifier.clickable { farmSizeAcres = preset }
            ) {
              Text(
                text = "$preset ${lang.t("Acres", "Ekari")}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (farmSizeAcres == preset) Color.White else TextPrimaryDark,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        // Primary Crops (Multi-select FilterChips)
        Text(
          text = lang.t("Primary Crops (Select all that apply):", "Mazao Unayolima (Chagua yote):"),
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          TanzaniaLocations.popularCrops.forEach { (cropEn, cropSw) ->
            val isSelected = selectedCrops.contains(cropEn)
            val cropDisplayName = if (lang.isSwahili) cropSw else cropEn
            FilterChip(
              selected = isSelected,
              onClick = {
                if (isSelected) {
                  if (selectedCrops.size > 1) selectedCrops.remove(cropEn)
                } else {
                  selectedCrops.add(cropEn)
                }
              },
              label = { Text(cropDisplayName) },
              leadingIcon = if (isSelected) {
                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
              } else null,
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BrandSurfaceGreen,
                selectedLabelColor = ForestGreenDark,
                selectedLeadingIconColor = ForestGreenDark
              ),
              border = BorderStroke(1.dp, if (isSelected) ForestGreenPrimary else CardBorder)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Water Source (Chanzo cha Maji)
        Text(
          text = lang.t("Primary Water Source:", "Chanzo Kikuu cha Maji:"),
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimaryDark,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          TanzaniaLocations.waterSources.forEach { (wsEn, wsSw) ->
            val isSelected = selectedWaterSource == wsEn
            val waterDisplayName = if (lang.isSwahili) wsSw else wsEn
            FilterChip(
              selected = isSelected,
              onClick = { selectedWaterSource = wsEn },
              label = { Text(waterDisplayName) },
              leadingIcon = {
                Icon(Icons.Default.WaterDrop, contentDescription = null, modifier = Modifier.size(16.dp))
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BrandSurfaceGreen,
                selectedLabelColor = ForestGreenDark
              ),
              border = BorderStroke(1.dp, if (isSelected) ForestGreenPrimary else CardBorder)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // ===============================================
      // SECTION 4: PREFERENCES & TERMS
      // ===============================================
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF8)),
        border = BorderStroke(1.dp, CardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // SMS Market Alerts Toggle
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { receiveSmsAlerts = !receiveSmsAlerts },
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = receiveSmsAlerts,
              onCheckedChange = { receiveSmsAlerts = it },
              colors = CheckboxDefaults.colors(checkedColor = ForestGreenPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = lang.t(
                  "Receive Market Prices & Weather via SMS",
                  "Pokea Taarifa za Masoko na Hali ya Hewa kwa SMS"
                ),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
              )
              Text(
                text = lang.t(
                  "Daily market updates and rain alerts will be sent to your phone.",
                  "Tutakutumia ujumbe wa bei na mabadiliko ya mvua kwa namba yako ya simu."
                ),
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Agree to terms
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { agreedToTerms = !agreedToTerms },
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = agreedToTerms,
              onCheckedChange = { agreedToTerms = it },
              colors = CheckboxDefaults.colors(checkedColor = ForestGreenPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = lang.t(
                "I agree to MkulimaAI Tanzania terms and conditions",
                "Ninakubali vigezo na masharti ya MkulimaAI Tanzania"
              ),
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // SUBMIT BUTTON: Register - Strictly in the active single language!
      Button(
        onClick = {
          when {
            fullName.isBlank() -> {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(lang.t("Please enter your full name.", "Tafadhali weka Jina lako Kamili."))
              }
            }
            phoneNumber.isBlank() -> {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(lang.t("Please enter your phone number.", "Tafadhali weka Namba yako ya Simu."))
              }
            }
            password.length < 4 -> {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(lang.t("Password must be at least 4 characters.", "Neno la siri liwe na herufi angalau 4."))
              }
            }
            password != confirmPassword -> {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(lang.t("Passwords do not match.", "Neno la siri halifanani, thibitisha tena."))
              }
            }
            !agreedToTerms -> {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(lang.t("Please accept registration terms to continue.", "Tafadhali kubali vigezo vya usajili kuendelea."))
              }
            }
            else -> {
              val formattedPhone = when {
                phoneNumber.startsWith("0") -> "+255${phoneNumber.substring(1).replace(" ", "")}"
                !phoneNumber.startsWith("+") -> "+255${phoneNumber.replace(" ", "")}"
                else -> phoneNumber
              }

              val acresDouble = farmSizeAcres.toDoubleOrNull() ?: 2.5

              val newProfile = FarmerProfile(
                name = fullName.trim(),
                phone = formattedPhone,
                email = email.trim().ifEmpty { "farmer@mkulima.co.tz" },
                region = selectedRegion,
                district = selectedDistrict,
                village = villageOrWard.trim().ifEmpty { "Kati" },
                farmSizeAcres = acresDouble,
                primaryCrops = selectedCrops.toList(),
                farmingType = selectedFarmingType,
                waterSource = selectedWaterSource,
                receiveSmsAlerts = receiveSmsAlerts,
                memberSince = "September 2026"
              )
              FarmerProfileManager.updateProfile(newProfile, context)

              coroutineScope.launch {
                SupabaseRepository().saveFarmerProfile(
                  fullName = fullName.trim(),
                  phoneNumber = formattedPhone,
                  region = selectedRegion,
                  district = selectedDistrict,
                  farmSizeAcres = acresDouble,
                  primaryCrop = selectedCrops.firstOrNull() ?: "Mahindi"
                )
                snackbarHostState.showSnackbar(
                  lang.t(
                    "Registration successful! Welcome to MkulimaAI, ${fullName.trim()}.",
                    "Usajili umekamilika! Karibu MkulimaAI, ${fullName.trim()}."
                  )
                )
              }

              onRegisterSuccess()
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("register_submit_button"),
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
      ) {
        Text(
          text = lang.t("Complete Registration", "Kamilisha Usajili"),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Already have an account? Login - Clean single language!
      Row(
        modifier = Modifier
          .clickable { onNavigateToLogin() }
          .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = lang.t("Already have an account? ", "Una akaunti tayari? "),
          fontSize = 14.sp,
          color = TextSecondary
        )
        Text(
          text = lang.t("Login", "Ingia"),
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = ForestGreenPrimary
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    SnackbarHost(
      hostState = snackbarHostState,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp)
    )
  }
}

@Composable
private fun SectionCard(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, CardBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = BrandSurfaceGreen,
          modifier = Modifier.size(32.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = ForestGreenDark,
              modifier = Modifier.size(18.dp)
            )
          }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = ForestGreenDark
        )
      }

      HorizontalDivider(color = CardBorder.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 14.dp))

      content()
    }
  }
}
