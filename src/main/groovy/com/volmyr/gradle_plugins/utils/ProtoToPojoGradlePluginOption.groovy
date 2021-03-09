package com.volmyr.gradle_plugins.utils

/**
 * Option for the ProtoToPojo Gradle Plugin.
 */
class ProtoToPojoGradlePluginOption implements Serializable {
    def protoDirOrJar
    def protoClasses = []
}
