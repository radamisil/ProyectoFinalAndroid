package com.visight.adondevamos.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.visight.adondevamos.data.entity.FavouritePlace
import retrofit2.http.DELETE

@Dao
interface FavouritePlaceDao {

    @Query("SELECT * from FavouritePlace")
    fun getAllFavourites(): List<FavouritePlace>

    @Insert
    fun addFavourite(favouritePlace: FavouritePlace)

    @Delete
    fun removeFavourite(favouritePlace: FavouritePlace)
}