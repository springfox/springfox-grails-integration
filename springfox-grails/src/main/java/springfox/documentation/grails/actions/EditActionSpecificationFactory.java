package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;

class EditActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public EditActionSpecificationFactory(TypeResolver resolver) {
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
