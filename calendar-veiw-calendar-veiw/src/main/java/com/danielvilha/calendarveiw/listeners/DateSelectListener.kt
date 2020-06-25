package com.danielvilha.calendarveiw.listeners

import java.util.*

/**
 * Created by danielvilha on 25/06/20
 * https://github.com/danielvilha
 */
interface DateSelectListener {
    fun dateSelect(selectedDate: Date?)
    fun dateLongSelect(selectedDate: Date?)
}