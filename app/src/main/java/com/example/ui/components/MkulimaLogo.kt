package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary

@Composable
fun MkulimaLeafLogo(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp
) {
  Image(
    painter = painterResource(id = R.drawable.img_app_logo_transparent),
    contentDescription = "MkulimaAI Leaf Logo",
    modifier = modifier.size(size)
  )
}

@Composable
fun MkulimaBrandHeader(
  modifier: Modifier = Modifier,
  logoSize: Dp = 56.dp,
  fontSize: Int = 26,
  textColor: Color = ForestGreenDark,
  taglineColor: Color = Color(0xFF4A6B53),
  showTagline: Boolean = false
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    MkulimaLeafLogo(size = logoSize)
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "MkulimaAI",
      fontSize = fontSize.sp,
      fontWeight = FontWeight.Bold,
      color = textColor,
      letterSpacing = (-0.5).sp
    )
    if (showTagline) {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Smart Solutions\nfor Better Harvests",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = taglineColor,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        lineHeight = 18.sp
      )
    }
  }
}

@Composable
fun MkulimaHorizontalLogo(
  modifier: Modifier = Modifier,
  textColor: Color = ForestGreenDark,
  logoSize: Dp = 28.dp
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    MkulimaLeafLogo(size = logoSize)
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = "MkulimaAI",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = textColor
    )
  }
}
