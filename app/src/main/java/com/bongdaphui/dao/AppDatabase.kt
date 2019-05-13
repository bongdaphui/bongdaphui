package com.bongdaphui.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.bongdaphui.model.FbFieldModel

@Database(entities = [FbFieldModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getItemDAO(): FieldsDAO

}