package springfox.documentation.grails;

import com.google.common.collect.ImmutableMap;
import grails.web.mapping.LinkGenerator;
import grails.web.mapping.UrlMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static springfox.documentation.grails.Actions.*;

@Component
class GrailsActionAttributes {
  public static Map<String, String> methodMap = ImmutableMap.<String, String>builder()
      .put("index", "GET")
      .put("show", "GET")
      .put("create", "GET")
      .put("edit", "GET")
      .put("update", "PUT")
      .put("save", "POST")
      .put("patch", "PATCH")
      .put("delete", "DELETE")
      .build();
  
  private final LinkGenerator linkGenerator;
  private final grails.web.mapping.UrlMappings urlMappings;

  @Autowired
  public GrailsActionAttributes(
      LinkGenerator linkGenerator,
      grails.web.mapping.UrlMappings urlMappings) {
    this.linkGenerator = linkGenerator;
    this.urlMappings = urlMappings;
  }

  public Set<RequestMethod> httpMethod(GrailsActionContext context) {
    Set<RequestMethod> requestMethods = methodOverrides(context);
    if (requestMethods.isEmpty()) {
      Set<RequestMethod> defaultMethods = Arrays.stream(urlMappings.getUrlMappings())
          .filter(mapping ->
              Objects.equals(mapping.getControllerName(), context.getController().getName())
                  && Objects.equals(mapping.getActionName(), context.getAction()))
          .map(mapping -> RequestMethod.valueOf(mapping.getHttpMethod()))
          .collect(Collectors.toSet());
      if (defaultMethods.isEmpty()) {
        String defaultMethod = context.isRestfulController()
                      ? methodMap.getOrDefault(context.getAction(), "POST")
                      : "POST";
        return Collections.singleton(RequestMethod.valueOf(defaultMethod));
      }
      return defaultMethods;
    }
    return requestMethods;
  }

  public boolean isRestfulAction(String action) {
    return methodMap.containsKey(action);
  }

  public Optional<UrlMapping> urlMapping(Predicate<UrlMapping> selector) {
    return Arrays.stream(urlMappings.getUrlMappings())
        .filter(selector)
        .findFirst();
  }

  public String actionUrl(GrailsActionContext context, UrlMapping mapping) {
    return mapping.createRelativeURL(
        context.getController().getLogicalPropertyName(),
        context.getAction(),
        UrlMappings.pathParameters(mapping),
        "UTF-8")
        .replace("%7B", "{").replace("%7D", "}")
        .replace(linkGenerator.getServerBaseURL(), "")
        .replace(".{format}", "")
        .toLowerCase();
  }

}
