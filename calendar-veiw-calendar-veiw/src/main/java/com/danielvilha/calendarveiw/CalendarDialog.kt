package com.danielvilha.calendarveiw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.danielvilha.calendarveiw.listeners.DialogCompleteListener
import java.util.*

/**
 * Created by danielvilha on 25/06/20
 * https://github.com/danielvilha
 */
class CalendarDialog : DialogFragment(), DialogCompleteListener {
    private var slyCalendarData = CalendarData()
    private var callback: Callback? = null

    interface Callback {
        fun onCancelled()
        fun onDataSelected(firstDate: Calendar?, secondDate: Calendar?, hours: Int, minutes: Int)
    }

    override fun complete() {
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SlyCalendarDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val calendarView = layoutInflater.inflate(R.layout.calendar_main, container) as CalendarView
        calendarView.setSlyCalendarData(slyCalendarData)
        calendarView.setCallback(callback)
        calendarView.setCompleteListener(this)
        return calendarView
    }


    fun setStartDate(startDate: Date?): CalendarDialog? {
        slyCalendarData.selectedStartDate = startDate
        return this
    }

    fun setEndDate(endDate: Date?): CalendarDialog? {
        slyCalendarData.selectedEndDate = endDate
        return this
    }

    fun setSingle(single: Boolean): CalendarDialog? {
        slyCalendarData.single = single
        return this
    }

    fun setFirstMonday(firsMonday: Boolean): CalendarDialog? {
        slyCalendarData.firstMonday = firsMonday
        return this
    }

    fun setCallback(callback: Callback?): CalendarDialog? {
        this.callback = callback
        return this
    }

    fun setTimeTheme(themeResource: Int?): CalendarDialog? {
        slyCalendarData.timeTheme = themeResource
        return this
    }

    fun getCalendarFirstDate(): Date? {
        return slyCalendarData.selectedStartDate
    }

    fun getCalendarSecondDate(): Date? {
        return slyCalendarData.selectedEndDate
    }


    fun setBackgroundColor(backgroundColor: Int?): CalendarDialog? {
        slyCalendarData.backgroundColor = backgroundColor
        return this
    }

    fun setHeaderColor(headerColor: Int?): CalendarDialog? {
        slyCalendarData.headerColor = headerColor
        return this
    }

    fun setHeaderTextColor(headerTextColor: Int?): CalendarDialog? {
        slyCalendarData.headerTextColor = headerTextColor
        return this
    }

    fun setTextColor(textColor: Int?): CalendarDialog? {
        slyCalendarData.textColor = textColor
        return this
    }

    fun setSelectedColor(selectedColor: Int?): CalendarDialog? {
        slyCalendarData.selectedColor = selectedColor
        return this
    }

    fun setSelectedTextColor(selectedTextColor: Int?): CalendarDialog? {
        slyCalendarData.selectedTextColor = selectedTextColor
        return this
    }

    fun showTime(show: Boolean): CalendarDialog?  {
        slyCalendarData.showTime = show
        return this
    }
}