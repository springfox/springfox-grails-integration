package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

import javax.servlet.ServletContext;
import java.util.List;

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

  @Bean
  DocumentationPluginsBootstrapper documentationPluginsBootstrapper(
          DocumentationPluginsManager documentationPluginsManager,
          List<RequestHandlerProvider> handlerProviders,
          DocumentationCache scanned,
          ApiDocumentationScanner resourceListing,
          TypeResolver typeResolver,
          Defaults defaults,
          ServletContext servletContext) {

    return new SwaggerDocumentationPluginsBootstrapper(
            documentationPluginsManager,
            handlerProviders,
            scanned,
            resourceListing,
            typeResolver,
            defaults,
            servletContext);
  }
}
