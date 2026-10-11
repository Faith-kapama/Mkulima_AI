package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted

enum class BottomNavItem(
  val route: String,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  HOME("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
  SERVICES("services", "Services", Icons.Filled.GridView, Icons.Outlined.GridView),
  MARKETPLACE("marketplace", "Marketplace", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
  PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MkulimaBottomNav(
  currentRoute: String,
  onNavigate: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.White)
      .navigationBarsPadding()
  ) {
    HorizontalDivider(thickness = 1.dp, color = CardBorder)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavItem.entries.forEach { item ->
        val isSelected = currentRoute == item.route
        val color = if (isSelected) ForestGreenPrimary else TextMuted
        val icon = if (isSelected) item.selectedIcon else item.unselectedIcon

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = false, radius = 24.dp),
              onClick = { onNavigate(item.route) }
            )
            .testTag("nav_item_${item.label.lowercase()}")
        ) {
          Icon(
            imageVector = icon,
            contentDescription = item.label,
            tint = color,
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = color,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }
  }
}
