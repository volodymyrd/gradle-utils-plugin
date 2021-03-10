package com.volmyr.gradle_plugins.utils

import org.gradle.api.Project

/**
 * Extension to configure the ProtoToPojo plugin.
 */
abstract class ProtoToPojoGradlePluginExtension {
    private final Project project

    String generatedFilesBaseDir
    def options = []

    ProtoToPojoGradlePluginExtension(Project project) {
        this.project = project
        generatedFilesBaseDir = "${project.buildDir}/generated/source/pojo"
    }

    def protoToPojo(Closure closure) {
        closure.delegate = this
        closure()
    }

    def option(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        def optionClosureDelegate = new OptionClosureDelegate(project)
        closure.delegate = optionClosureDelegate
        options << optionClosureDelegate.option
        closure()
    }

    private static class OptionClosureDelegate {

        @Delegate
        final ProtoToPojoGradlePluginOption option = new ProtoToPojoGradlePluginOption()
        private final Project project

        OptionClosureDelegate(Project project) {
            this.project = project
        }
    }
}
