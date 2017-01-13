package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

import java.util.HashSet;
import java.util.Map;

import static springfox.documentation.grails.Actions.*;

@Component
public class ActionSpecificationResolver {
  private final GrailsActionAttributes urlProvider;
  private final HandlerMethodResolver handlerMethodResolver;
  private final Map<String, ActionSpecificationFactory> restfulActions;

  @Autowired
  public ActionSpecificationResolver(TypeResolver resolver, GrailsActionAttributes urlProvider) {
    this.urlProvider = urlProvider;
    this.restfulActions = restfulActions(resolver);
    this.handlerMethodResolver = new HandlerMethodResolver(resolver);
  }

  ActionSpecification resolve(GrailsActionContext context) {
    if (isRestfulController(context.getController())) {
      return restfulActions.get(context.getAction()).create(context);
    }
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

  @SuppressWarnings({"unchecked", "squid:S1166"})
  private boolean isRestfulController(GrailsClass controllerClazz) {
    try {
      Class<?> restfulController = Class.forName("grails.rest.RestfulController");
      return restfulController.isAssignableFrom(controllerClazz.getClazz());
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
