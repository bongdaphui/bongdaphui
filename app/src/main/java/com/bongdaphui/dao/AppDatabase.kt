package com.bongdaphui.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.UserModel

@Database(entities = [FbFieldModel::class, UserModel::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFieldDAO(): FieldsDAO

    abstract fun getUserDAO(): UsersDAO

}