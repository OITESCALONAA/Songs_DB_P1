package com.aep.songsdbp1.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.utils.Constants

@Dao
interface SongDao {
    //Create
    @Insert
    suspend fun insertSong(song: SongEntity)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_GAME_TABLE}")
    suspend fun getAllSongs(): MutableList<SongEntity>

    //Update
    @Update
    suspend fun updateSong(song: SongEntity)

    //Delete
    @Delete
    suspend fun deleteSong(song: SongEntity)
}