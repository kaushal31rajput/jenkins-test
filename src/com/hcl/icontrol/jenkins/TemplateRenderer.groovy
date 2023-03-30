package com.hcl.icontrol.jenkins

import groovy.text.StreamingTemplateEngine

class TemplateRenderer {

    static def render(String templateText, Map variables) {
        def engine = new StreamingTemplateEngine()
        return engine.createTemplate(templateText).make(variables).toString()
    }
}
