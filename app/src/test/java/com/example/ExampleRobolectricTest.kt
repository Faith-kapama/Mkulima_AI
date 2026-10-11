package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.MockDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MkulimaAI", appName)
  }

  @Test
  fun `verify maize yield prediction calculation`() {
    val prediction = MockDataProvider.predictYield("Maize", 2.5, "Dodoma", "1. Vuli")
    assertEquals(2.8, prediction.estimatedYieldMin, 0.01)
    assertEquals(3.5, prediction.estimatedYieldMax, 0.01)
    assertEquals("tons per acre", prediction.unit)
    assertEquals("1. Vuli", prediction.season)
  }

  @Test
  fun `verify default diagnosis content`() {
    val diag = MockDataProvider.defaultDiagnosis
    assertEquals("Tomato Early Blight", diag.diseaseName)
    assertEquals(87, diag.confidence)
    assertTrue(diag.recommendations.contains("Remove affected leaves"))
  }
}
