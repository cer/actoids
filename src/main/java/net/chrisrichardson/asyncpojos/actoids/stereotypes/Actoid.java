package net.chrisrichardson.asyncpojos.actoids.stereotypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Actoid {
}
