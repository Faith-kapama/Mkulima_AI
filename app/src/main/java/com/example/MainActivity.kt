package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.MyApplicationTheme
import com.example.util.LanguageManager
import com.example.util.LocalLanguageManager

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    com.example.data.local.FarmerProfileManager.init(this)
    setContent {
      val languageManager = remember { LanguageManager() }
      CompositionLocalProvider(LocalLanguageManager provides languageManager) {
        MyApplicationTheme {
          AppNavigation(modifier = Modifier.fillMaxSize())
        }
      }
    }
  }
}
