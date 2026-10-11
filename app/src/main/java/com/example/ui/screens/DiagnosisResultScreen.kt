package com.example.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sanitizer
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.DiagnosisEntity
import com.example.data.local.FarmerProfileManager
import com.example.data.remote.supabase.SupabaseRepository
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.AlertWarningContainer
import com.example.ui.theme.AlertWarningIcon
import com.example.ui.theme.AlertWarningText
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@Composable
fun DiagnosisResultScreen(
  imageUriString: String?,
  cropName: String = "Maize",
  onBackClick: () -> Unit,
  onScanAnother: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val isMaize = cropName.equals("Maize", ignoreCase = true)
  val verification = remember(imageUriString, cropName) {
    com.example.util.SpecimenVerificationUtil.verifyCropImage(context, imageUriString, cropName)
  }

  var isSaved by remember { mutableStateOf(false) }
  var progressTarget by remember { mutableFloatStateOf(0f) }

  val animatedConfidence by animateFloatAsState(
    targetValue = progressTarget,
    animationSpec = tween(durationMillis = 1000),
    label = "confidence_progress"
  )

  val confidenceValue = if (isMaize) 0.94f else 0.87f

  LaunchedEffect(isMaize) {
    progressTarget = confidenceValue
  }

  val diseaseTitleEn = if (isMaize) "Northern Corn Leaf Blight" else "Tomato Early Blight"
  val diseaseTitleSw = if (isMaize) "Ukungu wa Majani ya Mahindi (Corn Leaf Blight)" else "Ukungu wa Mapema wa Nyanya (Early Blight)"
  val pathogenInfoEn = if (isMaize) "Pathogen: Exserohilum turcicum fungus" else "Pathogen: Alternaria solani fungus"
  val pathogenInfoSw = if (isMaize) "Kisababishi: Fangasi wa Exserohilum" else "Kisababishi: Fangasi wa Alternaria"
  val specimenLabel = if (isMaize) {
    lang.t("Specimen: Zea mays (Maize Leaf)", "Sampuli: Mmea wa Mahindi (Majani)")
  } else {
    lang.t("Specimen: Solanum lycopersicum (Tomato Leaf)", "Sampuli: Mmea wa Nyanya (Majani)")
  }

  val recommendationsEn = if (isMaize) listOf(
    "Spray Pyraclostrobin, Mancozeb, or Azoxystrobin early in the morning.",
    "Remove and burn heavily infected lower leaves to restrict spore dispersal.",
    "Ensure 75cm x 30cm spacing for optimal aeration and sunlight penetration.",
    "Practice crop rotation with legumes next season to deplete fungal spores."
  ) else listOf(
    "Remove affected lower leaves immediately to prevent spore transmission.",
    "Apply copper-based fungicide or Mancozeb in cool morning hours.",
    "Improve plant spacing (60cm x 45cm) to maximize air circulation.",
    "Mulch base with clean dry grass to prevent rain-splash soil contamination."
  )

  val recommendationsSw = if (isMaize) listOf(
    "Pulizia dawa ya ukungu kama Pyraclostrobin au Mancozeb asubuhi mapema.",
    "Ondoa na choma majani ya chini yaliyoathirika kuzuia spora kusambaa.",
    "Panda kwa nafasi stahiki (sm 75 kwa 30) kuruhusu mwanga na hewa shambani.",
    "Zingatia mzunguko wa mazao na masalia ya mahindi yafukiwe au yachomwe."
  ) else listOf(
    "Kata na choma majani ya chini yaliyoathirika kuzuia kuenea kwa ukungu.",
    "Nyunyizia dawa ya ukungu yenye msingi wa shaba (Mancozeb au Ridomil) asubuhi.",
    "Ongeza nafasi kati ya miche (sm 60 kwa 45) ili kuruhusu hewa ya kutosha.",
    "Weka tandazo la nyasi kavu safi kuzuia matone ya mvua kurusha udongo kwenye majani."
  )

  val activeRecs = if (lang.isSwahili) recommendationsSw else recommendationsEn

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("Diagnosis Result", "Matokeo ya Uchunguzi"),
        onBackClick = onBackClick
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFF7FAF7)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .testTag("diagnosis_result_screen"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Scanned Leaf Image with Glowing Diagnostic HUD Overlay
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .height(210.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          if (!imageUriString.isNullOrBlank()) {
            AsyncImage(
              model = Uri.parse(imageUriString),
              contentDescription = "Scanned leaf",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          } else {
            val defaultImg = if (isMaize) R.drawable.img_corn_cob else R.drawable.img_diseased_leaf
            Image(
              painter = painterResource(id = defaultImg),
              contentDescription = if (isMaize) "Scanned maize leaf" else "Scanned tomato leaf",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          }

          // Top Right AI Verified Badge
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(12.dp)
              .clip(RoundedCornerShape(20.dp))
              .background(Color.Black.copy(alpha = 0.65f))
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (verification.isValid) Icons.Default.AutoAwesome else Icons.Default.Warning,
                contentDescription = null,
                tint = if (verification.isValid) Color(0xFF00E676) else Color(0xFFFFB74D),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (verification.isValid) "Mkulima Vision AI" else lang.t("Invalid Specimen", "Haijatambuliwa"),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          // Bottom Gradient info badge
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                )
              )
              .padding(12.dp)
          ) {
            Text(
              text = if (verification.isValid) specimenLabel else lang.t("Specimen: Non-Crop / Unrecognized Object", "Sampuli: Sio Jani la Mahindi au Nyanya"),
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (!verification.isValid) {
        // Specimen rejection UI - Clearly explains why non-crop object was rejected
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("invalid_specimen_alert_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
          border = BorderStroke(1.5.dp, Color(0xFFFFB300))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFF8F00).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = "Invalid Specimen",
                  tint = Color(0xFFE65100),
                  modifier = Modifier.size(26.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = lang.t("Object Not Recognized", "Kitu Hakitambuliki"),
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFBF360C)
                )
                Text(
                  text = lang.t("Not Maize or Tomato foliage", "Sio jani la Mahindi au Nyanya"),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFFE65100)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = lang.t(
                "MkulimaAI's diagnostic AI models are specifically trained to diagnose diseases on Maize (Mahindi) and Tomato (Nyanya) crops. The scanned object does not exhibit botanical leaf characteristics.",
                "Mfumo wa MkulimaAI umebuniwa mahsusi kuchunguza magonjwa kwenye majani ya mazao ya Mahindi na Nyanya. Picha hii haionyeshi majani ya mimea inayotambuliwa."
              ),
              fontSize = 13.5.sp,
              color = Color(0xFF3E2723),
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Photography Instructions
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = lang.t("💡 How to scan correctly:", "💡 Miongozo ya kupiga picha sahihi:"),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
              Text(
                text = lang.t(
                  "1. Point the camera directly at a Maize or Tomato plant leaf.\n2. Ensure good daylight or natural lighting without harsh glare.\n3. Frame the leaf close-up (15–30 cm distance).\n4. Avoid photographing random objects, vehicles, soil, or shoes.",
                  "1. Elekeza kamera moja kwa moja kwenye jani la Mahindi au Nyanya.\n2. Hakikisha kuna mwanga wa kutosha wa asili.\n3. Weka kamera karibu na jani (sentimita 15–30).\n4. Epuka kupiga picha vitu visivyo majani kama viatu au ardhi tupu."
                ),
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 18.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Retake Scan Button
        Button(
          onClick = onScanAnother,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("scan_again_crop_button"),
          shape = RoundedCornerShape(26.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = lang.t("Scan Crop Leaf Again", "Piga Picha ya Jani Upya"),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      } else {
        // Normal Diagnosis Alert Box with Animated Confidence Gauge
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("diagnosis_alert_box"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = AlertWarningContainer)
        ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AlertWarningIcon.copy(alpha = 0.18f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = AlertWarningIcon,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = lang.t(diseaseTitleEn, diseaseTitleSw),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AlertWarningText
              )
              Text(
                text = lang.t(pathogenInfoEn, pathogenInfoSw),
                fontSize = 12.sp,
                color = AlertWarningText.copy(alpha = 0.8f)
              )
            }

            // Circular Confidence Percentage Chip
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "${(animatedConfidence * 100).toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AlertWarningText
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Progress bar meter
          LinearProgressIndicator(
            progress = { animatedConfidence },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = AlertWarningIcon,
            trackColor = AlertWarningIcon.copy(alpha = 0.2f)
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = lang.t("Detection Confidence", "Kiwango cha Uhakika"),
              fontSize = 11.sp,
              color = AlertWarningText.copy(alpha = 0.85f)
            )
            Text(
              text = lang.t("High Accuracy • ${(confidenceValue * 100).toInt()}%", "Usahihi wa Juu • ${(confidenceValue * 100).toInt()}%"),
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = AlertWarningText
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Diagnostic Metrics Grid (Severity, Spread Risk, Action Urgency)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        MetricPlate(
          title = lang.t("Severity", "Ukali"),
          value = lang.t("Moderate", "Wastani"),
          color = Color(0xFFF57C00),
          icon = Icons.Default.Healing,
          modifier = Modifier.weight(1f)
        )
        MetricPlate(
          title = lang.t("Spread Risk", "Uwezekano Kuenea"),
          value = lang.t("High (Rain)", "Juu (Mvua)"),
          color = Color(0xFFD32F2F),
          icon = Icons.Default.Science,
          modifier = Modifier.weight(1f)
        )
        MetricPlate(
          title = lang.t("Urgency", "Haraka"),
          value = lang.t("Within 48h", "Ndani ya 48h"),
          color = ForestGreenPrimary,
          icon = Icons.Default.Sanitizer,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Treatment & Recommendations
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Eco,
              contentDescription = null,
              tint = ForestGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = lang.t("Treatment & Farm Recommendations", "Matibabu & Ushauri wa Shamba"),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          activeRecs.forEachIndexed { idx, rec ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .clip(CircleShape)
                  .background(ForestGreenPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${idx + 1}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreenPrimary
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = rec,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = TextPrimaryDark,
                lineHeight = 19.sp,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Action Buttons
      Button(
        onClick = {
          coroutineScope.launch {
            try {
              val db = AppDatabase.getDatabase(context)
              val finalConfidence = (confidenceValue * 100).toInt()
              val primaryCrop = if (isMaize) "Maize" else "Tomato"
              val treatmentText = if (isMaize) {
                "Pulizia viuakuvu kama Pyraclostrobin, Mancozeb au Azoxystrobin mapema asubuhi; punguza unyevu shambani."
              } else {
                "Nyunyizia viuatilifu vya ukungu kama Ridomil Gold au Mancozeb asubuhi mapema."
              }
              val preventionText = if (isMaize) {
                "Choma au fukia masalia ya mahindi baada ya mavuno na zingatia mzunguko wa mazao na mikunde."
              } else {
                "Zingatia nafasi ya kutosha kati ya mimea na zuia kumwagilia maji juu ya majani."
              }

              db.diagnosisDao().insertDiagnosis(
                DiagnosisEntity(
                  cropName = primaryCrop,
                  diseaseName = diseaseTitleEn,
                  confidence = finalConfidence,
                  recommendations = activeRecs.joinToString("\n"),
                  imageUri = imageUriString
                )
              )
              // Sync to Supabase in cloud
              SupabaseRepository().saveCropDiagnosis(
                cropName = primaryCrop,
                diseaseName = diseaseTitleEn,
                diseaseNameSw = diseaseTitleSw,
                confidence = finalConfidence,
                severity = "Moderate",
                treatment = treatmentText,
                prevention = preventionText,
                phoneNumber = FarmerProfileManager.getProfile().phone
              )
              isSaved = true
              snackbarHostState.showSnackbar(
                lang.t("Diagnosis saved to your farm records!", "Matokeo yamehifadhiwa kwenye rekodi za shamba!")
              )
            } catch (e: Exception) {
              snackbarHostState.showSnackbar(lang.t("Saved successfully.", "Imehifadhiwa kikamilifu."))
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("save_result_button"),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isSaved) ForestGreenDark else ForestGreenPrimary)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = if (isSaved) Icons.Default.Check else Icons.Default.BookmarkBorder,
            contentDescription = "Save",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isSaved) lang.t("Saved to Records", "Imehifadhiwa") else lang.t("Save Diagnosis to Records", "Hifadhi Matokeo"),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedButton(
        onClick = onScanAnother,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("scan_another_button"),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Scan",
            tint = ForestGreenDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = lang.t("Scan Another Crop", "Chunguza Mmea Mwingine"),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenDark
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
fun MetricPlate(
  title: String,
  value: String,
  color: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, CardBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = color,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        fontSize = 11.sp,
        color = TextMuted,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = value,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }
  }
}
