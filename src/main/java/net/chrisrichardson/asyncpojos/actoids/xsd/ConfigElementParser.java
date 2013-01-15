package net.chrisrichardson.asyncpojos.actoids.xsd;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidBeanPostProcessor;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidContextThreadLocalHolder;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidSystem;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ConfigElementParser implements BeanDefinitionParser {
  
  @Override
  public BeanDefinition parse(Element element, ParserContext parserContext) {
    String defaultExecutorBeanName = element.getAttribute("default-executor");

    BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ActoidContextThreadLocalHolder.class);
    parserContext.getRegistry().registerBeanDefinition("actoid-context-holder", bdb.getBeanDefinition());

    bdb = BeanDefinitionBuilder.rootBeanDefinition(ActoidSystem.class);
    bdb.addPropertyReference("executorService", defaultExecutorBeanName);
    bdb.addPropertyReference("actoidContextThreadLocalHolder", "actoid-context-holder");
    parserContext.getRegistry().registerBeanDefinition("actoidSystem", bdb.getBeanDefinition());

    bdb = BeanDefinitionBuilder.rootBeanDefinition(ActoidBeanPostProcessor.class);
    bdb.addPropertyReference("actoidSystem", "actoidSystem");
    parserContext.getRegistry().registerBeanDefinition("actoidBeanPostProcessor", bdb.getBeanDefinition());
    return null;

  }
}
