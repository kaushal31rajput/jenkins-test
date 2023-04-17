package com.hcl.icontrol.jenkins.support

import org.apache.commons.io.IOUtils
import org.yaml.snakeyaml.Yaml

import java.nio.charset.StandardCharsets

class TestYamlUtils {

    static def readYamlFromPath(String filePath) {
        if (filePath != null) {
            String text = getResourceAsString(filePath)
            def yamlObj = readYaml(text)
            return yamlObj
        }
        return null
    }

    static def readYaml(String text) {
        Yaml yaml = new Yaml()
        return yaml.load(text)
    }

    static String getResourceAsString(String resourceName) {
        InputStream is = TestYamlUtils.getResourceAsStream(resourceName)
        return IOUtils.toString(is, StandardCharsets.UTF_8)
    }
}
