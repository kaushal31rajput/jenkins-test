package com.hcl.icontrol.jenkins.support

import com.hcl.icontrol.jenkins.support.external.PipelineTestHelper
import org.apache.commons.lang3.StringUtils

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource

class JenkinsPipelineTestHelper extends PipelineTestHelper {

    @Override
    void setUp() {
        super.setUp()
        registerEnvironmentVariables()
        registerExtraMethods()
        registerSharedLibraryMethods()
        registerLoadResourcesMethods()
        registerSharedLibrary()
    }

    void registerEnvironmentVariables() {
        addEnvVar("GCR_REPOSITORY", "europe-west2-docker.pkg.dev/dryice-icontrol/k8s")
        addEnvVar("DEV_CLUSTER_IP", "europe-west2-docker.pkg.dev/dryice-icontrol/k8s")
        addEnvVar("HELM_CHART_REPOSITORY_REGION", "europe-west2-docker.pkg.dev")
    }

    void registerSharedLibrary() {
        def library = library().name('jenkins-pipeline-shared')
                .defaultVersion("develop")
                .allowOverride(true)
                .implicit(false)
                .targetPath('<notNeeded>')
                .retriever(projectSource("./target"))
                .build()
        helper.registerSharedLibrary(library)
    }

    void registerSharedLibraryMethods() {
        helper.registerAllowedMethod("log", [String.class, String.class], { logLevel, message ->
            println(message as String)
        })
        helper.registerAllowedMethod("log", [String.class, Map.class], { logLevel, message ->
            println(message as String)
        })
        helper.registerAllowedMethod("gitCommitShort", [], {
            return "1234567"
        })
        helper.registerAllowedMethod("isBuildingOnTag", [], {
            return getBindingOrEnvironmentVariable("TAG_NAME").isPresent()
        })
        helper.registerAllowedMethod("emailextrecipients", [List.class], {
            return ""
        })
        helper.registerAllowedMethod("isBuildingOnBranch", [Map.class], { config ->
            def variable = getBindingOrEnvironmentVariable("GIT_BRANCH")
            if (variable.isEmpty()) {
                return false
            }
            String regex = (config(Map)).get("BRANCH_REGEX")
            if (variable.isPresent() && StringUtils.isBlank(regex)) {
                return true
            }
            return variable.get().matches(branchRegex as String)
        })
        helper.registerAllowedMethod("isBuildingOnPullRequest", [], {
            def changeId = getBindingOrEnvironmentVariable("CHANGE_ID")
            def branchName = getBindingOrEnvironmentVariable("BRANCH_NAME")
            return branchName.isPresent() && branchName.get().startsWith("PR-") &&
                    changeId.isPresent() && changeId.get().isNumber()
        })
        helper.registerAllowedMethod("kubernetesSwitchNamespace", [String.class], {})
    }

    void registerExtraMethods() {
    }

    void registerLoadResourcesMethods() {
        helper.registerAllowedMethod("readJSON", [Map.class], { config ->
            return [:]
        })
        helper.registerAllowedMethod("readYaml", [Map.class], { config ->
            def yamlData = TestYamlUtils.readYamlFromPath(config.file)
            return yamlData
        })
        helper.registerAllowedMethod("fileExists", [String.class], { file ->
            return true
        })
    }

    private Optional<String> getBindingOrEnvironmentVariable(String variableName) {
        if (binding.hasVariable(variableName)) {
            return Optional.of(binding.getVariable(variableName) as String)
        }
        if (binding.hasVariable('env')) {
            def expando = binding.getVariable('env') as Expando
            if (expando.getProperty(variableName)) {
                return Optional.of(expando.getProperty(variableName) as String)
            }
            return Optional.empty()
        }
        return Optional.empty()
    }

}
