package com.wish.bunny

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wish.bunny.databinding.ActivityMainBinding
import com.wish.bunny.friend.FriendFragment
import com.wish.bunny.home.HomeFragment
import com.wish.bunny.mypage.MypageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 뷰 바인딩
        setContentView(binding.root)

        binding.bottomNavi.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navi_home -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
                }
                R.id.navi_friend -> {
                    val boardFragment = FriendFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, boardFragment).commit()
                }
                R.id.navi_mypage -> {
                    val settingFragment = MypageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, settingFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.navi_home
        }
    }



}