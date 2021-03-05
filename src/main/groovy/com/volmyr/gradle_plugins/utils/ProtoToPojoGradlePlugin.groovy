package com.volmyr.gradle_plugins.utils

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for generating Pojo classes from Google protobuf.
 */
class ProtoToPojoGradlePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.tasks.register("protoToPojo") {
            doLast {
                println("Hello from plugin 'protoToPojo'")
            }
        }
    }
}
