package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage(val code: String, val displayName: String) {
  ENGLISH("en", "English"),
  KISWAHILI("sw", "Kiswahili")
}

class LanguageManager {
  var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)
    private set

  fun toggleLanguage() {
    currentLanguage = if (currentLanguage == AppLanguage.ENGLISH) {
      AppLanguage.KISWAHILI
    } else {
      AppLanguage.ENGLISH
    }
  }

  fun setLanguage(language: AppLanguage) {
    currentLanguage = language
  }

  val isSwahili: Boolean
    get() = currentLanguage == AppLanguage.KISWAHILI

  fun t(en: String, sw: String): String {
    return if (isSwahili) sw else en
  }
}

val LocalLanguageManager = compositionLocalOf { LanguageManager() }
