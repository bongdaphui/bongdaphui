package com.bongdaphui.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bongdaphui.model.ScheduleClubModel

@Dao
interface ScheduleClubDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ScheduleClubModel)

    @Query("SELECT * FROM scheduleClub")
    fun getItems(): List<ScheduleClubModel>

    @Query("DELETE FROM scheduleClub")
    fun deleteTable()

}