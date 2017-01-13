package springfox.documentation.grails;

import grails.core.GrailsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionSpecificationResolver {
  private final RestfulActionSpecificationFactory restfulActions;
  private final MethodBackedActionSpecificationFactory methodBackedActions;

  @Autowired
  public ActionSpecificationResolver(
      RestfulActionSpecificationFactory restfulActions,
      MethodBackedActionSpecificationFactory methodBackedActions) {
    this.restfulActions = restfulActions;
    this.methodBackedActions = methodBackedActions;
  }

  ActionSpecification resolve(GrailsActionContext context) {
    if (isRestfulController(context.getController())) {
      return restfulActions.create(context);
    }
    return methodBackedActions.create(context);
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
