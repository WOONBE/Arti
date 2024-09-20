package com.hexa.arti.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentPortfolioBinding
import com.hexa.arti.ui.MainActivity

class PortfolioFragment : BaseFragment<FragmentPortfolioBinding>(R.layout.fragment_portfolio) {


    override fun init() {
        initChart()
    }
    private fun initChart(){
        val dataList = ArrayList<PieEntry>()

        dataList.add(PieEntry(3f, "팝아트"))
        dataList.add(PieEntry(2f, "인상주의"))
        dataList.add(PieEntry(1f, "르네상스"))
        dataList.add(PieEntry(1f, "입체파"))
        dataList.add(PieEntry(1f, "그 외"))

        val dataSet = PieDataSet(dataList, "")
        with(dataSet) {
            sliceSpace = 3f
            setColors(*ColorTemplate.COLORFUL_COLORS)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}