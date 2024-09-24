package com.hexa.arti.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseActivity
import com.hexa.arti.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val isFirst = true
    lateinit var navController : NavController
    private val mainActivityViewModel : MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        setBottomNavHide()
        // Coroutine 내에서 값을 수집
        lifecycleScope.launch {
            mainActivityViewModel.getJwtToken().collect { token ->
                Log.d(TAG, "onCreate: $token")
            }
        }
    }


    override fun setupBinding(binding: ActivityMainBinding) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnMenu.setupWithNavController(navController)


//        if (isFirst) navController.navigate(R.id.surveyFragment)

        binding.btnArtUpload.setOnClickListener {
            navController.navigate(R.id.artworkUploadFragment)
        }

    }

    fun hideBottomNav(isHide: Boolean) {
        if (isHide) {
            binding.bnMenu.visibility = View.GONE
            binding.btnArtUpload.visibility = View.GONE
        } else {
            binding.bnMenu.visibility = View.VISIBLE
            binding.btnArtUpload.visibility = View.VISIBLE
        }
    }


    // 가로 모드
    fun changeLandScope() {
        // 가로모드로 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // 내비게이션 바 숨기기
                        or View.SYSTEM_UI_FLAG_FULLSCREEN       // 상태바 숨기기
                )
    }

    fun changePortrait() {

        // 기본 모드로 복원
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }

    fun moveLogin(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    /** 바텀 네비게이션 숨기는 기능 */
    private fun setBottomNavHide() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.homeFragment -> {
                    hideBottomNav(false)
                    Log.d(TAG, "setBottomNavHide: home")
                }
                R.id.searchFragment -> {
                    hideBottomNav(false)
                    Log.d(TAG, "setBottomNavHide: search")
                }
                R.id.myGalleryHomeFragment ->{
                    hideBottomNav(false)
                    Log.d(TAG, "setBottomNavHide: myGallery")
                }
                R.id.settingFragment ->{
                    hideBottomNav(false)
                    mainActivityViewModel.setFragmentState(MainActivityViewModel.PORTFOLIO_FRAGMENT)
                    Log.d(TAG, "setBottomNavHide: setting")
                }
                else ->{
                    hideBottomNav(true)
                    Log.d(TAG, "setBottomNavHide: etc")
                }
            }
        }
    }
}