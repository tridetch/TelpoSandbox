package ru.eosan.telposandbox.ui.main.ledScreen

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_led.*
import ru.eosan.telposandbox.R
import ru.eosan.telposandbox.TelpoDeviceInteractorImpl
import java.text.SimpleDateFormat
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class LedFragment : Fragment() {

    private lateinit var viewModel: LedViewModel

    private var log = StringBuilder("log start").appendln()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(LedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_led, container, false)
        viewModel.text.observe(viewLifecycleOwner, Observer<String> {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews() {
        ledLogTextView.movementMethod = ScrollingMovementMethod()
        turnOnLedButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.ledPowerToggle(true)
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Led turned on ${if (result) "success" else "failed"}")
            updateLedStatus()
            updateLog()
        }
        turnOffLedButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.ledPowerToggle(false)
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Led turned off ${if (result) "success" else "failed"}")
            updateLedStatus()
            updateLog()
        }
        toggleLedButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.ledPowerToggle()
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Led toggled ${if (result) "success" else "failed"}")
            updateLedStatus()
            updateLog()
        }
        brightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                TelpoDeviceInteractorImpl.setLedPowerBrightness(
                    progress,
                    autoPowerCheckBox.isChecked
                )
                updateLedStatus()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        updateLedStatus.setOnClickListener {
            updateLedStatus()
        }
    }

    private fun updateLog() {
        ledLogTextView.text = log.toString()
    }

    private fun updateLedStatus() {
        ledStatusLabel.text = if (TelpoDeviceInteractorImpl.isLedPowerOn()) "Вкл" else "Выкл"
    }

}