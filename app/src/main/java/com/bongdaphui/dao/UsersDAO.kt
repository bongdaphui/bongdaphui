package com.bongdaphui.dao

import android.arch.persistence.room.*
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.UserModel

@Dao
interface UsersDAO {

    @Insert
    fun insert(item: UserModel)

    @Update
    fun update(item: UserModel)

    @Delete
    fun delete(item: UserModel)

    @Query("SELECT * FROM users")
    fun getItems(): List<UserModel>

    @Query("DELETE FROM users")
    fun deleteTable()


}