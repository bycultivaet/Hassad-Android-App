package com.cultivaet.hassad.core.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cultivaet.hassad.domain.model.local.Farmer

@Dao
interface FarmerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(farmer: Farmer)

    @Delete
    suspend fun delete(farmer: Farmer)

    @Query("SELECT * FROM farmer")
    suspend fun getFarmers(): List<Farmer>
}