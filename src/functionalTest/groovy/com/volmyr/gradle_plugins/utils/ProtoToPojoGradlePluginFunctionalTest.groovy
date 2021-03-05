package com.volmyr.gradle_plugins.utils

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

/**
 * Functional tests for 'com.volmyr.gradle_plugins.utils.protoToPojo' plugin.
 */
class ProtoToPojoGradlePluginFunctionalTest extends Specification {
    def "can run task"() {
        given:
        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
            plugins {
                id('com.volmyr.gradle_plugins.utils.protoToPojo')
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("protoToPojo")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        result.output.contains("Hello from plugin 'protoToPojo'")
    }
}
