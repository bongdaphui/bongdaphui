package com.bongdaphui.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bongdaphui.model.SchedulePlayerModel

@Dao
interface SchedulePlayerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: SchedulePlayerModel)

    @Query("SELECT * FROM schedulePlayer")
    fun getItems(): List<SchedulePlayerModel>

    @Query("DELETE FROM schedulePlayer")
    fun deleteTable()

}