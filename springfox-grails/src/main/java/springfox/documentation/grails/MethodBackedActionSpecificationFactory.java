package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

import java.util.HashSet;
import java.util.Map;

import static springfox.documentation.grails.Actions.*;

@Component
class MethodBackedActionSpecificationFactory implements ActionSpecificationFactory {

  private final GrailsActionAttributes urlProvider;
  private final HandlerMethodResolver handlerMethodResolver;

  @Autowired
  public MethodBackedActionSpecificationFactory(TypeResolver resolver, GrailsActionAttributes urlProvider) {
    this.urlProvider = urlProvider;
    this.handlerMethodResolver = new HandlerMethodResolver(resolver);
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    Map<String, HandlerMethod> actions = actionsToHandler(context.getController().getClazz());
    HandlerMethod handlerMethod = actions.get(context.getAction());
    return new ActionSpecification(
        urlProvider.httpMethod(context),
        new HashSet<>(),
        new HashSet<>(),
        handlerMethod,
        handlerMethodResolver.methodParameters(handlerMethod),
        handlerMethodResolver.methodReturnType(handlerMethod)
    );
  }
}
