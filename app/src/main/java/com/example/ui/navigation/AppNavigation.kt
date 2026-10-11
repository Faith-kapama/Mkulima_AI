package com.example.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.model.MaizeYieldInput
import com.example.ui.screens.AiCompanionChatScreen
import com.example.ui.screens.CropHealthScanScreen
import com.example.ui.screens.DiagnosisResultScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MarketPricesScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.YieldPredictionInputScreen
import com.example.ui.screens.YieldPredictionResultScreen

@Composable
fun AppNavigation(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController()
) {
  var lastScannedImageUri by remember { mutableStateOf<String?>(null) }
  var lastScannedCrop by remember { mutableStateOf("Maize") }
  var maizeYieldInputState by remember { mutableStateOf(MaizeYieldInput()) }

  NavHost(
    navController = navController,
    startDestination = Screen.Splash.route,
    modifier = modifier
  ) {
    // 1. Splash Screen
    composable(Screen.Splash.route) {
      SplashScreen(
        onStart = {
          navController.navigate(Screen.Onboarding.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
          }
        }
      )
    }

    // 2. Onboarding Screen
    composable(Screen.Onboarding.route) {
      OnboardingScreen(
        onFinish = {
          navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Onboarding.route) { inclusive = true }
          }
        }
      )
    }

    // 3. Login Screen
    composable(Screen.Login.route) {
      LoginScreen(
        onLoginSuccess = {
          navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
          }
        },
        onNavigateToRegister = {
          navController.navigate(Screen.Register.route)
        }
      )
    }

    // 3b. Register Screen
    composable(Screen.Register.route) {
      RegisterScreen(
        onRegisterSuccess = {
          navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
          }
        },
        onNavigateToLogin = {
          navController.popBackStack()
        }
      )
    }

    // 4. Home Screen / Dashboard
    composable(Screen.Home.route) {
      HomeScreen(
        onNavigateToCropHealth = { navController.navigate(Screen.CropHealthScan.route) },
        onNavigateToYieldPrediction = { navController.navigate(Screen.YieldPredictionInput.route) },
        onNavigateToMarketPrices = { navController.navigate(Screen.MarketPrices.route) },
        onNavigateToMarketplace = { navController.navigate(Screen.Marketplace.route) },
        onNavigateToServices = { navController.navigate(Screen.Services.route) },
        onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
        onNavigateToAiCompanion = { navController.navigate(Screen.AiCompanion.route) }
      )
    }

    // 5. Crop Health / Scan
    composable(Screen.CropHealthScan.route) {
      CropHealthScanScreen(
        onBackClick = { navController.popBackStack() },
        onDiagnosisReady = { uriString, crop ->
          lastScannedImageUri = uriString
          lastScannedCrop = crop
          navController.navigate(Screen.DiagnosisResult.route)
        }
      )
    }

    // 6. Diagnosis Result
    composable(Screen.DiagnosisResult.route) {
      DiagnosisResultScreen(
        imageUriString = lastScannedImageUri,
        cropName = lastScannedCrop,
        onBackClick = { navController.popBackStack() },
        onScanAnother = { navController.popBackStack() }
      )
    }

    // 7. Yield Prediction Input
    composable(Screen.YieldPredictionInput.route) {
      YieldPredictionInputScreen(
        onBackClick = { navController.popBackStack() },
        onPredictionReady = { input ->
          maizeYieldInputState = input
          navController.navigate(Screen.YieldPredictionResult.route)
        }
      )
    }

    // 8. Yield Prediction Result
    composable(Screen.YieldPredictionResult.route) {
      YieldPredictionResultScreen(
        input = maizeYieldInputState,
        onBackClick = { navController.popBackStack() },
        onTryDifferentInputs = { navController.popBackStack() }
      )
    }

    // 9. Market Prices
    composable(Screen.MarketPrices.route) {
      MarketPricesScreen(
        onBackClick = { navController.popBackStack() },
        onPostProduceClick = { navController.navigate(Screen.Marketplace.route) },
        onNavigateHome = { navController.navigate(Screen.Home.route) },
        onNavigateServices = { navController.navigate(Screen.Services.route) },
        onNavigateMarketplace = { navController.navigate(Screen.Marketplace.route) },
        onNavigateProfile = { navController.navigate(Screen.Profile.route) }
      )
    }

    // 10. Marketplace
    composable(Screen.Marketplace.route) {
      MarketplaceScreen(
        onBackClick = { navController.popBackStack() },
        onNavigateToHome = {
          navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
          }
        },
        onNavigateToServices = { navController.navigate(Screen.Services.route) },
        onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
      )
    }

    // Extra: Services Screen
    composable(Screen.Services.route) {
      ServicesScreen(
        onNavigateToCropHealth = { navController.navigate(Screen.CropHealthScan.route) },
        onNavigateToYieldPrediction = { navController.navigate(Screen.YieldPredictionInput.route) },
        onNavigateToMarketPrices = { navController.navigate(Screen.MarketPrices.route) },
        onNavigateToMarketplace = { navController.navigate(Screen.Marketplace.route) },
        onNavigateToHome = {
          navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
          }
        },
        onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
        onNavigateToAiCompanion = { navController.navigate(Screen.AiCompanion.route) }
      )
    }

    // Extra: Farmer Profile Screen
    composable(Screen.Profile.route) {
      ProfileScreen(
        onBackClick = { navController.popBackStack() },
        onNavigateToHome = {
          navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
          }
        },
        onNavigateToServices = { navController.navigate(Screen.Services.route) },
        onNavigateToMarketplace = { navController.navigate(Screen.Marketplace.route) },
        onLogout = {
          navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
          }
        }
      )
    }

    // Extra: AI Companion Chat Screen
    composable(Screen.AiCompanion.route) {
      AiCompanionChatScreen(
        onBackClick = { navController.popBackStack() }
      )
    }
  }
}
