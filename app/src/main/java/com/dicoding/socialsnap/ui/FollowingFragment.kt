package com.dicoding.socialsnap.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.viewModels
import com.dicoding.socialsnap.adapter.SocialAdapter
import com.dicoding.socialsnap.data.response.ItemsItem
import com.dicoding.socialsnap.databinding.FragmentFollowingBinding
import com.dicoding.socialsnap.ui.FollowersFragment.Companion.USERNAME
import com.dicoding.socialsnap.viewmodel.DetailUserViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val followingModel by viewModels<DetailUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        followingModel.following.observe(viewLifecycleOwner) { followingData ->
            if (followingData == null) {
                val listUsers = arguments?.getString(USERNAME) ?: ""
                followingModel.getFollowing(listUsers)
            } else {
                showFollowing(followingData)
            }
        }
        followingModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showFollowing(dataUsers: List<ItemsItem>) {
        binding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SocialAdapter(dataUsers).apply {
                setOnItemClickCallback(object : SocialAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ItemsItem) {
                        navigateToDetail(data.login)
                    }
                })
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToDetail(username: String) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}