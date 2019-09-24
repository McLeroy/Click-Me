package com.mcleroy.clickme.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.mcleroy.clickme.R

class SessionWindowBottomSheet(context: Context, currentDurationInMins: Long, sessionWindowCallback: SessionWindowCallback) : BottomSheetDialog(context) {

    private var seekBar: SeekBar
    private var statusText: TextView

    init {
        @SuppressLint("InflateParams")
        val rootView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_session_window, null)
        setContentView(rootView)
        setTitle(R.string.change_window)
        seekBar = rootView.findViewById(R.id.seekBar)
        statusText = rootView.findViewById(R.id.statusText)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                processSeekBarChange()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        val progress = currentDurationInMins / 5
        seekBar.progress = progress.toInt()
        rootView.findViewById<MaterialButton>(R.id.proceedBtn).setOnClickListener(object: (View.OnClickListener) {
            override fun onClick(p0: View?) {
                val currentProgress = if (seekBar.progress == 0) 1 else seekBar.progress
                sessionWindowCallback.windowDelayChanged(5L * currentProgress)
                dismiss()
            }
        })
    }

    private fun processSeekBarChange() {
        val currentProgress = if (seekBar.progress == 0) 1 else seekBar.progress
        val delay = 5L * currentProgress
        statusText.text = String.format("%s %s", delay, if (delay == 1L) "min" else "mins")
        DebugUtils.debug(SessionWindowBottomSheet::class.java, "Seek bar progress: " + seekBar.progress)
    }

    interface SessionWindowCallback {
        fun windowDelayChanged(delay: Long)
    }
}