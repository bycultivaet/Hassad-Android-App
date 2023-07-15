package com.cultivaet.hassad.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cultivaet.hassad.core.source.local.dao.FacilitatorDao
import com.cultivaet.hassad.domain.model.local.Facilitator

@Database(
    entities = [Facilitator::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun facilitatorDao(): FacilitatorDao
}