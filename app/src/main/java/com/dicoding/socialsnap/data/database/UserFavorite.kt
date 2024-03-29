package com.dicoding.socialsnap.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity (tableName = "FavoriteUser")
@Parcelize
data class UserFavorite(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = "",
) : Parcelable
