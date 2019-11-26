package com.malinskiy.marathon.execution.device

import com.malinskiy.marathon.execution.TestResult
import com.malinskiy.marathon.test.TestBatch
import kotlinx.coroutines.CompletableDeferred

sealed class DeviceEvent {
    data class Execute(val batch: TestBatch) : DeviceEvent()
    object Complete : DeviceEvent()
    object Initialize : DeviceEvent()
    object Terminate : DeviceEvent()
    object WakeUp : DeviceEvent()
    data class GetDeviceState(val deferred: CompletableDeferred<DeviceState>) : DeviceEvent()

    sealed class TestFinished(val testResult: TestResult) : DeviceEvent()

    override fun toString(): String = "DeviceEvent.${this::class.java.simpleName}"
}
