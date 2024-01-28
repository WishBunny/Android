package com.wish.bunny.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wish.bunny.databinding.ActivityOnboardingBinding

/**
    작성자: 엄상은
    처리 내용: 온보딩 액티비티
*/
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