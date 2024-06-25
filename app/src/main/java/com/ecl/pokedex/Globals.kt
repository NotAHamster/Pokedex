package com.ecl.pokedex

import android.content.Context
import androidx.room.Room
import com.ecl.pokedex.data.AppDatabase
import com.ecl.pokedex.io.LocalStorage as IoLocalStorage
import com.ecl.pokedex.io.Network

object Globals {
    var themeID: Int = R.style.Base_Theme_Pokedex
    val network = Network()

    object DatabaseProvider {
        private var INSTANCE: AppDatabase? = null

        fun setDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun getDatabase(): AppDatabase? {
            return INSTANCE
        }
    }

    object LocalStorage {
        private var INSTANCE: IoLocalStorage? = null

        fun set(context: Context): IoLocalStorage {
            return INSTANCE ?: synchronized(this) {
                val instance = IoLocalStorage(DatabaseProvider.setDatabase(context))
                INSTANCE = instance
                instance
            }
        }

        fun get(): IoLocalStorage? {
            return INSTANCE
        }
    }
}
