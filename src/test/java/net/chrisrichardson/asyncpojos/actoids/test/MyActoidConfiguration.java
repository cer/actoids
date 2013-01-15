package net.chrisrichardson.asyncpojos.actoids.test;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidSystem;
import net.chrisrichardson.asyncpojos.actoids.pooled.PooledActoidFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyActoidConfiguration {

    @Bean
    public Worker pooledWorker(ActoidSystem actoidSystem) {
      PooledActoidFactory<Worker> paf = new PooledActoidFactory<Worker>(Worker.class, actoidSystem, "workerImpl", 10);
      return paf.make();
    }
}
