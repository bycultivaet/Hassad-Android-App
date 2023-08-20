package com.cultivaet.hassad.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cultivaet.hassad.core.source.local.dao.FacilitatorAnswerDao
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer

@Database(
    entities = [FacilitatorAnswer::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun facilitatorAnswerDao(): FacilitatorAnswerDao
}