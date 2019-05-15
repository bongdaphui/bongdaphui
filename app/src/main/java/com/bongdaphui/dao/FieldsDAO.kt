package com.bongdaphui.dao

import android.arch.persistence.room.*
import com.bongdaphui.model.FbFieldModel

@Dao
interface FieldsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FbFieldModel)

    @Update
    fun update(item: FbFieldModel)

    @Delete
    fun delete(item: FbFieldModel)

    @Query("SELECT * FROM fields")
    fun getItems(): List<FbFieldModel>

    @Query("DELETE FROM fields")
    fun deleteTable()
//
//    @Query("SELECT * FROM fields WHERE idCity = :id")
//    fun getItemById(id: String?): FbFieldModel

}