package com.malinskiy.marathon.report

import com.google.gson.Gson
import com.malinskiy.marathon.analytics.internal.sub.DeviceConnectedEvent
import com.malinskiy.marathon.analytics.internal.sub.ExecutionReport
import com.malinskiy.marathon.analytics.internal.sub.TestEvent
import com.malinskiy.marathon.device.DeviceFeature
import com.malinskiy.marathon.device.DeviceInfo
import com.malinskiy.marathon.device.DevicePoolId
import com.malinskiy.marathon.device.DeviceProvider
import com.malinskiy.marathon.device.NetworkState
import com.malinskiy.marathon.device.OperatingSystem
import com.malinskiy.marathon.execution.AnalyticsConfiguration
import com.malinskiy.marathon.execution.Configuration
import com.malinskiy.marathon.execution.TestParser
import com.malinskiy.marathon.execution.TestResult
import com.malinskiy.marathon.execution.TestStatus
import com.malinskiy.marathon.io.FileManager
import com.malinskiy.marathon.log.MarathonLogConfigurator
import com.malinskiy.marathon.report.junit.JUnitReporter
import com.malinskiy.marathon.report.junit.JUnitWriter
import com.malinskiy.marathon.test.Test
import com.malinskiy.marathon.vendor.VendorConfiguration
import org.amshove.kluent.shouldBe
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.testng.reporters.JUnitReportReporter
import java.io.File
import java.time.Instant

class JUnitReporterSpek : Spek(
    {

        val configuration = Configuration(
            name = "",
            outputDir = File("src/test/resources/output/"),
            analyticsConfiguration = AnalyticsConfiguration.DisabledAnalytics,
            poolingStrategy = null,
            shardingStrategy = null,
            sortingStrategy = null,
            batchingStrategy = null,
            flakinessStrategy = null,
            retryStrategy = null,
            filteringConfiguration = null,
            ignoreFailures = null,
            isCodeCoverageEnabled = null,
            fallbackToScreenshots = null,
            strictMode = null,
            uncompletedTestRetryQuota = null,
            testClassRegexes = null,
            includeSerialRegexes = null,
            excludeSerialRegexes = null,
            testBatchTimeoutMillis = null,
            testOutputTimeoutMillis = null,
            debug = null,
            vendorConfiguration = object : VendorConfiguration {
                override fun testParser(): TestParser? = null
                override fun deviceProvider(): DeviceProvider? = null
                override fun logConfigurator(): MarathonLogConfigurator? = null
                override fun preferableRecorderType(): DeviceFeature? = null
            },
            analyticsTracking = false
        )

        val fileManager = FileManager(configuration.outputDir)
        val gson = Gson()
        println(configuration.outputDir.absolutePath)

        fun createTestEvent(deviceInfo: DeviceInfo, methodName: String, status: TestStatus, final: Boolean = true): TestEvent {
            return TestEvent(
                Instant.now(),
                DevicePoolId("myPool"),
                deviceInfo,
                TestResult(
                    Test("com", "example", methodName, emptyList()),
                    deviceInfo,
                    status,
                    0,
                    100
                ),
                final
            )
        }

        given("an ExecutionReport without retries") {
            val device = DeviceInfo(
                operatingSystem = OperatingSystem("23"),
                serialNumber = "xxyyzz",
                model = "Android SDK built for x86",
                manufacturer = "unknown",
                networkState = NetworkState.CONNECTED,
                deviceFeatures = listOf(DeviceFeature.SCREENSHOT, DeviceFeature.VIDEO),
                healthy = true
            )
            val report = ExecutionReport(
                deviceProviderPreparingEvent = emptyList(),
                devicePreparingEvents = emptyList(),
                deviceConnectedEvents = listOf(
                    DeviceConnectedEvent(Instant.now(), DevicePoolId("myPool"), device)
                ),
                testEvents = listOf(
                    createTestEvent(device, "test1", TestStatus.INCOMPLETE),
                    createTestEvent(device, "test2", TestStatus.PASSED),
                    createTestEvent(device, "test3", TestStatus.FAILURE)
                )


            )

            val jUnitWriter = JUnitWriter(FileManager(configuration.outputDir))
            val junitReport= JUnitReporter(jUnitWriter)
            junitReport.generate(report)


        }
    })

