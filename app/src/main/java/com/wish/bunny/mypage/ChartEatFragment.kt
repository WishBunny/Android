package com.wish.bunny.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.wish.bunny.databinding.FragmentChartEatBinding

class ChartEatFragment(private val percentDo:Float, private val percentEat:Float, private val percentGet:Float) : Fragment() {
    private var _binding: FragmentChartEatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartEatBinding.inflate(inflater, container, false)
        setBarChartEat("먹고 싶어요", percentEat)

        return _binding?.root
    }

    private fun setBarChartEat(message: String, achieve: Float) {
        _binding?.pieChartEat?.setUsePercentValues(true)

        _binding?.let { binding ->
            val entries = ArrayList<PieEntry>()

            entries.add(PieEntry(100-achieve, "🥕"))
            entries.add(PieEntry(achieve, "달성 위시🐰"))

            val colorsItems = ArrayList<Int>()
            colorsItems.add(Color.parseColor("#DADADA"))
            colorsItems.add(Color.parseColor("#FFE1E3"))

            val pieDataSet = PieDataSet(entries, "")
            pieDataSet.apply {
                colors = colorsItems
                valueTextColor = Color.BLACK
                valueTextSize = 10f
            }

            val pieData = PieData(pieDataSet)
            _binding?.pieChartEat?.apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = false
                centerText = message
                setEntryLabelColor(Color.BLACK)
                setCenterTextSize(15f)

                legend.apply {
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    orientation = Legend.LegendOrientation.VERTICAL
                    setDrawInside(false)
                    isEnabled = true
                }

                animateY(1400, Easing.EaseInOutQuad)
                animate()
            }
        }
    }

}