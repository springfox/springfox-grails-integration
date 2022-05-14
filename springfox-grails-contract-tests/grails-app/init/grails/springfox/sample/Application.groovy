package grails.springfox.sample

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.documentation.grails.config.SpringfoxGrailsIntegrationConfiguration
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
  Docket api() {
    new Docket(DocumentationType.SWAGGER_2)
        .ignoredParameterTypes(MetaClass)
        .select()
        .paths(not(ant("/error")))
        .build()
  }

  @Bean
  static WebMvcConfigurer webConfigurer() {
    new WebMvcConfigurer() {
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