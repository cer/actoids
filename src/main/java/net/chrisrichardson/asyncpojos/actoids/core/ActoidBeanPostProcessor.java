package net.chrisrichardson.asyncpojos.actoids.core;

import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

public class ActoidBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  private ActoidSystem actoidSystem;
  
  public void setActoidSystem(ActoidSystem actoidSystem) {
    this.actoidSystem = actoidSystem;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Actoid actoidAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), Actoid.class);
    if (actoidAnnotation == null)
      return bean;
    return actoidSystem.makeActoid(bean);
  }

}
