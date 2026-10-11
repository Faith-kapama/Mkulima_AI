package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.MkulimaGreenHeader
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

fun createTempPictureUri(context: Context): Uri {
  val tempFile = File.createTempFile("crop_scan_${System.currentTimeMillis()}", ".jpg", context.cacheDir).apply {
    createNewFile()
    deleteOnExit()
  }
  val authority = "${context.packageName}.fileprovider"
  return FileProvider.getUriForFile(context, authority, tempFile)
}

@Composable
fun CropHealthScanScreen(
  onBackClick: () -> Unit,
  onDiagnosisReady: (String?, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  var selectedCrop by remember { mutableStateOf("Maize") }
  var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
  var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
  var isScanning by remember { mutableStateOf(false) }

  // Laser beam animation for scanning
  val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
  val laserOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 260f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "laser_y"
  )

  fun triggerScanProcess(uri: Uri?) {
    selectedImageUri = uri
    isScanning = true
    coroutineScope.launch {
      delay(1500)
      isScanning = false
      onDiagnosisReady(uri?.toString(), selectedCrop)
    }
  }

  // Camera Capture Launcher
  val takePictureLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
  ) { success: Boolean ->
    if (success && pendingCameraUri != null) {
      triggerScanProcess(pendingCameraUri)
    } else {
      // Fallback or user cancelled, still allow demonstration
      triggerScanProcess(pendingCameraUri)
    }
  }

  // Camera Permission Launcher
  val cameraPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    if (isGranted) {
      try {
        val uri = createTempPictureUri(context)
        pendingCameraUri = uri
        takePictureLauncher.launch(uri)
      } catch (e: Exception) {
        triggerScanProcess(null)
      }
    } else {
      coroutineScope.launch {
        snackbarHostState.showSnackbar(
          lang.t(
            "Camera permission needed to take photos. You can also pick from gallery.",
            "Ruhusa ya kamera inahitajika kupiga picha. Unaweza pia kuchagua kutoka gallery."
          )
        )
      }
      // Demo fallback scan
      triggerScanProcess(null)
    }
  }

  // Gallery Picker Launcher
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      triggerScanProcess(uri)
    }
  }

  fun launchCameraFlow() {
    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
      try {
        val uri = createTempPictureUri(context)
        pendingCameraUri = uri
        takePictureLauncher.launch(uri)
      } catch (e: Exception) {
        triggerScanProcess(null)
      }
    } else {
      cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }

  Scaffold(
    topBar = {
      MkulimaGreenHeader(
        title = lang.t("Crop Health", "Afya ya Mazao"),
        onBackClick = onBackClick
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    modifier = modifier.fillMaxSize(),
    containerColor = Color.White
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Title
      Text(
        text = lang.t("Scan Your Crop", "Chunguza Mmea Wako"),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Description
      Text(
        text = lang.t(
          "Take or upload a clear photo of the\nplant leaves.",
          "Piga au pakia picha iliyo wazi ya\nmajani ya mmea."
        ),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Crop Selector: Maize (Mahindi) vs Tomato (Nyanya)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        val isMaizeSelected = selectedCrop.equals("Maize", ignoreCase = true)
        val isTomatoSelected = selectedCrop.equals("Tomato", ignoreCase = true)

        Card(
          onClick = { selectedCrop = "Maize" },
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .testTag("select_crop_maize"),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(
            width = if (isMaizeSelected) 2.dp else 1.dp,
            color = if (isMaizeSelected) ForestGreenPrimary else CardBorder
          ),
          colors = CardDefaults.cardColors(
            containerColor = if (isMaizeSelected) ForestGreenPrimary.copy(alpha = 0.12f) else Color.White
          )
        ) {
          Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "🌽 " + lang.t("Maize", "Mahindi"),
              fontSize = 15.sp,
              fontWeight = if (isMaizeSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isMaizeSelected) ForestGreenDark else TextPrimaryDark
            )
          }
        }

        Card(
          onClick = { selectedCrop = "Tomato" },
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .testTag("select_crop_tomato"),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(
            width = if (isTomatoSelected) 2.dp else 1.dp,
            color = if (isTomatoSelected) Color(0xFFD32F2F) else CardBorder
          ),
          colors = CardDefaults.cardColors(
            containerColor = if (isTomatoSelected) Color(0xFFD32F2F).copy(alpha = 0.12f) else Color.White
          )
        ) {
          Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "🍅 " + lang.t("Tomato", "Nyanya"),
              fontSize = 15.sp,
              fontWeight = if (isTomatoSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isTomatoSelected) Color(0xFFC62828) else TextPrimaryDark
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Leaf Image Preview Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .testTag("crop_scan_preview_card"),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          if (selectedImageUri != null) {
            AsyncImage(
              model = selectedImageUri,
              contentDescription = "Selected leaf photo",
              modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp)),
              contentScale = ContentScale.Crop
            )
          } else {
            val defaultDrawable = if (selectedCrop.equals("Maize", ignoreCase = true)) {
              R.drawable.img_corn_cob
            } else {
              R.drawable.img_diseased_leaf
            }
            Image(
              painter = painterResource(id = defaultDrawable),
              contentDescription = "Sample crop leaf with spots",
              modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp)),
              contentScale = ContentScale.Crop
            )
          }

          // Visual scanning effects: Laser scanning beam + Overlay
          if (isScanning) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
            )

            // Animated high-tech laser scan line
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .offset(y = laserOffset.dp)
                .height(4.dp)
                .background(
                  Brush.horizontalGradient(
                    listOf(
                      Color.Transparent,
                      Color(0xFF00E676),
                      Color.White,
                      Color(0xFF00E676),
                      Color.Transparent
                    )
                  )
                )
            )

            // Central Scanning status
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clip(RoundedCornerShape(14.dp))
                  .background(Color.Black.copy(alpha = 0.7f))
                  .padding(horizontal = 20.dp, vertical = 14.dp)
              ) {
                CircularProgressIndicator(color = Color(0xFF00E676), strokeWidth = 3.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = lang.t("Scanning leaf with Mkulima AI...", "Mkulima AI inachambua jani..."),
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = lang.t("Detecting pathogens & blight spots", "Inatambua vijidudu & madoa"),
                  color = Color(0xFFB0BEC5),
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Primary Green Button: Take Photo (Direct Camera Access)
      Button(
        onClick = { launchCameraFlow() },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("take_photo_button"),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Camera",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = lang.t("Take Photo with Camera", "Piga Picha kwa Kamera"),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Secondary Button: Upload from Gallery
      OutlinedButton(
        onClick = {
          photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("upload_gallery_button"),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "Gallery",
            tint = ForestGreenDark,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = lang.t("Upload from Gallery", "Pakia Kutoka Gallery"),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenDark
          )
        }
      }
    }
  }
}
