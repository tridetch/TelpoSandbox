package ru.eosan.telposandbox.ui.main.proximitySensorScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.eosan.telposandbox.TelpoDeviceInteractorImpl

class ProximitySensorViewModel : ViewModel() {

    private val sensorStatus = MutableLiveData<Boolean>()
    val proximitySensorStatus: LiveData<Boolean> = sensorStatus

    init {
        viewModelScope.launch {
            TelpoDeviceInteractorImpl.getProximitySensorEvents()
                .collect { sensorStatus.value = it }
        }
    }

}