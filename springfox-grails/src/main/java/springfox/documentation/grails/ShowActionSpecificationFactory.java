package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;

import java.util.Collections;

class ShowActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public ShowActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    return new ActionSpecification(
        context.path(),
        Collections.singleton(context.getRequestMethod()),
        context.supportedMediaTypes(),
        context.supportedMediaTypes(),
        context.handlerMethod(),
        context.pathParameters(),
        resolver.resolve(domainClass(context.getDomainClass())));
  }
}
