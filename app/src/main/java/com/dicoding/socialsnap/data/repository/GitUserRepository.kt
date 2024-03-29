package com.dicoding.socialsnap.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.socialsnap.data.database.FavoriteRoomDatabase
import com.dicoding.socialsnap.data.database.GitUserDao
import com.dicoding.socialsnap.data.database.UserFavorite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GitUserRepository(application: Application) {
    private val gitDao: GitUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        gitDao = db.gitUserDao()
    }

    fun getAllFavorite(): LiveData<List<UserFavorite>> = gitDao.getAllFavorite()

    fun insert(userFavorite: UserFavorite) {
        executorService.execute { gitDao.insertFavUser(userFavorite) }
    }

    fun delete(username: String) {
        executorService.execute { gitDao.deleteFavUser(username) }
    }

    fun getAllFavByUsername(username: String)= gitDao.getFavoriteUserByUsername(username)
}