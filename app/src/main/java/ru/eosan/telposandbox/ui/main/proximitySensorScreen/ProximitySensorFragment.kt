package ru.eosan.telposandbox.ui.main.proximitySensorScreen

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_proximity_sensor.*
import ru.eosan.telposandbox.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class ProximitySensorFragment : Fragment() {

    private lateinit var viewModel: ProximitySensorViewModel

    private var log = StringBuilder("log start").appendln()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(ProximitySensorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_proximity_sensor, container, false)
        viewModel.proximitySensorStatus.observe(viewLifecycleOwner, Observer<Boolean> { status ->
            status?.let {
                onSensorStatusUpdate(it)
            }
        })
        return root
    }

    private fun onSensorStatusUpdate(status: Boolean) {
        proximitySensorStatusLabel.text = if (status) "Привет" else "Никого..."
        log.appendln("${SimpleDateFormat("HH:mm:ss").format(Date())} Proximity status $status")
        updateLog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews() {
        proximityLogTextView.movementMethod = ScrollingMovementMethod()
    }

    private fun updateLog() {
        proximityLogTextView.text = log.toString()
    }
}