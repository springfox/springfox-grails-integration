package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;

import java.util.Collections;

import static springfox.documentation.grails.Parameters.*;

class DeleteActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public DeleteActionSpecificationFactory(TypeResolver resolver) {
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
        Collections.singletonList(
            pathParameter(
                1,
                "id",
                resolver.resolve(idType(context.getDomainClass())))),
        resolver.resolve(domainClass(context.getDomainClass())));

  }
}
