package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;

class DeleteActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public DeleteActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    return new ActionSpecification(
        context.path(),
        context.getRequestMethods(),
        context.supportedMediaTypes(),
        context.supportedMediaTypes(),
        context.handlerMethod(),
        context.pathParameters(),
        resolver.resolve(domainClass(context.getDomainClass())));

  }
}
