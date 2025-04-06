package com.aep.songsdbp1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aep.songsdbp1.data.db.model.SongEntity
import com.aep.songsdbp1.utils.Constants

@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SongDatabase : RoomDatabase() {
    //Aquí va el DAO
    abstract fun songDao(): SongDao

    companion object {
        // Singleton: previene que múltiples instancias se abran al mismo tiempo
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            // Si la instancia no es null, regrésala
            // Si lo es, crea la base de datos
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()
                INSTANCE = instance
                // regresar instancia
                instance
            }
        }
    }
}