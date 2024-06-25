package com.ecl.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ecl.pokedex.data.ECL_Move

@Dao
interface ECL_Move_Dao {
    @Insert
    fun insert(move: ECL_Move)

    @Query("SELECT * FROM moves WHERE id = :id")
    fun getMoveById(id: Int): List<ECL_Move>

    @Query("SELECT id From moves")
    fun getMoveIds(): List<Int>
}