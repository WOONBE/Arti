package com.hexa.arti.ui.profile

import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentPortfolioBinding
import com.hexa.arti.ui.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PortfolioFragment : BaseFragment<FragmentPortfolioBinding>(R.layout.fragment_portfolio) {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun init() {
        initViews()
    }

    private fun initViews() {
        mainActivityViewModel.fragmentState.observe(viewLifecycleOwner) { state ->
            if (state == MainActivityViewModel.PORTFOLIO_FRAGMENT) {
                initChart()
            }
        }
    }

    private fun initChart() {
        val dataList = ArrayList<PieEntry>()

        dataList.add(PieEntry(1f, "팝아트"))
        dataList.add(PieEntry(2f, "인상주의"))
        dataList.add(PieEntry(1f, "르네상스"))
        dataList.add(PieEntry(3f, "입체파"))
        dataList.add(PieEntry(1f, "그 외"))

        val dataSet = PieDataSet(dataList, "")
        with(dataSet) {
            sliceSpace = 3f
            setColors(*ColorTemplate.JOYFUL_COLORS)
        }
        dataSet.setDrawValues(false)

        var data = PieData(dataSet)

        binding.pcChart.data = data
        binding.pcChart.description.isEnabled = false
        binding.pcChart.setTouchEnabled(false)
        binding.pcChart.isRotationEnabled = false
        binding.pcChart.legend.isEnabled = false
        binding.pcChart.isDrawHoleEnabled = true

        binding.pcChart.animateY(500)
        binding.pcChart.invalidate()

    }

    override fun onResume() {
        super.onResume()
        initChart()
    }

}