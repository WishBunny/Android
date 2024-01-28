package com.wish.bunny.mypage

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.wish.bunny.databinding.FragmentChartBinding

class ChartFragment(private val percentDo:Float,private val percentEat:Float,private val percentGet:Float) : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        setBarChartDo("하고 싶어요",percentDo)
        setBarChartEat("먹고 싶어요",percentEat)
        setBarChartGet("갖고 싶어요",percentGet)

        return _binding?.root
    }

    private fun setBarChartDo(message: String, achieve: Float) {
        _binding?.pieChartDo?.setUsePercentValues(true)

        _binding?.let { binding ->
            // data set
            val entries = ArrayList<PieEntry>()

            entries.add(PieEntry(100-achieve, "남은 위시"))
            entries.add(PieEntry(achieve, "이룬 위시"))

            // add a lot of colors
            val colorsItems = ArrayList<Int>()
            for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
            for (c in COLORFUL_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
            colorsItems.add(ColorTemplate.getHoloBlue())

            val pieDataSet = PieDataSet(entries, "")
            pieDataSet.apply {
                colors = colorsItems
                valueTextColor = Color.BLACK
                valueTextSize = 10f
            }

            val pieData = PieData(pieDataSet)
            _binding?.pieChartDo?.apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = false
                centerText = message
                setEntryLabelColor(Color.BLACK)
                setCenterTextSize(15f)
                animateY(1400, Easing.EaseInOutQuad)
                animate()
            }

        }
    }

    private fun setBarChartEat(message: String, achieve: Float) {
        _binding?.pieChartDo?.setUsePercentValues(true)

        _binding?.let { binding ->
            // data set
            val entries = ArrayList<PieEntry>()

            entries.add(PieEntry(100-achieve, "남은 위시"))
            entries.add(PieEntry(achieve, "이룬 위시"))

            val colorsItems = ArrayList<Int>()
            for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
            for (c in COLORFUL_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
            colorsItems.add(ColorTemplate.getHoloBlue())

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
                animateY(1400, Easing.EaseInOutQuad)
                animate()
            }
        }
    }

    private fun setBarChartGet(message: String, achieve: Float) {
        _binding?.pieChartGet?.setUsePercentValues(true)

        _binding?.let { binding ->
            // data set
            val entries = ArrayList<PieEntry>()

            entries.add(PieEntry(100-achieve, "남은 위시"))
            entries.add(PieEntry(achieve, "이룬 위시"))

            // add a lot of colors
            val colorsItems = ArrayList<Int>()
            for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
            for (c in COLORFUL_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
            for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
            colorsItems.add(ColorTemplate.getHoloBlue())

            val pieDataSet = PieDataSet(entries, "")
            pieDataSet.apply {
                colors = colorsItems
                valueTextColor = Color.BLACK
                valueTextSize = 10f
            }

            val pieData = PieData(pieDataSet)
            _binding?.pieChartGet?.apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = false
                centerText = message
                setEntryLabelColor(Color.BLACK)
                setCenterTextSize(14f)
                animateY(1400, Easing.EaseInOutQuad)
                animate()
            }
        }
    }


}
