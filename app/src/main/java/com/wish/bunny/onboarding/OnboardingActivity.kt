package com.wish.bunny.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wish.bunny.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerViewpager.adapter = RegisterPagerAdapter(supportFragmentManager)
        binding.registerViewpager.offscreenPageLimit = 2
        binding.dotsIndicator.setViewPager(binding.registerViewpager)
    }
}