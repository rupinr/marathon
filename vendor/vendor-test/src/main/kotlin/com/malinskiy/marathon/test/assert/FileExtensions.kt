package com.malinskiy.marathon.test.assert

import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.File
import org.xmlunit.assertj.XmlAssert.assertThat
import org.xmlunit.builder.Input

fun File.shouldBeEqualToAsJson(expected: File) {
    JSONAssert.assertEquals(expected.readText(), readText(), JSONCompareMode.LENIENT)
}

fun File.shouldBeEqualToAsXML(expected: File) {
    assertThat(this).and(Input.fromFile(expected)).areIdentical()
}
