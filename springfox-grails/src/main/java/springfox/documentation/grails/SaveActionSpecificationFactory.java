package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static com.google.common.collect.Sets.*;
import static springfox.documentation.grails.Actions.*;

class SaveActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  SaveActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    Map<String, HandlerMethod> actions = actionsToHandler(context.getController().getClazz());
    HandlerMethod handlerMethod = actions.get(context.getAction());
    return new ActionSpecification(
        methodOverrides(context, newHashSet(RequestMethod.POST, RequestMethod.PUT)),
        new HashSet<>(producesOverrides(context)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        handlerMethod,
        new ArrayList<>(Arrays.asList(
            pathParameter(1, "id", resolver.resolve(idType(context.getDomainClass()))),
            bodyParameter(2, resolver.resolve(domainClass(context.getDomainClass()))))),
        resolver.resolve(domainClass(context.getDomainClass())));

  }
}
