package com.grewon.dronedin.earnings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.earnings.adapter.EarningsAdapter
import com.grewon.dronedin.earnings.contract.EarningContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.EarningsDataBean
import com.grewon.dronedin.server.MonthlyEarningBean
import com.grewon.dronedin.server.WeeklyDataBean
import com.grewon.dronedin.wallet.MyWalletActivity
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EarningsActivity : BaseActivity(), View.OnClickListener, OnChartValueSelectedListener,
    EarningContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var earningPresenter: EarningContract.Presenter

    private var lineChart: LineChart? = null

    private var offsetCount = 1
    private var isLoaded = false
    private var isFirstTimeDataLoaded = false
    private var earningAdapter: EarningsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)

        setClicks()
        initView()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.earnings)


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        earningPresenter.attachView(this)
        earningPresenter.attachApiInterface(retrofit)

        earningPresenter.getMonthlyEarning()
        apiCall()

        segment_group.setOnPositionChangedListener {
            when (it) {
                0 -> {
                    earningPresenter.getMonthlyEarning()
                }
                1 -> {
                    earningPresenter.getWeeklyEarning()
                }

            }
        }


        nested_scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!isLoaded && isFirstTimeDataLoaded) {
                    isLoaded = true
                    earning_recycle.post(Runnable { earningAdapter?.addLoadingData() })
                    Handler().postDelayed(Runnable {
                        earning_recycle.post(Runnable {
                            apiCall()
                        })
                    }, 200)
                }
            }
        })


    }

    private fun apiCall() {
        earningPresenter.getEarningsData(offsetCount)
    }


    private fun lineChartConfigure() {
        if (lineChart != null && lineChart?.data != null) {
            line_chart_layout.removeView(lineChart)
        }
        lineChart = LineChart(applicationContext)

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.line_image)
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.line_image)
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        lineChart?.layoutParams = layoutParams
        line_chart_layout.addView(lineChart)


        // disable description text

        // disable description text
        lineChart?.description?.isEnabled = false


        // enable touch gestures


        // enable touch gestures
        lineChart?.setTouchEnabled(false)

        // set listeners

        // set listeners
        lineChart?.setOnChartValueSelectedListener(this)
        lineChart?.setDrawGridBackground(false)
        lineChart?.setGridBackgroundColor(ContextCompat.getColor(this, R.color.gray_f4))


        // enable scaling and dragging
        lineChart?.isDragEnabled = true
        lineChart?.setScaleEnabled(true)
        lineChart?.isScaleXEnabled = true
        lineChart?.isScaleYEnabled = true
        lineChart?.extraBottomOffset = 30f
        lineChart?.isAutoScaleMinMaxEnabled = true;
        lineChart?.setPinchZoom(false)


        // disable dual axis (only use LEFT axis)


        // disable dual axis (only use LEFT axis)
        lineChart?.axisRight?.isEnabled = false
        lineChart?.axisLeft?.isEnabled = true



        lineChart?.animateX(1500)

        val l: Legend = lineChart?.legend!!
        l.isEnabled = false
        l.setDrawInside(false)
        l.textSize = 16f
        l.isWordWrapEnabled = false
        l.textColor = ContextCompat.getColor(this, R.color.gray_71)

        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.form = Legend.LegendForm.DEFAULT


    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_withdraw.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_withdraw -> {
                startActivity(Intent(this, MyWalletActivity::class.java))
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }


    override fun onMonthlyDataGetSuccessful(response: MonthlyEarningBean) {
        if (response.data != null) {
            setMonthlyData(response.data)
        }
    }

    private fun setMonthlyData(monthlyDataList: ArrayList<MonthlyEarningBean.Data>) {

        lineChartConfigure()


        val xAxis: XAxis = lineChart?.xAxis!!


        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLinesBehindData(true)
        xAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)
        xAxis.textSize = 10f
        xAxis.setLabelCount(12, true)
        xAxis.granularity = 1f
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -90f
        val monthList = ArrayList<String>()

        for (item in monthlyDataList) {
            monthList.add(item.month.toString())
        }

        val valueFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                try {

                    return monthList[value.toInt()].substring(0, 3).toUpperCase(Locale.ROOT)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ""
            }
        }
        xAxis.valueFormatter = valueFormatter

        val yAxis: YAxis = lineChart?.axisLeft!!
        yAxis.setDrawGridLines(true)
        yAxis.setDrawAxisLine(true)
        yAxis.textSize = 10f
        yAxis.setDrawLabels(true)
        yAxis.setDrawGridLinesBehindData(true)
        yAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)

        val values = ArrayList<Entry>()

        for ((index, item) in monthlyDataList.withIndex()) {
            values.add(
                Entry(
                    index.toFloat(), item.totalAmount?.toFloat()!!
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
            IFillFormatter { dataSet, dataProvider -> lineChart?.axisLeft!!.axisMinimum }
        set1.notifyDataSetChanged()


        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the data sets


        val lineData = LineData(dataSets)
        lineData.setDrawValues(false)
        lineChart?.data = lineData
        lineChart?.setVisibleXRangeMaximum(12f)


        lineChart?.notifyDataSetChanged()
        lineChart?.invalidate()


    }

    override fun onMonthlyDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onWeeklyDataGetSuccessful(response: WeeklyDataBean) {
        if (response.data != null) {
            setWeeklyData(response.data)
        }
    }

    private fun setWeeklyData(weeklyDataList: ArrayList<WeeklyDataBean.Data>) {
        lineChartConfigure()


        val xAxis: XAxis = lineChart?.xAxis!!


        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLinesBehindData(true)
        xAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)
        xAxis.textSize = 12f
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        val monthList = ArrayList<String>()

        for (item in weeklyDataList) {
            monthList.add(item.week.toString())
        }

        val valueFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                try {
                    return monthList[value.toInt()].toUpperCase(Locale.ROOT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ""
            }
        }
        xAxis.valueFormatter = valueFormatter

        val yAxis: YAxis = lineChart?.axisLeft!!
        yAxis.setDrawGridLines(true)
        yAxis.setDrawAxisLine(true)
        yAxis.textSize = 12f
        yAxis.setDrawLabels(true)
        yAxis.setDrawGridLinesBehindData(true)
        yAxis.textColor = ContextCompat.getColor(this, R.color.gray_71)

        val values = ArrayList<Entry>()

        for ((index, item) in weeklyDataList.withIndex()) {
            values.add(
                Entry(
                    index.toFloat(), item.totalAmount?.toFloat()!!
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
            IFillFormatter { dataSet, dataProvider -> lineChart?.axisLeft!!.axisMinimum }
        set1.notifyDataSetChanged()


        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the data sets


        val lineData = LineData(dataSets)
        lineData.setDrawValues(false)
        lineChart?.data = lineData
        lineChart?.setVisibleXRangeMaximum(10f)


        lineChart?.notifyDataSetChanged()
        lineChart?.invalidate()

    }

    override fun onWeeklyDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onEarningsDataGetSuccessful(response: EarningsDataBean) {
        if (offsetCount == 1) {
            txt_available_earnings.text = getString(R.string.price_string, response.totalEarning)
            txt_total_earnings.text = getString(R.string.price_string, response.totalEarning)
        }
        if (response.data != null && response.data.size > 0) {
            isFirstTimeDataLoaded = true
            isLoaded = true
            if (earningAdapter != null) {
                earningAdapter?.removeLoadingData()
            }
            setEarningData(response.data)
            offsetCount += 1

        } else {

            isLoaded = true
            if (earningAdapter != null) {
                earningAdapter?.removeLoadingData()
            }
            if (offsetCount == 1) {
                if (response.msg != null) {
                    setEmptyView(response.msg, R.drawable.ic_no_data)
                }
            }


        }
    }

    override fun onEarningsDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (offsetCount == 1) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
            }
        }
    }

    private fun setEmptyView(msg: String, drawable: Int) {
        no_data_layout.visibility = View.VISIBLE
        earning_recycle.visibility = View.GONE
        txt_no_data_image.setImageResource(drawable)
        txt_no_data_title.text = msg
    }

    private fun setEarningData(earningList: ArrayList<EarningsDataBean.Data>) {
        isLoaded = false

        earning_recycle.layoutManager = LinearLayoutManager(this)
        if (offsetCount == 1) {
            earningAdapter = EarningsAdapter(this)
            earning_recycle.adapter = earningAdapter
        }
        earningAdapter?.addItemsList(earningList)
        no_data_layout.visibility = View.GONE
        earning_recycle.visibility = View.VISIBLE
    }

    override fun onApiException(error: Int) {
        setEmptyView(getString(error), R.drawable.ic_connectivity)
    }


}