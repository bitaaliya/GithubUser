package com.dicoding.socialsnap.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.socialsnap.ui.FollowersFragment
import com.dicoding.socialsnap.ui.FollowersFragment.Companion.USERNAME
import com.dicoding.socialsnap.ui.FollowingFragment

class SectionAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putString(USERNAME, username)
                    }
                }
            }
            else -> {
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString(USERNAME, username)
                    }
                }
            }
        }
    }
}