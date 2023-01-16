package com.example.chatapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapp.ui.view.ChatFragment
import com.example.chatapp.ui.view.HomeFragment
import com.example.chatapp.ui.view.SettingsFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ChatFragment()
            else -> SettingsFragment()
        }
    }
}