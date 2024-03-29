package com.dicoding.socialsnap.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.socialsnap.adapter.SocialAdapter
import com.dicoding.socialsnap.data.database.UserFavorite
import com.dicoding.socialsnap.data.response.ItemsItem
import com.dicoding.socialsnap.databinding.ActivityFavoriteBinding
import com.dicoding.socialsnap.factory.ViewModelFactory
import com.dicoding.socialsnap.viewmodel.FavoriteViewModel

@Suppress("DEPRECATION")
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val list = ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        observeFavoriteData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "Favorite User"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel() {
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
    }

    private fun observeFavoriteData() {
        favoriteViewModel.getAllFav().observe(this) { userFav ->
            userFav?.let {
                binding.rvFav.visibility = View.VISIBLE
                setDataFavorite(it)
            }
        }
    }

    private fun setDataFavorite(userFav: List<UserFavorite>) {
        list.clear()
        userFav.forEach { data ->
            val mFollow = ItemsItem(
                data.username,
                data.avatarUrl ?: ""
            )
            list.add(mFollow)
        }
        showRecycleList()
    }

    private fun showRecycleList() {
        binding.rvFav.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            val socialAdapter = SocialAdapter(list)
            socialAdapter.setOnItemClickCallback(object : SocialAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    navigateToDetail(data.login)
                }
            })
            adapter = socialAdapter
            binding.pbFav.visibility = View.GONE
        }
    }

    private fun navigateToDetail(username: String) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, username)
        startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val pref = SettingPreference(this)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}