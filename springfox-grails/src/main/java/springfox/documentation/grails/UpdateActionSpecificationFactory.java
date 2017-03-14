package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static springfox.documentation.grails.Parameters.*;

class UpdateActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  UpdateActionSpecificationFactory(TypeResolver resolver) {
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
        new ArrayList<>(Arrays.asList(
            pathParameter(1, "id", resolver.resolve(idType(context.getDomainClass()))),
            bodyParameter(2, resolver.resolve(domainClass(context.getDomainClass()))))),
        resolver.resolve(domainClass(context.getDomainClass())));

  }
}
