package com.danielvilha.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.danielvilha.calendarveiw.CalendarDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by danielvilha on 25/06/20
 * https://github.com/danielvilha
 */
class MainActivity : AppCompatActivity(), CalendarDialog.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShowCalendar.setOnClickListener {
            CalendarDialog()
                .setSingle(false)
                ?.setFirstMonday(false)
                ?.setCallback(this@MainActivity)
                ?.show(supportFragmentManager, MainActivity::class.java.name)
        }

        btnShowCalendarHidden.setOnClickListener {
            CalendarDialog()
                .setSingle(false)
                ?.setFirstMonday(false)
                ?.showTime(false)
                ?.setCallback(this@MainActivity)
                ?.show(supportFragmentManager, MainActivity::class.java.name)
        }
    }

    override fun onCancelled() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
    }

    override fun onDataSelected(firstDate: Calendar?, secondDate: Calendar?, hours: Int, minutes: Int) {
        if (firstDate != null) {
            if (secondDate == null) {
                firstDate[Calendar.HOUR_OF_DAY] = hours
                firstDate[Calendar.MINUTE] = minutes
                Snackbar.make(
                    findViewById(R.id.btnShowCalendar),
                    SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault()).format(firstDate.time),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                secondDate[Calendar.HOUR_OF_DAY] = hours
                secondDate[Calendar.MINUTE] = minutes
                Snackbar.make(
                    findViewById(R.id.btnShowCalendar),
                    getString(
                        R.string.period,
                        SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.time),
                        SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault()).format(secondDate.time)
                    ),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            Snackbar.make(
                findViewById(R.id.btnShowCalendar),
                "No days selected",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}