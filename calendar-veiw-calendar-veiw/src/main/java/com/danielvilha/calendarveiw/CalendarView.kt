package com.danielvilha.calendarveiw

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.danielvilha.calendarveiw.listeners.DateSelectListener
import com.danielvilha.calendarveiw.listeners.DialogCompleteListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by danielvilha on 25/06/20
 * https://github.com/danielvilha
 */
class CalendarView : FrameLayout, DateSelectListener {
    private var calendarData: CalendarData? = null
    private var callback: CalendarDialog.Callback? = null
    private var completeListener: DialogCompleteListener? = null
    private var attrs: AttributeSet? = null
    private var defStyle = 0

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)
    constructor(context: Context) :
            this(context, null)

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        View.inflate(context, R.layout.calendar_frame, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyle, 0)
        if (calendarData?.backgroundColor == null) {
            calendarData?.backgroundColor = typedArray.getColor(
                R.styleable.CalendarView_backgroundColor,
                ContextCompat.getColor(context, R.color.slycalendar_defBackgroundColor)
            )
        }
        if (calendarData?.headerColor == null) {
            calendarData?.headerColor = typedArray.getColor(
                R.styleable.CalendarView_headerColor,
                ContextCompat.getColor(context, R.color.slycalendar_defHeaderColor)
            )
        }
        if (calendarData?.headerTextColor == null) {
            calendarData?.headerTextColor = typedArray.getColor(
                R.styleable.CalendarView_headerTextColor,
                ContextCompat.getColor(context, R.color.slycalendar_defHeaderTextColor)
            )
        }
        if (calendarData?.textColor == null) {
            calendarData?.textColor = typedArray.getColor(
                R.styleable.CalendarView_textColor,
                ContextCompat.getColor(context, R.color.slycalendar_defTextColor)
            )
        }
        if (calendarData?.selectedColor == null) {
            calendarData?.selectedColor = typedArray.getColor(
                R.styleable.CalendarView_selectedColor,
                ContextCompat.getColor(context, R.color.slycalendar_defSelectedColor)
            )
        }
        if (calendarData?.selectedTextColor == null) {
            calendarData?.selectedTextColor = typedArray.getColor(
                R.styleable.CalendarView_selectedTextColor,
                ContextCompat.getColor(context, R.color.slycalendar_defSelectedTextColor)
            )
        }
        typedArray.recycle()
        val viewPager: ViewPager = findViewById(R.id.content)
        viewPager.adapter = MonthPagerAdapter(calendarData, this)
        viewPager.currentItem = viewPager.adapter!!.count / 2
        showCalendar()
    }

    fun setCallback(callback: CalendarDialog.Callback?) {
        this.callback = callback
    }

    fun setCompleteListener(completeListener: DialogCompleteListener?) {
        this.completeListener = completeListener
    }

    fun setSlyCalendarData(calendarData: CalendarData?) {
        this.calendarData = calendarData
        init(attrs, defStyle)
        showCalendar()
    }

    private fun showCalendar() {
        paintCalendar()
        showTime()
        findViewById<View>(R.id.txtCancel).setOnClickListener {
            callback?.onCancelled()
            completeListener?.complete()
        }
        findViewById<View>(R.id.txtSave).setOnClickListener {
            if (callback != null) {
                var start: Calendar? = null
                var end: Calendar? = null
                if (calendarData?.selectedStartDate != null) {
                    start = Calendar.getInstance()
                    start.time = calendarData?.selectedStartDate
                }
                if (calendarData?.selectedEndDate != null) {
                    end = Calendar.getInstance()
                    end.time = calendarData?.selectedEndDate
                }
                callback!!.onDataSelected(start, end, calendarData!!.selectedHour, calendarData!!.selectedMinutes)
            }
            completeListener?.complete()
        }
        val calendarStart = Calendar.getInstance()
        var calendarEnd: Calendar? = null
        if (calendarData?.selectedStartDate != null) {
            calendarStart.time = calendarData?.selectedStartDate
        } else {
            calendarStart.time = calendarData?.showDate
        }
        if (calendarData?.selectedEndDate != null) {
            calendarEnd = Calendar.getInstance()
            calendarEnd.time = calendarData?.selectedEndDate
        }
        (findViewById<View>(R.id.txtYear) as TextView).text = calendarStart[Calendar.YEAR].toString()
        if (calendarEnd == null) {
            (findViewById<View>(R.id.txtSelectedPeriod) as TextView).text = SimpleDateFormat("EE, dd MMMM", Locale.getDefault()).format(calendarStart.time)
        } else {
            if (calendarStart[Calendar.MONTH] == calendarEnd[Calendar.MONTH]) {
                (findViewById<View>(R.id.txtSelectedPeriod) as TextView).text = context.getString(R.string.slycalendar_dates_period, SimpleDateFormat("EE, dd", Locale.getDefault()).format(calendarStart.time), SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.time))
            } else {
                (findViewById<View>(R.id.txtSelectedPeriod) as TextView).text = context.getString(R.string.slycalendar_dates_period, SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarStart.time), SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.time))
            }
        }
        findViewById<View>(R.id.btnMonthPrev).setOnClickListener {
            val vpager: ViewPager = findViewById(R.id.content)
            vpager.currentItem = vpager.currentItem - 1
        }
        findViewById<View>(R.id.btnMonthNext).setOnClickListener {
            val vpager: ViewPager = findViewById(R.id.content)
            vpager.currentItem = vpager.currentItem + 1
        }
        findViewById<View>(R.id.txtTime).setOnClickListener {
            var style = R.style.SlyCalendarTimeDialogTheme
            if (calendarData?.timeTheme != null) {
                style = calendarData?.timeTheme!!
            }
            val tpd = TimePickerDialog(context, style, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendarData?.selectedHour = hourOfDay
                calendarData?.selectedMinutes = minute
                showTime()
            }, calendarData?.selectedHour!!, calendarData?.selectedMinutes!!, true)
            tpd.show()
        }
        findViewById<TextView>(R.id.txtTime).visibility = if (calendarData!!.showTime) View.VISIBLE else View.GONE
        val viewPager: ViewPager = findViewById(R.id.content)
        viewPager.adapter!!.notifyDataSetChanged()
        viewPager.invalidate()
    }

    private fun paintCalendar() {
        findViewById<View>(R.id.mainFrame).setBackgroundColor(calendarData?.backgroundColor!!)
        findViewById<View>(R.id.headerView).setBackgroundColor(calendarData?.headerColor!!)
        (findViewById<View>(R.id.txtYear) as TextView).setTextColor(calendarData?.headerTextColor!!)
        (findViewById<View>(R.id.txtSelectedPeriod) as TextView).setTextColor(calendarData?.headerTextColor!!)
        (findViewById<View>(R.id.txtTime) as TextView).setTextColor(calendarData?.headerColor!!)
    }


    private fun showTime() {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = calendarData?.selectedHour!!
        calendar[Calendar.MINUTE] = calendarData?.selectedMinutes!!
        (findViewById<View>(R.id.txtTime) as TextView).text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
    }

    override fun dateSelect(selectedDate: Date?) {
        if (calendarData?.selectedStartDate == null || calendarData?.single!!) {
            calendarData?.selectedStartDate = selectedDate
            showCalendar()
            return
        }
        if (calendarData?.selectedEndDate == null) {
            when {
                selectedDate?.time!! < calendarData?.selectedStartDate?.time!! -> {
                    calendarData?.selectedEndDate = calendarData?.selectedStartDate
                    calendarData?.selectedStartDate = selectedDate
                    showCalendar()
                    return
                }
                selectedDate.time == calendarData?.selectedStartDate?.time -> {
                    calendarData?.selectedEndDate = null
                    calendarData?.selectedStartDate = selectedDate
                    showCalendar()
                    return
                }
                selectedDate.time > calendarData?.selectedStartDate?.time!! -> {
                    calendarData?.selectedEndDate = selectedDate
                    showCalendar()
                    return
                }
            }
        }
        if (calendarData?.selectedEndDate != null) {
            calendarData?.selectedEndDate = null
            calendarData?.selectedStartDate = selectedDate
            showCalendar()
        }
    }

    override fun dateLongSelect(selectedDate: Date?) {
        calendarData?.selectedEndDate = null
        calendarData?.selectedStartDate = selectedDate
        showCalendar()
    }
}