package com.hcl.icontrol.jenkins

import com.google.common.io.Resources
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class TemplateRendererTest extends Specification {

    def "should template python pod template"() {
        given:
        String templatePath = "pod-templates/python.yaml"
        String yaml = Resources.toString(Resources.getResource(templatePath), StandardCharsets.UTF_8)

        expect:
        TemplateRenderer.render(yaml, [images: [python: "python:3.8-slim"], options: [:]]).contains("image: python:3.8-slim")
        def defaultTemplate = TemplateRenderer.render(yaml, [images: [:], options: [:]])
        defaultTemplate.contains("image: python:3.7-slim")
        defaultTemplate.contains("affinity")
        println defaultTemplate
        !TemplateRenderer.render(yaml, [images: [:], options: [["disableAffinity": "true"]]]).contains("affinity")
    }
}
