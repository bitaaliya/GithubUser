package com.dicoding.socialsnap.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.socialsnap.adapter.SocialAdapter
import com.dicoding.socialsnap.data.response.ItemsItem
import com.dicoding.socialsnap.databinding.FragmentFollowersBinding
import com.dicoding.socialsnap.viewmodel.DetailUserViewModel

class FollowersFragment : Fragment() {

    companion object {
        const val USERNAME = "username"
    }

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val followersModel by viewModels<DetailUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        followersModel.followers.observe(viewLifecycleOwner) { followersData ->
            if (followersData == null) {
                val listUsers = arguments?.getString(USERNAME) ?: ""
                followersModel.getFollowers(listUsers)
            } else {
                showFollowers(followersData)
            }
        }
        followersModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showFollowers(dataUsers: List<ItemsItem>) {
        binding.rvFollowers.apply {
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
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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