package com.volmyr.gradle_plugins.utils

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Tests for {@link ProtoToPojoGradlePlugin}.
 */
class ProtoToPojoGradlePluginTest extends Specification {
    def "plugin registers task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply("com.volmyr.gradle_plugins.utils.protoToPojo")

        then:
        project.tasks.findByName("protoToPojo") != null
    }
}
