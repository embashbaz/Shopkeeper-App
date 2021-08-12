package com.example.shopkeeperapp.ui.Performance

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.getMonth
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;


class PerformanceFragment : Fragment() {

    lateinit var barChart: BarChart
    val  cartViewModel: PerformanceViewModel by viewModels{
        PerformanceViewModelFactory((activity?.application as ShopKeeperApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_performance, container, false)

        barChart = view.findViewById<BarChart>(R.id.chart1)

        var soldItems = ArrayList<String>()
        var barEntry = ArrayList<BarEntry>()
        cartViewModel.getMostSoldItems(getMonth())?.observe(viewLifecycleOwner, {
            if (!it.isEmpty()){
                var index = 0
                for (item in it){
                    soldItems.add(item.incomeName)
                    barEntry.add(BarEntry(item.count.toFloat(), index.toFloat()))
                    index++
                }

                val barDataSet = BarDataSet(barEntry, "Most Item Sold")
               // barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK)

                val barData = BarData(barDataSet)
                barChart.setFitBars(true)
                barChart.setData(barData)
                barChart.animateXY(3000, 3000)


            }
        })



        return view
    }

}