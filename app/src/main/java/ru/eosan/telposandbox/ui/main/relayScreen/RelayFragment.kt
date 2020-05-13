package ru.eosan.telposandbox.ui.main.relayScreen

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_relay.*
import ru.eosan.telposandbox.R
import ru.eosan.telposandbox.TelpoDeviceInteractorImpl
import java.text.SimpleDateFormat
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class RelayFragment : Fragment() {

    private lateinit var viewModel: RelayViewModel

    private var log = StringBuilder("log start").appendln()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(RelayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews() {
        relayLogTextView.movementMethod = ScrollingMovementMethod()
        openRelayButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.relayToggle(true)
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Relay opened ${if (result) "success" else "failed"}")
            updateRelayStatus()
            updateLog()
        }
        closeRelayButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.relayToggle(false)
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Relay closed ${if (result) "success" else "failed"}")
            updateRelayStatus()
            updateLog()
        }
        toggleRelayButton.setOnClickListener {
            val result = TelpoDeviceInteractorImpl.relayToggle()
            log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Relay toggled ${if (result) "success" else "failed"}")
            updateRelayStatus()
            updateLog()
        }
        updateRelayStatus.setOnClickListener {
            updateRelayStatus()
        }
    }

    private fun updateLog() {
        relayLogTextView.text = log.toString()
    }

    private fun updateRelayStatus() {
        relayStatusLabel.text =
            if (TelpoDeviceInteractorImpl.isRelayOpen()) "Открыто" else "Закрыто"
    }

}