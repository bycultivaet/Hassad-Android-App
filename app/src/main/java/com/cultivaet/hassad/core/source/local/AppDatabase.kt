package com.cultivaet.hassad.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cultivaet.hassad.core.source.local.dao.FacilitatorAnswerDao
import com.cultivaet.hassad.core.source.local.dao.FarmerDao
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.local.Farmer

@Database(
    entities = [FacilitatorAnswer::class, Farmer::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun facilitatorAnswerDao(): FacilitatorAnswerDao

    abstract fun farmerDao(): FarmerDao
}