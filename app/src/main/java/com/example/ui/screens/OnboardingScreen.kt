package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

data class OnboardingPage(
  val title: String,
  val description: String,
  val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
  onFinish: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current

  val pages = listOf(
    OnboardingPage(
      title = lang.t(
        en = "Better Decisions\nfor a Brighter Tomorrow",
        sw = "Uamuzi Sahihi wa Kilimo\nkwa Maisha Bora"
      ),
      description = lang.t(
        en = "Get AI-powered advice, predict yields, check market prices and connect with buyers — all in one app.",
        sw = "Pata ushauri wa AI, tabiri mavuno, fuatilia bei za masoko na ungana na wanunuzi — yote ndani ya programu moja."
      ),
      imageRes = R.drawable.img_sprout_soil
    ),
    OnboardingPage(
      title = lang.t(
        en = "Instant AI Disease\nDetection in Seconds",
        sw = "Utambuzi wa Magonjwa\nkwa AI Ndani ya Sekunde"
      ),
      description = lang.t(
        en = "Snap a photo of crop leaves to identify tomato blight, maize rust, and get immediate treatment recommendations.",
        sw = "Piga picha jani la mmea kutambua ukungu wa nyanya, kutu ya mahindi na kupata dawa sahihi mara moja."
      ),
      imageRes = R.drawable.img_diseased_leaf
    ),
    OnboardingPage(
      title = lang.t(
        en = "Accurate Yields\n& Fair Market Prices",
        sw = "Utabiri Sahihi wa Mavuno\nna Bei za Masoko"
      ),
      description = lang.t(
        en = "Plan your harvest seasons with predictive analytics and monitor real-time crop market prices across Tanzania.",
        sw = "Panga msimu wako kwa takwimu sahihi na ufuatilie bei za mazao katika masoko mbalimbali nchini Tanzania."
      ),
      imageRes = R.drawable.img_healthy_crops
    ),
    OnboardingPage(
      title = lang.t(
        en = "Direct Farmer\nProduce Marketplace",
        sw = "Soko la Moja kwa Moja\nkwa Wakulima"
      ),
      description = lang.t(
        en = "List your grains, vegetables, and fruits for verified bulk buyers in Dodoma, Dar es Salaam, and beyond.",
        sw = "Weka nafaka, mbogamboga na matunda yako kwa wanunuzi wa jumla Dodoma, Dar es Salaam na nchi nzima."
      ),
      imageRes = R.drawable.img_sprout_soil
    )
  )

  val pagerState = rememberPagerState(pageCount = { pages.size })
  val coroutineScope = rememberCoroutineScope()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("onboarding_screen")
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Top Row with Skip (Single language)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          text = lang.t("Skip", "Ruka"),
          color = ForestGreenPrimary,
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable { onFinish() }
            .padding(8.dp)
            .testTag("onboarding_skip_button")
        )
      }

      // Horizontal Pager
      HorizontalPager(
        state = pagerState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) { page ->
        val item = pages[page]
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          // Rounded Hero Visual Frame
          Box(
            modifier = Modifier
              .size(240.dp)
              .clip(RoundedCornerShape(32.dp))
              .background(Color(0xFFF3F7F3)),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = item.imageRes),
              contentDescription = item.title,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          }

          Spacer(modifier = Modifier.height(36.dp))

          // Bold Title
          Text(
            text = item.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Subtitle / Description
          Text(
            text = item.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
          )
        }
      }

      // Page Indicator Dots
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        repeat(pages.size) { index ->
          val isSelected = pagerState.currentPage == index
          Box(
            modifier = Modifier
              .padding(horizontal = 4.dp)
              .size(if (isSelected) 8.dp else 6.dp)
              .clip(CircleShape)
              .background(if (isSelected) ForestGreenPrimary else Color(0xFFD1D5DB))
          )
        }
      }

      // Button: Next / Get Started - Strictly in single selected language!
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 28.dp, vertical = 20.dp)
      ) {
        Button(
          onClick = {
            if (pagerState.currentPage < pages.size - 1) {
              coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
              }
            } else {
              onFinish()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("onboarding_next_button"),
          shape = RoundedCornerShape(26.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Text(
            text = if (pagerState.currentPage == pages.size - 1) {
              lang.t(en = "Get Started", sw = "Anza Sasa")
            } else {
              lang.t(en = "Next", sw = "Endelea")
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
      }
    }
  }
}
