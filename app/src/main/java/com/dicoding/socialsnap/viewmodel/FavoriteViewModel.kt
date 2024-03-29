package com.dicoding.socialsnap.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.socialsnap.data.database.UserFavorite
import com.dicoding.socialsnap.data.repository.GitUserRepository

class FavoriteViewModel(application: Application): ViewModel() {

    private val gitRepository: GitUserRepository = GitUserRepository(application)

    fun insert(favoriteUser: UserFavorite) {
        gitRepository.insert(favoriteUser)
    }

    fun delete(user: String) {
        gitRepository.delete(user)
    }

    fun getAllFav(): LiveData<List<UserFavorite>> = gitRepository.getAllFavorite()

    fun getAllFavoriteByUsername(username: String): LiveData<UserFavorite> = gitRepository.getAllFavByUsername(username)
}
