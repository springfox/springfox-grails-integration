package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;

import java.util.Collections;

import static springfox.documentation.grails.Parameters.bodyParameter;

class CreateActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public CreateActionSpecificationFactory(TypeResolver resolver) {
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
        Collections.singletonList(
            bodyParameter(1, resolver.resolve(domainClass(context.getDomainClass())))),
        resolver.resolve(domainClass(context.getDomainClass())));
  }
}
