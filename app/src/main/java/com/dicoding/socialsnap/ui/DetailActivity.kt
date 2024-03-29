package com.dicoding.socialsnap.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.socialsnap.R
import com.dicoding.socialsnap.adapter.SectionAdapter
import com.dicoding.socialsnap.data.database.UserFavorite
import com.dicoding.socialsnap.data.response.DetailSocialResponse
import com.dicoding.socialsnap.databinding.ActivityDetailBinding
import com.dicoding.socialsnap.factory.ViewModelFactory
import com.dicoding.socialsnap.viewmodel.DetailUserViewModel
import com.dicoding.socialsnap.viewmodel.FavoriteViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_USER = "extra_user"

        val TAB_TITLES = intArrayOf(
            R.string.tabs_followers,
            R.string.tabs_following
        )
    }

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailUserViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var favUser: UserFavorite? = null
    private var checkFav: Boolean = false
    private val username: String? by lazy {
        intent.getStringExtra(EXTRA_USERNAME) ?: intent.getStringExtra(EXTRA_USER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupViewModel()
    }

    private fun setupViews() {
        binding.tvName.text = username

        val sectionsPagerAdapter = SectionAdapter(this, username.toString())
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail User"
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
        favoriteViewModel = obtainViewModel(this@DetailActivity)

        viewModel.isFavorite.observe(this) { isFavorite ->
            checkFav = isFavorite
        }

        viewModel.isLoading.observe(this) { loader ->
            showLoading(loader)
        }

        viewModel.detailUser.observe(this) { detailUserData ->
            getUserData(detailUserData)
        }

        viewModel.getDetailUser(username.toString())
    }


    private fun getUserData(userItems: DetailSocialResponse) {
        Glide.with(this@DetailActivity)
            .load(userItems.avatarUrl)
            .into(binding.circleImageView)
        binding.apply {
            tvUsername.text = userItems.login
            tvName.text = userItems.name
            tvFollowers.text = userItems.followers.toString()
            tvFollowing.text = userItems.following.toString()
            circleImageView.loadImage(userItems.avatarUrl)
        }
        favUser = UserFavorite(userItems.login, userItems.avatarUrl)
        CoroutineScope(Dispatchers.IO).launch {
            val username = intent.getStringExtra(EXTRA_USER).toString()
            val mcheckFav = favoriteViewModel.getAllFavoriteByUsername(username)
            withContext(Dispatchers.Main) {
                if (mcheckFav.value != null) {
                    checkFav = true
                    binding.fabFavorite.isSelected = true
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_solid)
                } else {
                    checkFav = false
                    binding.fabFavorite.isSelected = false
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_line)
                }
            }
        }
        binding.fabFavorite.setOnClickListener {
            if (!checkFav) {
                checkFav= true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_solid)
                binding.fabFavorite.isSelected = true
                favoriteViewModel.insert(favUser as UserFavorite)
                Toast.makeText(this, "${userItems.login} Added to favorite", Toast.LENGTH_SHORT).show()

            }else {
                checkFav = false
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_line)
                binding.fabFavorite.isSelected = false
                favoriteViewModel.delete(userItems.login)
                Toast.makeText(this, "${userItems.login} Removed from favorite", Toast.LENGTH_SHORT).show()
            }
            invalidateOptionsMenu()
        }
    }

    private fun ImageView.loadImage(avatarUrl: String) {
        Glide.with(this.context)
            .load(avatarUrl)
            .apply(RequestOptions().override(200, 200))
            .centerCrop()
            .circleCrop()
            .into(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val pref = SettingPreference(this)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.fav_main -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.darkmode -> {
                val intent = Intent(this, DarkModeActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_share -> {
                val shareText = "GitHub User: ${binding.tvUsername.text}\nFollowers: ${binding.tvFollowers.text}\nFollowing: ${binding.tvFollowing.text}"
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}