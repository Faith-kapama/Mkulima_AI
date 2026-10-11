package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.FarmerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FarmerProfileManager {

  private const val PREFS_NAME = "mkulima_farmer_profile"
  private const val KEY_NAME = "farmer_name"
  private const val KEY_PHONE = "farmer_phone"
  private const val KEY_EMAIL = "farmer_email"
  private const val KEY_REGION = "farmer_region"
  private const val KEY_DISTRICT = "farmer_district"
  private const val KEY_VILLAGE = "farmer_village"
  private const val KEY_FARM_SIZE = "farmer_farm_size"
  private const val KEY_CROPS = "farmer_crops"
  private const val KEY_FARMING_TYPE = "farmer_farming_type"
  private const val KEY_WATER_SOURCE = "farmer_water_source"
  private const val KEY_SMS_ALERTS = "farmer_sms_alerts"
  private const val KEY_MEMBER_SINCE = "farmer_member_since"
  private const val KEY_IS_LOGGED_IN = "farmer_is_logged_in"

  private var sharedPreferences: SharedPreferences? = null

  private val defaultProfile = FarmerProfile(
    name = "Juma Rashidi",
    phone = "+255 754 123 456",
    email = "juma.farmer@mkulima.co.tz",
    region = "Dodoma",
    district = "Chamwino",
    village = "Buigiri",
    farmSizeAcres = 3.5,
    primaryCrops = listOf("Maize", "Tomato"),
    farmingType = "Smallholder Farmer",
    waterSource = "Rainfed",
    receiveSmsAlerts = true,
    memberSince = "September 2026"
  )

  private val _currentProfile = MutableStateFlow(defaultProfile)
  val currentProfile: StateFlow<FarmerProfile> = _currentProfile.asStateFlow()

  fun init(context: Context) {
    if (sharedPreferences == null) {
      sharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      loadPersistedProfile()
    }
  }

  private fun loadPersistedProfile() {
    val prefs = sharedPreferences ?: return
    val savedName = prefs.getString(KEY_NAME, null) ?: return
    val savedPhone = prefs.getString(KEY_PHONE, defaultProfile.phone) ?: defaultProfile.phone
    val savedEmail = prefs.getString(KEY_EMAIL, defaultProfile.email) ?: defaultProfile.email
    val savedRegion = prefs.getString(KEY_REGION, defaultProfile.region) ?: defaultProfile.region
    val savedDistrict = prefs.getString(KEY_DISTRICT, defaultProfile.district) ?: defaultProfile.district
    val savedVillage = prefs.getString(KEY_VILLAGE, defaultProfile.village) ?: defaultProfile.village
    val savedFarmSize = prefs.getFloat(KEY_FARM_SIZE, defaultProfile.farmSizeAcres.toFloat()).toDouble()
    val savedCropsStr = prefs.getString(KEY_CROPS, "Maize,Tomato") ?: "Maize,Tomato"
    val savedFarmingType = prefs.getString(KEY_FARMING_TYPE, defaultProfile.farmingType) ?: defaultProfile.farmingType
    val savedWaterSource = prefs.getString(KEY_WATER_SOURCE, defaultProfile.waterSource) ?: defaultProfile.waterSource
    val savedSms = prefs.getBoolean(KEY_SMS_ALERTS, true)
    val savedMemberSince = prefs.getString(KEY_MEMBER_SINCE, defaultProfile.memberSince) ?: defaultProfile.memberSince

    _currentProfile.value = FarmerProfile(
      name = savedName,
      phone = savedPhone,
      email = savedEmail,
      region = savedRegion,
      district = savedDistrict,
      village = savedVillage,
      farmSizeAcres = savedFarmSize,
      primaryCrops = savedCropsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() },
      farmingType = savedFarmingType,
      waterSource = savedWaterSource,
      receiveSmsAlerts = savedSms,
      memberSince = savedMemberSince
    )
  }

  fun updateProfile(profile: FarmerProfile, context: Context? = null) {
    _currentProfile.value = profile
    val prefs = sharedPreferences ?: context?.applicationContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)?.also {
      sharedPreferences = it
    }
    prefs?.edit()?.apply {
      putString(KEY_NAME, profile.name)
      putString(KEY_PHONE, profile.phone)
      putString(KEY_EMAIL, profile.email)
      putString(KEY_REGION, profile.region)
      putString(KEY_DISTRICT, profile.district)
      putString(KEY_VILLAGE, profile.village)
      putFloat(KEY_FARM_SIZE, profile.farmSizeAcres.toFloat())
      putString(KEY_CROPS, profile.primaryCrops.joinToString(","))
      putString(KEY_FARMING_TYPE, profile.farmingType)
      putString(KEY_WATER_SOURCE, profile.waterSource)
      putBoolean(KEY_SMS_ALERTS, profile.receiveSmsAlerts)
      putString(KEY_MEMBER_SINCE, profile.memberSince)
      putBoolean(KEY_IS_LOGGED_IN, true)
      apply()
    }
  }

  fun loginWithIdentifier(identifier: String, context: Context? = null) {
    val clean = identifier.trim()
    val current = _currentProfile.value
    val updated = if (clean.contains("@")) {
      val nameFromEmail = clean.substringBefore("@").replace(".", " ").split(" ")
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
      current.copy(
        email = clean,
        name = if (current.email == clean) current.name else nameFromEmail
      )
    } else {
      val formattedPhone = when {
        clean.startsWith("0") -> "+255${clean.substring(1).replace(" ", "")}"
        !clean.startsWith("+") -> "+255${clean.replace(" ", "")}"
        else -> clean
      }
      current.copy(phone = formattedPhone)
    }
    updateProfile(updated, context)
  }

  fun isLoggedIn(context: Context? = null): Boolean {
    val prefs = sharedPreferences ?: context?.applicationContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs?.getBoolean(KEY_IS_LOGGED_IN, false) ?: false
  }

  fun getProfile(): FarmerProfile = _currentProfile.value
}
