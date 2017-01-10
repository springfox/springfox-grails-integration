package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import grails.web.mapping.LinkGenerator;
import grails.web.mapping.UrlMappings;
import org.grails.web.mapping.mvc.UrlMappingsHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.service.RequestHandlerProvider;

import java.util.List;

@Configuration
public class GrailsConfiguration {
  private final GrailsApplication grailsApplication;
  private final LinkGenerator grailsLinkGenerator;
  private final UrlMappings grailsUrlMappingsHolder;
  private final List<UrlMappingsHandlerMapping> urlMappingsHandlerMappingList;
  private final TypeResolver resolver;

  @Autowired
  public GrailsConfiguration(
      GrailsApplication grailsApplication,
      LinkGenerator grailsLinkGenerator,
      UrlMappings grailsUrlMappingsHolder,
      List<UrlMappingsHandlerMapping> urlMappingsHandlerMappingList,
      TypeResolver resolver) {
    this.grailsApplication = grailsApplication;
    this.grailsLinkGenerator = grailsLinkGenerator;
    this.grailsUrlMappingsHolder = grailsUrlMappingsHolder;
    this.urlMappingsHandlerMappingList = urlMappingsHandlerMappingList;
    this.resolver = resolver;
  }

  @Bean
  public RequestHandlerProvider grailsProvider() {
    return new GrailsRequestHandlerProvider(grailsApplication, resolver, grailsLinkGenerator, grailsUrlMappingsHolder);
  }

  public GrailsApplication getGrailsApplication() {
    return grailsApplication;
  }

  public LinkGenerator getGrailsLinkGenerator() {
    return grailsLinkGenerator;
  }

  public UrlMappings getGrailsUrlMappingsHolder() {
    return grailsUrlMappingsHolder;
  }

  public List<UrlMappingsHandlerMapping> getUrlMappingsHandlerMappingList() {
    return urlMappingsHandlerMappingList;
  }


  public TypeResolver getResolver() {
    return resolver;
  }
}
