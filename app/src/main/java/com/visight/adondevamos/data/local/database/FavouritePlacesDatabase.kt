package com.visight.adondevamos.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.visight.adondevamos.data.entity.FavouritePlace
import com.visight.adondevamos.utils.AppConstants

@Database(entities = arrayOf(FavouritePlace::class), version = 1)
abstract class FavouritePlacesDatabase : RoomDatabase() {

    abstract fun favouritePlaceDao(): FavouritePlaceDao

    companion object {
        private var INSTANCE: FavouritePlacesDatabase? = null

        fun getInstance(context: Context): FavouritePlacesDatabase? {
            if (INSTANCE == null) {
                synchronized(FavouritePlacesDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FavouritePlacesDatabase::class.java, AppConstants.DATABASE_NAME)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}