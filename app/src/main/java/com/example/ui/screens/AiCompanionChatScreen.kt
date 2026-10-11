package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.BrandSurfaceGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
  val id: String,
  val text: String,
  val isUser: Boolean,
  val timestamp: String = "Now"
)

@Composable
fun AiCompanionChatScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  var inputQuery by remember { mutableStateOf("") }
  var isTyping by remember { mutableStateOf(false) }

  val messages = remember {
    mutableStateListOf(
      ChatMessage(
        id = "1",
        text = if (lang.isSwahili) {
          "Habari Mkulima! Mimi ni Msaidizi wako wa Kilimo cha Kisasa (Mkulima AI Companion). Naweza kukusaidia kuhusu mwenendo wa bei sokoni, mikakati ya biashara ya kilimo, faida ya mazao, pembejeo bora, au hali ya hewa. Ungependa kuanzia wapi leo?"
        } else {
          "Hello, Farmer! I'm your Mkulima AI Companion. I can help you analyze market price trends, agribusiness opportunities, high-yield crop advice, and farm budgeting. How can I assist your farm business today?"
        },
        isUser = false
      )
    )
  }

  val quickPrompts = if (lang.isSwahili) {
    listOf(
      "📈 Mwenendo wa bei ya mahindi Dodoma",
      "🍅 Jinsi ya kuongeza faida ya nyanya",
      "💼 Mikakati ya kuuza moja kwa moja kwa wanunuzi",
      "🌧️ Utabiri wa mvua msimu wa Mvuli"
    )
  } else {
    listOf(
      "📈 Maize price trends in Dodoma",
      "🍅 How to maximize tomato profits",
      "💼 Direct-to-buyer sales strategy",
      "🌧️ Weather impact on planting season"
    )
  }

  fun sendQuery(queryText: String) {
    if (queryText.isBlank()) return
    val userMsg = ChatMessage(id = System.currentTimeMillis().toString(), text = queryText, isUser = true)
    messages.add(userMsg)
    inputQuery = ""
    isTyping = true

    coroutineScope.launch {
      delay(1200)
      isTyping = false

      val botReply = generateSmartResponse(queryText, lang.isSwahili)
      messages.add(
        ChatMessage(
          id = (System.currentTimeMillis() + 1).toString(),
          text = botReply,
          isUser = false
        )
      )
      delay(100)
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("AI Agribusiness Companion", "Msaidizi wa Biashara ya Kilimo"),
        onBackClick = onBackClick
      )
    },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFF7FAF7)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .imePadding()
    ) {
      // Top Companion Status Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(BrandSurfaceGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = ForestGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = lang.t("Mkulima AI Agribusiness Advisor", "Mshauri wa Biashara Mkulima AI"),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
            Text(
              text = lang.t("Online • Expert on East Africa Markets & Crops", "Yupo Hewani • Mtaalamu wa Mazao & Masoko"),
              fontSize = 11.sp,
              color = ForestGreenDark
            )
          }
        }
      }

      // Quick Suggestion Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(quickPrompts) { prompt ->
          SuggestionChip(
            onClick = { sendQuery(prompt.substring(2).trim()) },
            label = { Text(prompt, fontSize = 12.sp, color = TextPrimaryDark) },
            colors = SuggestionChipDefaults.suggestionChipColors(
              containerColor = Color.White
            )
          )
        }
      }

      // Messages List
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(messages, key = { it.id }) { msg ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
          ) {
            if (!msg.isUser) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .background(ForestGreenPrimary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Eco,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
            }

            Card(
              shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (msg.isUser) 16.dp else 4.dp,
                bottomEnd = if (msg.isUser) 4.dp else 16.dp
              ),
              colors = CardDefaults.cardColors(
                containerColor = if (msg.isUser) ForestGreenPrimary else Color.White
              ),
              modifier = Modifier.widthIn(max = 290.dp)
            ) {
              Text(
                text = msg.text,
                fontSize = 13.5.sp,
                color = if (msg.isUser) Color.White else TextPrimaryDark,
                lineHeight = 19.sp,
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }

        if (isTyping) {
          item {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(start = 38.dp, top = 4.dp)
            ) {
              CircularProgressIndicator(
                color = ForestGreenPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = lang.t("Mkulima AI is analyzing trends...", "Mkulima AI inachambua takwimu..."),
                fontSize = 11.sp,
                color = TextMuted
              )
            }
          }
        }
      }

      // Input Bottom Bar
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputQuery,
            onValueChange = { inputQuery = it },
            placeholder = {
              Text(
                text = lang.t("Ask about trends, prices, harvest...", "Uliza kuhusu masoko, bei, faida..."),
                fontSize = 13.sp,
                color = TextMuted
              )
            },
            modifier = Modifier
              .weight(1f)
              .testTag("ai_chat_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { sendQuery(inputQuery) }),
            singleLine = true
          )

          IconButton(
            onClick = { sendQuery(inputQuery) },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (inputQuery.isNotBlank()) ForestGreenPrimary else BrandSurfaceGreen)
              .testTag("ai_chat_send_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = if (inputQuery.isNotBlank()) Color.White else ForestGreenDark,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

fun generateSmartResponse(query: String, isSwahili: Boolean): String {
  val q = query.lowercase()
  if (isSwahili) {
    return when {
      q.contains("bei") || q.contains("soko") || q.contains("trend") ->
        "Mwenendo wa bei huko Dodoma unaonyesha kupanda kwa mahindi hadi TZS 850 - 920 kwa kilo kutokana na uhitaji mkubwa wa viwanda vya unga. Kwa upande wa nyanya, bei inacheza kati ya TZS 800 - 1,200/kg. Ushauri wa biashara: Hifadhi mahindi vizuri ukitumia mifuko ya PICS kuepuka wadudu ili uuze mwezi Novemba ambapo bei hufikia TZS 1,050/kg."
      q.contains("nyanya") || q.contains("faida") ->
        "Ili kuongeza faida ya nyanya:\n1. Tumia mbegu chotara (F1) kama vile Anna F1 au Eden F1 zenye ukinzani wa magonjwa.\n2. Vuna asubuhi mapema na panga kwa madaraja (Grade A & B) kuuza bei ya juu sokoni.\n3. Jiunge na jukwaa la 'Marketplace' hapa MkulimaAI kuuza moja kwa moja kwa migahawa na maduka ya jumla bila madalali."
      q.contains("mvua") || q.contains("hali ya hewa") || q.contains("hewa") ->
        "Kanda ya Kati (Dodoma) inatarajia msimu wa mvua wa Mvuli kuanza mwishoni mwa mwezi ujao kwa kiwango cha wastani (450-600mm). Hakikisha unatayarisha mashamba mapema na kuchagua mbegu fupi za mahindi (za kukomaa siku 90-110) zinazostahimili ukame."
      else ->
        "Ushauri wa MkulimaAI: Katika kilimo biashara, kupunguza gharama za pembejeo na kuuza mazao yenye thamani iliyoongezwa (packaging & grading) kunaleta hadi 35% ya faida ya ziada. Unaweza pia kuchunguza sehemu yetu ya 'Market Prices' kuona mwenendo wa miji ya Dar es Salaam, Mwanza na Arusha."
    }
  } else {
    return when {
      q.contains("price") || q.contains("trend") || q.contains("market") ->
        "Market Intelligence Report: Dodoma maize wholesale has gained +8.5% this quarter, reaching TZS 850/kg, driven by regional milling demand. Tomato crates average TZS 800-1,200/kg. Recommendation: Hold graded dry grain in hermetic bags to target peak demand in Q4 when prices historically touch TZS 1,050/kg."
      q.contains("profit") || q.contains("tomato") || q.contains("business") ->
        "To maximize agribusiness profits:\n1. Adopt certified hybrid seeds (e.g., Anna F1) with bacterial wilt tolerance.\n2. Post your lots directly in our 'Marketplace' tab to bypass middleman broker fees (saving up to 18%).\n3. Schedule staggered planting to harvest when supply in local open-air markets dips."
      q.contains("weather") || q.contains("rain") ->
        "Seasonal forecast for Central Region shows early rains starting late next month with 450-600mm aggregate volume. Farmers are advised to conduct dry planting with medium-maturity drought-tolerant certified seed."
      else ->
        "Agribusiness Tip: Improving post-harvest handling and selling direct to institutional buyers on MkulimaAI yields an average of 25-40% higher return per acre. Check the 'Yield Prediction' tab to benchmark your expected harvest against current regional inputs."
    }
  }
}
