package com.hexa.arti.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hexa.arti.R
import com.hexa.arti.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnMenu.setupWithNavController(navController)

        binding.btnArtUpload.setOnClickListener {
            navController.navigate(R.id.artworkUploadFragment)
        }

        // BottomNavigationView의 '홈' 버튼 선택 시 스택 제거 로직 추가
        binding.bnMenu.setOnItemSelectedListener { item ->
            val navOptions = NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, false)  // 스택에서 HomeFragment 이전 프래그먼트 모두 제거
                .build()
            when (item.itemId) {

                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment,null, navOptions)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment,null, navOptions)
                    true
                }
                R.id.subscribeFragment -> {
                    navController.navigate(R.id.subscribeFragment,null, navOptions)
                    true
                }
                R.id.portfolioFragment -> {
                    navController.navigate(R.id.portfolioFragment,null, navOptions)
                    true
                }
                else -> false
            }
        }
    }
}