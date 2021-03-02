package com.grewon.dronedin.earnings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.earnings.adapter.EarningsAdapter
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import java.util.*

class EarningsActivity : BaseActivity(), View.OnClickListener, OnChartValueSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
        setClicks()
        initView()
    }

    private fun initView() {
        lineChartConfigure()
        setEarningData()
    }

    private fun setEarningData() {
        earning_recycle.layoutManager = LinearLayoutManager(this)
        val earningAdapter = EarningsAdapter(this)
        earning_recycle.adapter = earningAdapter
    }

    private fun lineChartConfigure() {
        // disable description text

        // disable description text
        line_chart.description.isEnabled = false


        // enable touch gestures


        // enable touch gestures
        line_chart.setTouchEnabled(true)

        // set listeners

        // set listeners
        line_chart.setOnChartValueSelectedListener(this)
        line_chart.setDrawGridBackground(false)
        line_chart.setGridBackgroundColor(ContextCompat.getColor(this, R.color.gray_f4))


        // enable scaling and dragging
        line_chart.isDragEnabled = true
        line_chart.setScaleEnabled(true)
        line_chart.isScaleXEnabled = true
        line_chart.isScaleYEnabled = true
        line_chart.extraBottomOffset = 30f
        //line_chart.setAutoScaleMinMaxEnabled(true);
        // force pinch zoom along both axis
        //line_chart.setAutoScaleMinMaxEnabled(true);
        // force pinch zoom along both axis
        line_chart.setPinchZoom(true)


        // disable dual axis (only use LEFT axis)


        // disable dual axis (only use LEFT axis)
        line_chart.axisRight.isEnabled = false
        line_chart.axisLeft.isEnabled = true



        line_chart.animateX(1500)

        val l: Legend = line_chart.legend
        l.isEnabled = false
        l.setDrawInside(false)
        l.textSize = 16f
        l.isWordWrapEnabled = false
        l.textColor = ContextCompat.getColor(this, R.color.gray_71)

        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.form = Legend.LegendForm.DEFAULT
        setLineChartData()
    }

    private fun setLineChartData() {

        val xAxis: XAxis = line_chart.xAxis


        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLinesBehindData(true)
        xAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)
        xAxis.textSize = 12f
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.granularity = 1f


        val yAxis: YAxis = line_chart.axisLeft
        yAxis.setDrawGridLines(true)
        yAxis.setDrawAxisLine(true)
        yAxis.textSize = 12f
        yAxis.setDrawLabels(true)
        yAxis.setDrawGridLinesBehindData(true)
        yAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)

        val values = ArrayList<Entry>()


        for (i in 1..10) {
            values.add(
                Entry(
                    i.toFloat(), (1..100).random().toFloat()
                )
            )
        }


        val set1 = LineDataSet(values, "")
        set1.color = ContextCompat.getColor(this, R.color.chart_dot_color)
        set1.setCircleColor(ContextCompat.getColor(this, R.color.chart_dot_color))
        set1.lineWidth = 3f
        set1.circleRadius = 5f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 12f
        set1.setDrawFilled(true)

        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> line_chart.axisLeft.axisMinimum }
        set1.notifyDataSetChanged()


        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the data sets


        val data = LineData(dataSets)
        data.setDrawValues(false)
        line_chart.data = data
        line_chart.setVisibleXRangeMaximum(10f)


        line_chart.notifyDataSetChanged()
        line_chart.invalidate()

    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {
    }
}