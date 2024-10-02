package com.hexa.arti.ui.survey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentSurveyBinding
import com.hexa.arti.ui.LoginActivity
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SurveyFragment : BaseFragment<FragmentSurveyBinding>(R.layout.fragment_survey){

    private var status = 0
    override fun init() {
    }
    private var click = true
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.surveyGridLayout.setOnClickListener{
            if (!click) return@setOnClickListener

            click = false
            status += 20

            CoroutineScope(Dispatchers.Main).launch {
                for (i in status-20..status) {
                    binding.progressBar.progress = i
                    delay(20L) // 속도를 조절하려면 이 값을 변경하세요.

                    binding.surveyProgressTv.text = "$i%"
                }
                click = true
                if(status >= 99) navigate(R.id.action_surveyFragment_to_createGalleryFragment)
            }

                CoroutineScope(Dispatchers.Main).launch {
                    fadeOutAndIn(binding.surveyGridLayout)
                }

        }

        // 뒤로가기 이벤트
        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            status -= 20
            if(status < 0) {
                mainActivity.startActivity(Intent(requireContext(),LoginActivity::class.java))
                mainActivity.finish()
            }
            else{
                binding.progressBar.progress = status
                binding.surveyProgressTv.text = "$status%"
            }

        }
    }

    // fade_in, fade_out 애니메이션
    private fun fadeOutAndIn(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(250)
            .withEndAction {
                view.animate()
                    .alpha(1f)
                    .setDuration(250)
                    .start()
            }
            .start()
    }

}