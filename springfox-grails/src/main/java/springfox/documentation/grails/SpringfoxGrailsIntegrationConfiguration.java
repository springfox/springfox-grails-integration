package springfox.documentation.grails;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "springfox.documentation.grails")
public class SpringfoxGrailsIntegrationConfiguration {

  @Bean
  @ConditionalOnMissingBean(GrailsPropertySelector.class)
  public GrailsPropertySelector propertySelector() {
    return new DefaultGrailsPropertySelector();
  }

  @Bean
  @ConditionalOnMissingBean(GrailsPropertyTransformer.class)
  public GrailsPropertyTransformer propertyTransformer() {
    return new DefaultGrailsPropertyTransformer();
  }

  @Bean
  @ConditionalOnMissingBean(GeneratedClassNamingStrategy.class)
  GeneratedClassNamingStrategy namingStrategy() {
    return new DefaultGeneratedClassNamingStrategy();
  }
}
