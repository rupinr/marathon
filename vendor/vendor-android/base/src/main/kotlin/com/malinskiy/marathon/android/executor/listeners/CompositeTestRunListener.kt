package com.malinskiy.marathon.android.executor.listeners

import com.malinskiy.marathon.android.model.TestIdentifier

class CompositeTestRunListener(private val listeners: List<AndroidTestRunListener>) : AndroidTestRunListener {
    private inline fun execute(f: (AndroidTestRunListener) -> Unit) {
        listeners.forEach(f)
    }

    override fun testRunStarted(runName: String, testCount: Int) {
        execute { it.testRunStarted(runName, testCount) }
    }

    override fun testStarted(test: TestIdentifier) {
        execute { it.testStarted(test) }
    }

    override fun testAssumptionFailure(test: TestIdentifier, trace: String) {
        execute { it.testAssumptionFailure(test, trace) }
    }

    override fun testRunStopped(elapsedTime: Long) {
        execute { it.testRunStopped(elapsedTime) }
    }

    override fun testFailed(test: TestIdentifier, trace: String) {
        execute { it.testFailed(test, trace) }
    }

    override fun testEnded(test: TestIdentifier, testMetrics: Map<String, String>) {
        execute { it.testEnded(test, testMetrics) }
    }

    override fun testIgnored(test: TestIdentifier) {
        execute { it.testIgnored(test) }
    }

    override fun testRunFailed(errorMessage: String) {
        execute { it.testRunFailed(errorMessage) }
    }

    override fun testRunEnded(elapsedTime: Long, runMetrics: Map<String, String>) {
        execute { it.testRunEnded(elapsedTime, runMetrics) }
    }
}
