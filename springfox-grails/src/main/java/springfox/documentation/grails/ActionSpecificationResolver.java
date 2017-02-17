package springfox.documentation.grails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ActionSpecificationResolver {
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
    if (isRestfulController(context)) {
      return restfulActions.create(context);
    }
    return methodBackedActions.create(context);
  }

  @SuppressWarnings({"unchecked", "squid:S1166"})
  private boolean isRestfulController(GrailsActionContext context) {
    try {
      Class<?> restfulController = Class.forName("grails.rest.RestfulController");
      return restfulController.isAssignableFrom(context.getController().getClazz())
          && restfulActions.canHandle(context.getAction());
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
