package com.wish.bunny.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wish.bunny.onboarding.fragment.OnboardingFirstFragment
import com.wish.bunny.onboarding.fragment.OnboardingSecondFragment
import com.wish.bunny.onboarding.fragment.OnboardingLoginFragment

/**
    작성자: 엄상은
    작업 내용: 온보딩 화면의 뷰페이저를 위한 어댑터
*/
class RegisterPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> OnboardingFirstFragment()
            1 -> OnboardingSecondFragment()
            else -> OnboardingLoginFragment()
        }
    }
    override fun getCount() =  3
}