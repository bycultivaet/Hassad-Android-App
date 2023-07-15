package com.cultivaet.hassad.core.source.local.dao

import androidx.room.*
import com.cultivaet.hassad.domain.model.local.Facilitator

@Dao
interface FacilitatorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(facilitator: Facilitator)

    @Delete
    suspend fun delete(facilitator: Facilitator)

    @Query("SELECT * FROM facilitator")
    suspend fun getAllFacilitators(): List<Facilitator>
}