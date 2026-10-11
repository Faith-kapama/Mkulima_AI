package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
  @Query("SELECT * FROM saved_diagnoses ORDER BY timestamp DESC")
  fun getAllDiagnoses(): Flow<List<DiagnosisEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDiagnosis(diagnosis: DiagnosisEntity): Long

  @Delete
  suspend fun deleteDiagnosis(diagnosis: DiagnosisEntity)
}
