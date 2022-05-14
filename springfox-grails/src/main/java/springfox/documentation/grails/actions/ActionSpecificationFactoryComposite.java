package springfox.documentation.grails.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;

@Component
public class ActionSpecificationFactoryComposite implements ActionSpecificationFactory {
    private final RestfulActionSpecificationFactory restfulActions;
    private final MethodBackedActionSpecificationFactory methodBackedActions;

    @Autowired
    public ActionSpecificationFactoryComposite(RestfulActionSpecificationFactory restfulActions,
                                               MethodBackedActionSpecificationFactory methodBackedActions) {
        this.restfulActions = restfulActions;
        this.methodBackedActions = methodBackedActions;
    }

    ActionSpecification resolve(GrailsActionContext context) {
        if (context.isRestfulController()) {
            return restfulActions.create(context);
        }
        return methodBackedActions.create(context);
    }

    @Override
    public ActionSpecification create(GrailsActionContext actionContext) {
        return resolve(actionContext);
    }
}
