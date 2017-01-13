package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static springfox.documentation.grails.Actions.*;

@Component
public class IndexActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  @Autowired
  public IndexActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    Map<String, HandlerMethod> actions = actionsToHandler(context.getController().getClazz());
    HandlerMethod handlerMethod = actions.get(context.getAction());
    return new ActionSpecification(
        new HashSet<>(Collections.singletonList(RequestMethod.GET)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        handlerMethod,
        new ArrayList<>(Collections.singletonList(
            queryParameter(
                1,
                "max",
                resolver.resolve(Integer.class),
                false,
                ""))),
        resolver.resolve(List.class, domainClass(context.getDomainClass())));

  }
}
