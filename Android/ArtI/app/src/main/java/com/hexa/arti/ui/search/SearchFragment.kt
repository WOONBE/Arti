package com.hexa.arti.ui.search

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentSearchBinding
import com.hexa.arti.ui.MainActivity
import kotlinx.coroutines.launch


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var mainActivity: MainActivity
    private var isSearchDetail = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)

    }

    override fun init() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isSearchDetail) {
                        offSearchFocus()
                        isSearchDetail = false
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })

        binding.tietSearch.setOnFocusChangeListener { _, hasFocus ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (hasFocus) {
                    onSearchFocus()
                }
            }
        }

        binding.tvCancel.setOnClickListener {
            offSearchFocus()
        }

        binding.ivClearText.setOnClickListener {
            binding.tietSearch.setText("")
        }

        binding.tietSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) binding.ivClearText.visibility = View.GONE
            else binding.ivClearText.visibility = View.VISIBLE
        }
    }

    private fun onSearchFocus() {

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.tietSearch, InputMethodManager.SHOW_IMPLICIT)

        isSearchDetail = true

        binding.tvSearchTitle.visibility = View.GONE
        binding.tvCancel.visibility = View.VISIBLE
        binding.tilSearch.clearFocus()

        binding.clSearchBanner.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.clSearchBanner.visibility = View.GONE
            }

        binding.clSearchBottom.alpha = 0f
        binding.clSearchBottom.visibility = View.VISIBLE
        binding.clSearchBottom.animate()
            .alpha(1f)
            .setDuration(150)
    }

    private fun offSearchFocus() {

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        binding.tvSearchTitle.visibility = View.VISIBLE
        binding.tvCancel.visibility = View.GONE
        binding.tietSearch.setText("")

        binding.clSearchBottom.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.clSearchBottom.visibility = View.GONE
            }

        binding.clSearchBanner.alpha = 0f
        binding.clSearchBanner.visibility = View.VISIBLE
        binding.clSearchBanner.animate()
            .alpha(1f)
            .setDuration(150)
    }


}