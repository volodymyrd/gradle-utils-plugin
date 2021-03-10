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
                    String protoDirOrJar = getJarPath(project, option)
                    ProtoToPojo.Options options = ProtoToPojo.Options.builder()
                            .withDefaultValues()
                            .protoCompiledDirOrJar(protoDirOrJar)
                            .doc("This POJO class was created automatically from the Google protobuf file \$S.")
                            .build()
                    for (String className : option.protoClasses) {
                        project.logger.info("Generating pojo for proto class: " + className)
                        List<ProtoToPojo.Result> results = new ProtoToPojo(className, options).generate().results
                        for (ProtoToPojo.Result result : results) {
                            File dir = new File(generatedFilesBaseDir + "/" + result.packageName().replaceAll("\\.", "/"))
                            if (!dir.exists()) {
                                dir.mkdirs()
                            }
                            new File(dir, result.className() + ".java") << result.pojoFile()
                        }
                        numberOfFiles += results.size()
                    }
                }
                project.logger.info("Successfully created ${numberOfFiles} file(s)")
            }
        }
    }

    private static String getJarPath(Project project, ProtoToPojoGradlePluginOption option) {
        if (isNotEmpty(option.dependencyJar)) {
            if (isNotEmpty(option.protoDirOrJar)) {
                throw new RuntimeException("Only 'protoDirOrJar' or 'dependencyJar' parameter must be provided")
            }
            return resolveDep(project, option.dependencyJar)
        } else if (!isNotEmpty(option.protoDirOrJar)) {
            throw new RuntimeException("Please provide either 'protoDirOrJar' or 'dependencyJar' parameter")
        }
        return option.protoDirOrJar
    }

    private static boolean isNotEmpty(String string) {
        if (string?.trim()) {
            return true
        }
        return false
    }

    private static String resolveDep(Project project, String depName) {
        Set<File> deps = project.getConfigurations().findByName("compileClasspath").resolve();
        project.logger.info("Got deps ${deps} file(s)")
        return deps.find { it.name.startsWith(depName) }
    }
}
