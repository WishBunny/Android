package com.wish.bunny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.wish.bunny.databinding.ActivityMainBinding
import com.wish.bunny.friend.FriendFragment
import com.wish.bunny.home.HomeFragment
import com.wish.bunny.mypage.MypageFragment
import com.wish.bunny.wish.WishInsertFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 뷰 바인딩
        setContentView(binding.root)
        var keyhash = Utility.getKeyHash(this)

        /**
        작성자:  이혜연
        처리 내용: 하단 네비게이션 전환
         */
        binding.bottomNavi.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navi_home -> {
                        val homeFragment = HomeFragment()
                        val bundle = Bundle()
                        bundle.putString("isMine", "1")
                        homeFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, homeFragment).commit()
                    }

                    R.id.navi_friend -> {
                        val boardFragment = FriendFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, boardFragment).commit()
                    }

                    R.id.navi_wish -> {
                        val wishInsertFragment = WishInsertFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, wishInsertFragment).commit()
                    }

                    R.id.navi_mypage -> {
                        val settingFragment = MypageFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, settingFragment).commit()
                    }
                }

                true
            }
            selectedItemId = R.id.navi_home
        }
    }
}