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

  public ActionSpecification resolve(GrailsActionContext context) {
    if (context.isRestfulController()) {
      return restfulActions.create(context);
    }
    return methodBackedActions.create(context);
  }
}
