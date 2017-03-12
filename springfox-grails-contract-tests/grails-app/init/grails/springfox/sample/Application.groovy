package grails.springfox.sample

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.documentation.grails.DefaultGrailsPropertySelector
import springfox.documentation.grails.DefaultGrailsPropertyTransformer
import springfox.documentation.grails.DefaultGeneratedClassNamingStrategy
import springfox.documentation.grails.GrailsPropertySelector
import springfox.documentation.grails.GrailsPropertyTransformer
import springfox.documentation.grails.GeneratedClassNamingStrategy
import springfox.documentation.grails.SpringfoxGrailsIntegrationConfiguration
import springfox.documentation.schema.AlternateTypeRule
import springfox.documentation.schema.AlternateTypeRuleConvention
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import static com.google.common.base.Predicates.*
import static springfox.documentation.builders.PathSelectors.*

@EnableSwagger2
@Import([SpringfoxGrailsIntegrationConfiguration])
class Application extends GrailsAutoConfiguration {
  static void main(String[] args) {
    GrailsApp.run(Application, args)
  }

  @Bean
  @ConditionalOnMissingBean(GrailsPropertySelector)
  GrailsPropertySelector propertySelector() {
    new DefaultGrailsPropertySelector()
  }

  @Bean
  @ConditionalOnMissingBean(GrailsPropertyTransformer)
  GrailsPropertyTransformer propertyTransformer() {
    new DefaultGrailsPropertyTransformer()
  }

  @Bean
  @ConditionalOnMissingBean(GeneratedClassNamingStrategy)
  GeneratedClassNamingStrategy namingStrategy() {
    new DefaultGeneratedClassNamingStrategy()
  }

  @Bean
  Docket api(List<AlternateTypeRuleConvention> conventions) {
    def typeRules = conventions.collectMany {
      it.rules()
    }
    new Docket(DocumentationType.SWAGGER_2)
        .ignoredParameterTypes(MetaClass)
        .select()
        .paths(not(ant("/error")))
        .build()
        .alternateTypeRules(typeRules.toArray(new AlternateTypeRule[typeRules.size()]))
  }

  @Bean
  static WebMvcConfigurerAdapter webConfigurer() {
    new WebMvcConfigurerAdapter() {
      @Override
      void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
          registry
              .addResourceHandler("/webjars/**")
              .addResourceLocations("classpath:/META-INF/resources/webjars/")
        }
        if (!registry.hasMappingForPattern("/swagger-ui.html")) {
          registry
              .addResourceHandler("/swagger-ui.html")
              .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html")
        }
      }
    }
  }
}