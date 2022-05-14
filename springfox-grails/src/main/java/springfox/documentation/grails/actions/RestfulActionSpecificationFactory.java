package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RestfulActionSpecificationFactory implements ActionSpecificationFactory {
    private final Map<String, ActionSpecificationFactory> factoryLookup;

    @Autowired
    public RestfulActionSpecificationFactory(TypeResolver resolver) {
        this.factoryLookup = restfulActions(resolver);
    }

    @Override
    public ActionSpecification create(GrailsActionContext context) {
        Preconditions.checkArgument(
            canHandle(context.getAction()),
            String.format("Action %s is not a restful action", context.getAction()));

        return factoryLookup.get(context.getAction()).create(context);
    }

    public boolean canHandle(String action) {
        return factoryLookup.containsKey(action);
    }

    private Map<String, ActionSpecificationFactory> restfulActions(TypeResolver resolver) {
        LinkedHashMap<String, ActionSpecificationFactory> map = new LinkedHashMap<>(8);
        map.put("index", new IndexActionSpecificationFactory(resolver));
        map.put("show", new ShowActionSpecificationFactory(resolver));
        map.put("create", new CreateActionSpecificationFactory(resolver));
        map.put("edit", new EditActionSpecificationFactory(resolver));
        map.put("update", new UpdateActionSpecificationFactory(resolver));
        map.put("save", new SaveActionSpecificationFactory(resolver));
        map.put("patch", new PatchActionSpecificationFactory(resolver));
        map.put("delete", new DeleteActionSpecificationFactory(resolver));
        return map;
    }
}
