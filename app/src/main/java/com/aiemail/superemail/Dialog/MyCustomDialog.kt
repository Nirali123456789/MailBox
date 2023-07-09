package com.aiemail.superemail.Dialog

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.aiemail.superemail.Activities.DirectComposeActivity.Companion.fronDraft
import com.aiemail.superemail.Activities.DirectComposeActivity.Companion.isSaved
import com.aiemail.superemail.Activities.SnoozeActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.Snooze.AlarmReceiver
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PreferenceManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.api.services.gmail.model.Message

import java.util.*


class MyCustomDialog(
    layout1: Int,
    sender_id_: String = "",
    dialogListner: DialogListner,
    activity: AppCompatActivity,
    realObject: Message
) : DialogFragment() {
    var layout = layout1
    var sender_id = sender_id_
    private val TIMEOUT = 1500
    var dialogListner = dialogListner
    var appCompatActivity: AppCompatActivity = activity
    val email: Message = realObject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);

        preferenceManager = PreferenceManager(context!!)
        preferenceManager.SetUpPreference()

        var v = inflater.inflate(layout, container, false)
        if (layout == R.layout.layout_discard_dialog) {
            v.findViewById<TextView>(R.id.discard).setOnClickListener {
                dialog?.dismiss()
                activity!!.finish()
                activity!!.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out
                );
            }

            v.findViewById<TextView>(R.id.cancel).setOnClickListener {
                dialog!!.dismiss()
            }
            v.findViewById<TextView>(R.id.save).setOnClickListener {
                Thread {
                    Helpers.createDraftEmail(requireActivity(), email)
                }.start()
                fronDraft = false
                isSaved = true
                dialog!!.dismiss()
                activity!!.finish()
                activity!!.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out
                );

            }
        }
        if (layout == R.layout.layout_logout) {


            v.findViewById<TextView>(R.id.cancel).setOnClickListener {
                dialog!!.dismiss()
            }
            v.findViewById<TextView>(R.id.save).setOnClickListener {

                dialog!!.dismiss()
                activity!!.finish()
            }

        }

        if (layout == R.layout.layout_language_dialog) {
            val languageCodes = arrayOf(
                "en", // English
                "de", // German
                "es", // Spanish
                "fr", // French
                "it", // Italian
                "ja", // Japanese
                "pt", // Portuguese
                "ru", // Russian
                "zh", // Chinese
                "uk"  // Ukrainian
            )


            //Radio button on click change
            val radioGroup = v.findViewById(R.id.languges) as RadioGroup
            radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->


                    val radio_langange: RadioButton = v.findViewById(checkedId)
                    val position = radioGroup.indexOfChild(radio_langange)
                    preferenceManager.setInt("LocalPos", position)
                    Log.d("TAG", "onCreateView: " + radio_langange.text.toString())
                    Helpers.setLocal(activity!!, languageCodes[position])
                    activity?.recreate()
                    activity!!.finish()


                }
            )

        }
        if (layout == R.layout.layout_snooze_dialog) {


            Helpers.Visibility(v.findViewById<CheckBox>(R.id.check4), preferenceManager, "check4")
            Helpers.Visibility(v.findViewById<CheckBox>(R.id.check5), preferenceManager, "check5")
            Helpers.Visibility(v.findViewById<CheckBox>(R.id.check6), preferenceManager, "check6")
            Helpers.Visibility(v.findViewById<CheckBox>(R.id.check7), preferenceManager, "check7")

            // dialog!!.dismiss()
            // Get the current time
            // Get the current time
            var currentTime = Calendar.getInstance()
            currentTime.setTimeZone(Helpers.GetTimeZone());
            val currentHour = currentTime[Calendar.HOUR_OF_DAY]
            val currentMinute = currentTime[Calendar.MINUTE]
            // Add 2 hours to the current time

// Add 2 hours to the current time
            currentTime.add(Calendar.HOUR_OF_DAY, 3)
            val updatedHour = currentTime[Calendar.HOUR_OF_DAY]
            val updatedMinute = currentTime[Calendar.MINUTE]
            val selectedHour = updatedHour
            val selectedMinute = updatedMinute
            val selectedSecond = 0
            v.findViewById<TextView>(R.id.time).setText("" + selectedHour + ":" + selectedMinute)
            v.findViewById<TextView>(R.id.pick).setOnClickListener {
                currentTime = Calendar.getInstance()
                val currentHour = currentTime[Calendar.HOUR_OF_DAY]
                val currentMinute = currentTime[Calendar.MINUTE]

                val timePicker = MaterialTimePicker

                    .Builder()
                    .setTitleText("Select a time")
                    .setHour(currentHour)
                    .setMinute(currentMinute)
                    .build()



                timePicker.addOnPositiveButtonClickListener { picker ->
                    val selectedHour = timePicker.hour
                    val selectedMinute = timePicker.minute
                    val selectedSecond = 0
                    setAlarmIntent(
                        appCompatActivity,
                        selectedHour,
                        selectedMinute,
                        selectedSecond,
                        sender_id,
                        sender_id
                    )
                    dialogListner.dialogClick(sender_id, this)
                    // Handle the time selection here
                    // Perform actions with the selectedHour and selectedMinute
                }
                timePicker.show(childFragmentManager, "TIME_PICKER")
            }
            v.findViewById<RelativeLayout>(R.id.later_today).setOnClickListener {
                dialog!!.dismiss()
                getTime(selectedHour, selectedMinute, selectedSecond)

            }
            v.findViewById<RelativeLayout>(R.id.tomorrow_layout).setOnClickListener {
                dialog!!.dismiss()
                val tomorrow9AM = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 9)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }


                getTime(
                    tomorrow9AM.get(Calendar.HOUR_OF_DAY),
                    tomorrow9AM.get(Calendar.MINUTE),
                    tomorrow9AM.get(Calendar.SECOND)
                )

            }
            v.findViewById<RelativeLayout>(R.id.next_week).setOnClickListener {
                val nextWeekMonday = Calendar.getInstance().apply {
                    add(Calendar.WEEK_OF_YEAR, 1)
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    set(Calendar.HOUR_OF_DAY, 11)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                // Add 2 hours to the current time

                getTime(
                    nextWeekMonday.get(Calendar.HOUR_OF_DAY),
                    nextWeekMonday.get(Calendar.MINUTE),
                    nextWeekMonday.get(Calendar.SECOND)
                )
            }

            v.findViewById<LinearLayout>(R.id.check4).setOnClickListener {
                val today8Pm = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 20)// Set hour to 8 PM (24-hour format)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }


                getTime(
                    today8Pm.get(Calendar.HOUR_OF_DAY),
                    today8Pm.get(Calendar.MINUTE),
                    today8Pm.get(Calendar.SECOND)
                )

            }
            v.findViewById<LinearLayout>(R.id.check5).setOnClickListener {
                val calendar = Calendar.getInstance()
                // Find the next occurrence of a weekend (Saturday or Sunday)
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(
                        Calendar.DAY_OF_WEEK
                    ) != Calendar.SUNDAY
                ) {
                    calendar.add(Calendar.DAY_OF_WEEK, 1)
                }

                // Get the dates of the next Saturday and Sunday
                val saturday = calendar.time
                calendar.add(Calendar.DAY_OF_WEEK, 1)
                val sunday = calendar.time

                // Optionally, you can set the time to 9:00 AM
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val saturday9AM = calendar
                calendar.add(Calendar.DAY_OF_WEEK, 1)
                val sunday9AM = calendar

                getTime(
                    saturday9AM.get(Calendar.HOUR_OF_DAY),
                    saturday9AM.get(Calendar.MINUTE),
                    saturday9AM.get(Calendar.SECOND)
                )
                getTime(
                    sunday9AM.get(Calendar.HOUR_OF_DAY),
                    sunday9AM.get(Calendar.MINUTE),
                    sunday9AM.get(Calendar.SECOND)
                )

            }
            v.findViewById<LinearLayout>(R.id.check6).setOnClickListener {


                val calendar = Calendar.getInstance()
                // Find the next occurrence of a week
                // Find the next occurrence of Monday
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calendar.add(Calendar.DAY_OF_WEEK, 1)
                }

                // Set the time to 9:00 AM
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // Get the date and time of the next Monday at 9:00 AM
                val nextMonday9AM = calendar

                getTime(
                    nextMonday9AM.get(Calendar.HOUR_OF_DAY),
                    nextMonday9AM.get(Calendar.MINUTE),
                    nextMonday9AM.get(Calendar.SECOND)
                )


            }
            v.findViewById<LinearLayout>(R.id.check7).setOnClickListener {
                val calendar = Calendar.getInstance()

                // Set the calendar to the first day of the next month
                calendar.add(Calendar.MONTH, 1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)

                // Find the next occurrence of Monday
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calendar.add(Calendar.DAY_OF_WEEK, 1)
                }

                // Set the time to 9:00 AM
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // Get the date and time of the next Monday at 9:00 AM
                val nextMonday9AM = calendar


                getTime(
                    nextMonday9AM.get(Calendar.HOUR_OF_DAY),
                    nextMonday9AM.get(Calendar.MINUTE),
                    nextMonday9AM.get(Calendar.SECOND)
                )

            }



            v.findViewById<ImageView>(R.id.setting).setOnClickListener {
                dialog!!.dismiss()
                activity!!.startActivity(Intent(activity, SnoozeActivity::class.java))
                activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
        return v
    }

    private fun setAlarmIntent(
        context: Activity,
        Hours_Of_day: Int,
        Minutes: Int,
        Seconds: Int,
        sender_id: String,
        sender: String
    ) {
        Log.d("TAG", "sendEmailFullmessage: setAlarmIntent" + sender_id)
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.putExtra("senderid", sender_id)
//        intent.putExtra("sender", sender)

        // Create a Bundle and add data to it
        val extras = Bundle()
        extras.putString("senderid", sender_id)
        extras.putString("sender", sender)
        intent.putExtras(extras)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = Hours_Of_day // set hour

        cal[Calendar.MINUTE] = Minutes // set minute

        cal[Calendar.SECOND] = Seconds // set seconds


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            pendingIntent
        )

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    interface DialogListner {
        fun dialogClick(sender_id: String, dialog: MyCustomDialog)
    }

    fun getTime(selectedHour: Int, selectedMinute: Int, selectedSecond: Int) {


// Retrieve the updated hour and minute

// Retrieve the updated hour and minute


        setAlarmIntent(
            appCompatActivity,
            selectedHour,
            selectedMinute,
            selectedSecond,
            sender_id,
            sender_id
        )
        dialogListner.dialogClick(sender_id, this)
    }
}