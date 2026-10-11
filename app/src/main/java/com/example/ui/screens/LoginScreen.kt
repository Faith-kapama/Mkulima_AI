package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.data.local.AppDatabase
import com.example.data.local.FarmerProfileManager
import com.example.data.remote.supabase.SupabaseRepository
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MkulimaBrandHeader
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondary
import com.example.util.LocalLanguageManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  onNavigateToRegister: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val lang = LocalLanguageManager.current
  val context = LocalContext.current
  var username by remember { mutableStateOf("juma.farmer@mkulima.co.tz") }
  var password by remember { mutableStateOf("mkulima2026") }
  var passwordVisible by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
      .testTag("login_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(12.dp))

      // MkulimaAI Logo & Name
      MkulimaBrandHeader(
        logoSize = 58.dp,
        fontSize = 28,
        textColor = ForestGreenDark,
        showTagline = false
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Title & Subtitle
      Text(
        text = lang.t("Welcome Back", "Karibu Tena"),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = lang.t("Login to continue to farming services", "Ingia kuendelea na huduma za kilimo"),
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Input: Phone number or email
      OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        placeholder = { Text(lang.t("Phone number or email", "Namba ya simu au barua pepe"), color = TextMuted, fontSize = 14.sp) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = "Contact",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_input_username"),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = ForestGreenPrimary,
          unfocusedBorderColor = CardBorder,
          focusedTextColor = TextPrimaryDark,
          unfocusedTextColor = TextPrimaryDark,
          focusedContainerColor = Color(0xFFFAFCFA),
          unfocusedContainerColor = Color(0xFFFAFCFA)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Password input
      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        placeholder = { Text(lang.t("Password", "Neno la siri"), color = TextMuted, fontSize = 14.sp) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Password",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
          )
        },
        trailingIcon = {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (passwordVisible) lang.t("Hide password", "Ficha neno la siri") else lang.t("Show password", "Onyesha neno la siri"),
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_input_password"),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = ForestGreenPrimary,
          unfocusedBorderColor = CardBorder,
          focusedTextColor = TextPrimaryDark,
          unfocusedTextColor = TextPrimaryDark,
          focusedContainerColor = Color(0xFFFAFCFA),
          unfocusedContainerColor = Color(0xFFFAFCFA)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
      )

      // Forgot password? Link
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          text = lang.t("Forgot password?", "Umesahau neno la siri?"),
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = ForestGreenPrimary,
          modifier = Modifier
            .clickable {
              coroutineScope.launch {
                snackbarHostState.showSnackbar(
                  lang.t("Password reset link sent to your registered contact.", "Kiungo cha kurejesha neno la siri kimetumwa kwenye simu au barua pepe yako.")
                )
              }
            }
            .padding(vertical = 4.dp)
            .testTag("login_forgot_password")
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Button: Login - Strictly in single active language!
      Button(
        onClick = {
          if (username.isBlank() || password.isBlank()) {
            coroutineScope.launch {
              snackbarHostState.showSnackbar(
                lang.t("Please enter your phone number/email and password.", "Tafadhali weka namba yako ya simu/barua pepe na neno la siri.")
              )
            }
          } else {
            // Persist logged-in profile so user history is never lost
            FarmerProfileManager.loginWithIdentifier(username, context)
            val db = AppDatabase.getDatabase(context)
            coroutineScope.launch {
              SupabaseRepository().syncCloudDiagnosesToLocal(db.diagnosisDao(), username)
            }
            onLoginSuccess()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("login_submit_button"),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
      ) {
        Text(
          text = lang.t("Login", "Ingia"),
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Don't have an account? Register
      Row(
        modifier = Modifier
          .clickable { onNavigateToRegister() }
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = lang.t("Don't have an account? ", "Huna akaunti bado? "),
          fontSize = 13.sp,
          color = TextSecondary
        )
        Text(
          text = lang.t("Register Your Farm", "Sajili Shamba Lako"),
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = ForestGreenPrimary
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Divider: Or continue with
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorder)
        Text(
          text = lang.t("Or continue with", "Au endelea na"),
          fontSize = 12.sp,
          color = TextMuted,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = CardBorder)
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Social Login Buttons: Google & Apple
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedButton(
          onClick = { onLoginSuccess() },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("login_google_button"),
          shape = RoundedCornerShape(14.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFAFAFA))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "G",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFEA4335)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Google",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = TextPrimaryDark
            )
          }
        }

        OutlinedButton(
          onClick = { onLoginSuccess() },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("login_apple_button"),
          shape = RoundedCornerShape(14.dp),
          border = BorderStroke(1.dp, CardBorder),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFAFAFA))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Apple",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = TextPrimaryDark
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    SnackbarHost(
      hostState = snackbarHostState,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp)
    )
  }
}
