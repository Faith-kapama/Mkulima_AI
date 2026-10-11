package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextPrimaryDark

@Composable
fun MkulimaGreenHeader(
  title: String,
  onBackClick: (() -> Unit)? = null,
  showNotification: Boolean = false,
  onNotificationClick: (() -> Unit)? = null,
  isHomeHeader: Boolean = false
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .height(56.dp)
        .padding(horizontal = 12.dp),
      contentAlignment = Alignment.Center
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (onBackClick != null) {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier
              .size(44.dp)
              .testTag("top_bar_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = TextPrimaryDark
            )
          }
        } else if (isHomeHeader) {
          MkulimaHorizontalLogo(
            modifier = Modifier.padding(start = 6.dp),
            textColor = ForestGreenDark,
            logoSize = 28.dp
          )
        } else {
          Spacer(modifier = Modifier.width(44.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showNotification) {
          IconButton(
            onClick = { onNotificationClick?.invoke() },
            modifier = Modifier
              .size(44.dp)
              .testTag("notifications_button")
          ) {
            BadgedBox(
              badge = {
                Badge(
                  containerColor = Color(0xFFE53935),
                  modifier = Modifier.size(8.dp)
                )
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TextPrimaryDark,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        } else {
          Spacer(modifier = Modifier.width(44.dp))
        }
      }

      if (!isHomeHeader) {
        Text(
          text = title,
          color = TextPrimaryDark,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      }
    }
    HorizontalDivider(
      thickness = 0.8.dp,
      color = CardBorder.copy(alpha = 0.6f)
    )
  }
}
