package com.bongdaphui.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.model.UserModel

@Database(
    entities = [FbFieldModel::class, UserModel::class,
        SchedulePlayerModel::class, ScheduleClubModel::class], version = 3, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFieldDAO(): FieldsDAO

    abstract fun getUserDAO(): UsersDAO

    abstract fun getSchedulePlayerDAO(): SchedulePlayerDAO

    abstract fun getScheduleClubDAO(): ScheduleClubDAO

}