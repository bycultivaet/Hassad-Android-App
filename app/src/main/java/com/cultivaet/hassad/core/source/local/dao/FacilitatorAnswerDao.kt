package com.cultivaet.hassad.core.source.local.dao

import androidx.room.*
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer

@Dao
interface FacilitatorAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(facilitatorAnswer: FacilitatorAnswer)

    @Delete
    suspend fun delete(facilitatorAnswer: FacilitatorAnswer)

    @Query("SELECT * FROM facilitator_answer")
    suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer>
}