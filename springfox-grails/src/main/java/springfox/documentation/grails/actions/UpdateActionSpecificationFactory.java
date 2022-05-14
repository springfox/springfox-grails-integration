package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.grails.Parameters.bodyParameter;

class UpdateActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  UpdateActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    List<ResolvedMethodParameter> parameters = new ArrayList<>(context.pathParameters());
    parameters.add(bodyParameter(
        parameters.size(),
        resolver.resolve(domainClass(context.getDomainClass()))));
    return new ActionSpecification(
        context.path(),
        context.getRequestMethods(),
        context.supportedMediaTypes(),
        context.supportedMediaTypes(),
        context.handlerMethod(),
        parameters,
        resolver.resolve(domainClass(context.getDomainClass())));

  }
}
