package net.chrisrichardson.asyncpojos.actoids.xsd;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ActoidsNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("config", new ConfigElementParser());
    }
}