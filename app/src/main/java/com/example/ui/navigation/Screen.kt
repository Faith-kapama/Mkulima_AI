package com.example.ui.navigation

sealed class Screen(val route: String) {
  data object Splash : Screen("splash")
  data object Onboarding : Screen("onboarding")
  data object Login : Screen("login")
  data object Register : Screen("register")
  data object Home : Screen("home")
  data object CropHealthScan : Screen("crop_health_scan")
  data object DiagnosisResult : Screen("diagnosis_result")
  data object YieldPredictionInput : Screen("yield_prediction_input")
  data object YieldPredictionResult : Screen("yield_prediction_result")
  data object MarketPrices : Screen("market_prices")
  data object Marketplace : Screen("marketplace")
  data object Profile : Screen("profile")
  data object Services : Screen("services")
  data object AiCompanion : Screen("ai_companion")
}
