package springfox.documentation.grails.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.grails.definitions.DefaultGrailsPropertySelector;
import springfox.documentation.grails.definitions.DefaultGrailsPropertyTransformer;
import springfox.documentation.grails.definitions.GeneratedClassNamingStrategy;
import springfox.documentation.grails.definitions.GrailsPropertySelector;
import springfox.documentation.grails.definitions.GrailsPropertyTransformer;
import springfox.documentation.grails.naming.DefaultGeneratedClassNamingStrategy;

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
    public GeneratedClassNamingStrategy namingStrategy() {
        return new DefaultGeneratedClassNamingStrategy();
    }
}
