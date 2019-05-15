package com.bongdaphui.dao

import android.arch.persistence.room.*
import com.bongdaphui.model.UserModel

@Dao
interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: UserModel)

    @Update
    fun update(item: UserModel)

    @Delete
    fun delete(item: UserModel)

    @Query("SELECT * FROM users")
    fun getItems(): UserModel

    @Query("SELECT * FROM users WHERE id = :id")
    fun getItemById(id: String?): UserModel

    @Query("DELETE FROM users")
    fun deleteTable()


}