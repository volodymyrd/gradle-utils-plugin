package com.volmyr.gradle_plugins.utils

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

/**
 * Functional tests for 'com.volmyr.gradle_plugins.utils.protoToPojo' plugin.
 */
class ProtoToPojoGradlePluginFunctionalTest extends Specification {
    def "can run task"() {
        given:
        def protoDir = new File("build/classes/java/functionalTest")
        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle") << ""
        new File(projectDir, "build.gradle") << """
            plugins {
                id('com.volmyr.gradle_plugins.utils.protoToPojo')
            }
            protoToPojo {
                generatedFilesBaseDir = "${protoDir.absolutePath}"
                option {
                    protoDirOrJar = "${protoDir.absolutePath}"
                    protoClasses = ["com.volmyr.proto.model.test.Person"]
                }           
            }
        """

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("protoToPojo", "--info")
        runner.withDebug(true)
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        result.output.contains("Successfully created 1 file(s)")
    }

    def cleanup() {
        println('cleanup')
        def projectDir = new File("build/functionalTest")
        projectDir.deleteDir()
    }
}
