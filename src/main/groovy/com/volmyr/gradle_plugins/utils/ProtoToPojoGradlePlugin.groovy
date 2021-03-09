package com.volmyr.gradle_plugins.utils

import com.volmyr.proto.utils.ProtoToPojo
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for generating Pojo classes from Google protobuf.
 */
class ProtoToPojoGradlePlugin implements Plugin<Project> {
    static final String PLUGIN_NAME = 'protoToPojo'

    void apply(Project project) {
        def extension = project.extensions.create(PLUGIN_NAME, ProtoToPojoGradlePluginExtension, project)

        project.tasks.register(PLUGIN_NAME) {
            doLast {
                String generatedFilesBaseDir = extension.generatedFilesBaseDir
                int numberOfFiles = 0;
                for (ProtoToPojoGradlePluginOption option : extension.options) {
                    ProtoToPojo.Options options = ProtoToPojo.Options.builder()
                            .withDefaultValues()
                            .protoCompiledDirOrJar(option.protoDirOrJar)
                            .doc("This POJO class was created automatically from the Google protobuf file \$S.")
                            .build()
                    for (String className : option.protoClasses) {
                        project.logger.info("Generating pojo for proto class: " + className)
                        List<ProtoToPojo.Result> results = new ProtoToPojo(className, options).generate().results
                        for (ProtoToPojo.Result result : results) {
                            String dir = generatedFilesBaseDir + "/" + result.packageName().replaceAll("\\.", "/")
                            new File(dir, result.className() + ".java") << result.pojoFile()
                        }
                        numberOfFiles += results.size()
                    }
                }
                project.logger.info("Successfully created ${numberOfFiles} file(s)")
            }
        }
    }
}
