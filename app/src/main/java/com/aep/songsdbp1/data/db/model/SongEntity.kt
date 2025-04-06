package com.aep.songsdbp1.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aep.songsdbp1.utils.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    var id: Long = 0,
    @ColumnInfo(name = "song_title")
    var title: String,
    @ColumnInfo(name = "song_artist", defaultValue = "unknown")
    var artist: String,
    @ColumnInfo(name = "genre")
    var genre: String,
)
