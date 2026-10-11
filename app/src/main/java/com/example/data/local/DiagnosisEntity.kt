package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_diagnoses")
data class DiagnosisEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val cropName: String,
  val diseaseName: String,
  val confidence: Int,
  val recommendations: String, // comma or newline separated
  val imageUri: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)
