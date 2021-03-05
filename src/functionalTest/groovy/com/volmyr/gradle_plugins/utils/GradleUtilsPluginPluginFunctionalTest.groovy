package com.volmyr.gradle_plugins.utils

import spock.lang.Specification
import org.gradle.testkit.runner.GradleRunner

/**
 * A simple functional test for the 'com.volmyr.gradle_plugins.utils.greeting' plugin.
 */
class GradleUtilsPluginPluginFunctionalTest extends Specification {
    def "can run task"() {
        given:
        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
            plugins {
                id('com.volmyr.gradle_plugins.utils.greeting')
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("greeting")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        result.output.contains("Hello from plugin 'com.volmyr.gradle_plugins.utils.greeting'")
    }
}
