package com.dicoding.socialsnap.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserFavorite::class], version = 1)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun gitUserDao(): GitUserDao

    companion object{
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDatabase{
            if (INSTANCE == null){
                synchronized(FavoriteRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,FavoriteRoomDatabase::class.java, "favorit_database").build()
                }
            }
            return INSTANCE as FavoriteRoomDatabase
        }
    }
}