package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.MkulimaBrandHeader
import com.example.util.AppLanguage
import com.example.util.LocalLanguageManager

@Composable
fun SplashScreen(
  onStart: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current

  Box(
    modifier = modifier
      .fillMaxSize()
      .testTag("splash_screen")
  ) {
    // 1. Full Screen Background Farmer Visual
    Image(
      painter = painterResource(id = R.drawable.img_splash_farmer),
      contentDescription = "Farmer in lush field",
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop
    )

    // 2. Cinematic Gradient Overlay (Dark scrim for high-contrast legible text)
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.60f),
              Color.Black.copy(alpha = 0.25f),
              Color.Black.copy(alpha = 0.88f)
            )
          )
        )
    )

    // 3. Foreground Content with Top & Bottom Insets
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Header & Logo
      MkulimaBrandHeader(
        logoSize = 78.dp,
        fontSize = 34,
        textColor = Color.White,
        taglineColor = Color(0xFFDCFCE7),
        showTagline = true,
        modifier = Modifier.padding(top = 12.dp)
      )

      Spacer(modifier = Modifier.weight(1f))

      // Bottom Section: Motto, Language Selector & Single-Language Start Button
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Motto in selected language only
        Text(
          text = lang.t(
            en = "Better farming,\nbrighter future.",
            sw = "Kilimo bora,\nmaisha bora."
          ),
          color = Color.White,
          fontSize = 26.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 34.sp,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle in selected language only
        Text(
          text = lang.t(
            en = "Smart AI Farming Assistant for Tanzania",
            sw = "Msaidizi Mahiri wa AI kwa Wakulima Tanzania"
          ),
          color = Color.White.copy(alpha = 0.90f),
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // LANGUAGE SELECTION (KISWAHILI / ENGLISH)
        // ==========================================
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("language_selector_container")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              tint = Color.White.copy(alpha = 0.85f),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = lang.t("Choose Language:", "Chagua Lugha:"),
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
          }

          // Segmented Language Choice Bar
          Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.Black.copy(alpha = 0.45f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              // Kiswahili Choice
              val isKiswahili = lang.isSwahili
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isKiswahili) Color(0xFF16A34A) else Color.Transparent,
                modifier = Modifier
                  .weight(1f)
                  .height(44.dp)
                  .clickable { lang.setLanguage(AppLanguage.KISWAHILI) }
                  .testTag("lang_select_swahili")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center,
                  modifier = Modifier.fillMaxSize()
                ) {
                  if (isKiswahili) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                  }
                  Text(
                    text = "Kiswahili",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = if (isKiswahili) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }

              // English Choice
              val isEnglish = !lang.isSwahili
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isEnglish) Color(0xFF16A34A) else Color.Transparent,
                modifier = Modifier
                  .weight(1f)
                  .height(44.dp)
                  .clickable { lang.setLanguage(AppLanguage.ENGLISH) }
                  .testTag("lang_select_english")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center,
                  modifier = Modifier.fillMaxSize()
                ) {
                  if (isEnglish) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                  }
                  Text(
                    text = "English",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = if (isEnglish) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Start Button - Strictly in the single selected language!
        Button(
          onClick = onStart,
          shape = RoundedCornerShape(28.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF16A34A),
            contentColor = Color.White
          ),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("start_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = lang.t(en = "Get Started", sw = "Anza Sasa"),
              color = Color.White,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = lang.t(en = "Start", sw = "Anza"),
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}
