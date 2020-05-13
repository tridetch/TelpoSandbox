package ru.eosan.telposandbox

import androidx.annotation.IntRange
import com.common.pos.api.util.PosUtil
import com.common.pos.api.util.ShellUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by tride on 11.05.2020. MobileIdGuard
 */

interface TelpoDeviceInteractor {

    //region Led control
    /**
     * Control led power
     * @param enable true to turn of, false to turn off, null to toggle
     * @return true if operation success, false otherwise
     * */
    fun ledPowerToggle(enable: Boolean? = null): Boolean

    /**
     * Set led brightness
     * @param brightnessPercent brightness in percents. From 0 to 100
     * @param autoPowerControl toggle power automatically depending on brightness. Turn of if brightness = 0, turn on otherwise
     * */
    fun setLedPowerBrightness(
        @IntRange(from = 0, to = 100) brightnessPercent: Int,
        autoPowerControl: Boolean = true
    )

    /**
     * Get led power status
     * @return true if power on, false otherwise
     * */
    fun isLedPowerOn(): Boolean

    /**
     * Get current brightness
     * @return led brightness in percents
     * */
    @IntRange(from = 0, to = 100)
    fun getLedBrightness(): Int
    //endregion

    /**
     * Control relay
     * @param open true to open relay, false to close relay, null to toggle
     * @return true if operation success, false otherwise
     * */
    fun relayToggle(open: Boolean? = null): Boolean

    /**
     * Get relay status
     * @return true if relay open, false otherwise
     * */
    fun isRelayOpen(): Boolean

    /**
     * Return flow of proximity sensor events
     * @return true if someone in front of terminal, false if nobody
     * */
    fun getProximitySensorEvents(): Flow<Boolean>
}

object TelpoDeviceInteractorImpl : TelpoDeviceInteractor {

    //region Led control
    private var isLedEnabled: Boolean = false

    @IntRange(from = 0, to = 100)
    var currentLedBrightness: Int = 0

    override fun ledPowerToggle(enable: Boolean?): Boolean {
        val ledPowerAction = enable ?: !isLedEnabled
        val isResultSuccess = when (ledPowerAction) {
            true -> PosUtil.setLedPower(1)
            false -> PosUtil.setLedPower(0)
        } == 0

        if (isResultSuccess) {
            isLedEnabled = ledPowerAction
        }

        return isResultSuccess
    }

    override fun setLedPowerBrightness(brightnessPercent: Int, autoPowerControl: Boolean) {
        currentLedBrightness = brightnessPercent
        if (autoPowerControl && needTogglePower(brightnessPercent)) {
            ledPowerToggle(brightnessPercent != 0)
        }
        val brightnessValue = (255 / 100) * brightnessPercent
        val cmd = "echo $brightnessValue > /sys/class/backlight/led-brightness/brightness"
        ShellUtils.execCommand(cmd, false)
    }

    private fun needTogglePower(brightnessPercent: Int): Boolean =
        isLedEnabled != (brightnessPercent > 0)

    override fun isLedPowerOn(): Boolean = isLedEnabled

    override fun getLedBrightness(): Int = currentLedBrightness

    //endregion

    //region Relay control
    private var isRelayOpened: Boolean = false

    override fun relayToggle(open: Boolean?): Boolean {
        val relayAction = open ?: !isRelayOpened
        val isResultSuccess = when (relayAction) {
            true -> PosUtil.setRelayPower(1)
            false -> PosUtil.setRelayPower(0)
        } == 0
        if (isResultSuccess) {
            isRelayOpened = relayAction
        }
        return isResultSuccess
    }

    override fun isRelayOpen(): Boolean = isRelayOpened
    //endregion

    //region Proximity service
    override fun getProximitySensorEvents(): Flow<Boolean> =
        flow {
            while (true) {
                delay(300)
                emit(PosUtil.getPriximitySensorStatus() == 1)
            }
        }
    //endregion
}