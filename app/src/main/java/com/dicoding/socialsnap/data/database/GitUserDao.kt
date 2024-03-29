package com.dicoding.socialsnap.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GitUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavUser(fav: UserFavorite)

    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    fun deleteFavUser(username: String)

    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getAllFavorite(): LiveData<List<UserFavorite>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<UserFavorite>
}