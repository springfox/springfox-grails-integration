package springfox.documentation.grails;


import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;
import grails.web.mapping.LinkGenerator;
import springfox.documentation.RequestHandler;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultRequestHandlersProvider {
  private GrailsDomainClass domain;
  private GrailsControllerClass controller;
  private LinkGenerator linkGenerator;
  private TypeResolver resolver;

  public DefaultRequestHandlersProvider(TypeResolver resolver, LinkGenerator linkGenerator, GrailsControllerClass
      controller, GrailsDomainClass domain) {
    this.resolver = resolver;
    this.domain = domain;
    this.controller = controller;
    this.linkGenerator = linkGenerator;
  }

  public List<RequestHandler> handlers() {
    return controller.getActions()
        .stream()
        .map(it -> new GrailsRequestHandler(domain, controller, linkGenerator, it, resolver))
        .collect(Collectors.toList());
  }

  public GrailsDomainClass getDomain() {
    return domain;
  }

  public void setDomain(GrailsDomainClass domain) {
    this.domain = domain;
  }

  public GrailsControllerClass getController() {
    return controller;
  }

  public void setController(GrailsControllerClass controller) {
    this.controller = controller;
  }

  public LinkGenerator getLinkGenerator() {
    return linkGenerator;
  }

  public void setLinkGenerator(LinkGenerator linkGenerator) {
    this.linkGenerator = linkGenerator;
  }

  public TypeResolver getResolver() {
    return resolver;
  }

  public void setResolver(TypeResolver resolver) {
    this.resolver = resolver;
  }
}
