package springfox.documentation.grails;

import com.google.common.collect.ImmutableMap;
import grails.web.mapping.LinkGenerator;
import grails.web.mapping.UrlMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.builders.BuilderDefaults.*;
import static springfox.documentation.grails.Actions.*;

@Component
class GrailsActionAttributes {
  private static final List<RequestMethod> ALL_HTTP_METHODS = newArrayList(
      RequestMethod.GET,
      RequestMethod.POST,
      RequestMethod.PUT,
      RequestMethod.PATCH,
      RequestMethod.DELETE,
      RequestMethod.HEAD,
      RequestMethod.OPTIONS,
      RequestMethod.TRACE);

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

  public Collection<RequestMethod> httpMethods(GrailsActionContext context) {
    Set<RequestMethod> requestMethods = methodOverrides(context);
    if (requestMethods.isEmpty()) {
      Stream<UrlMapping> sorted = Arrays.stream(urlMappings.getUrlMappings())
          .filter(UrlMappings.selector(context, null))
          .sorted(Comparator.comparing(this::score));
      return sorted.findFirst()
          .map(m -> httpMethods(defaultIfAbsent(m.getHttpMethod(), "*")))
          .orElse(Collections.singleton(RequestMethod.OPTIONS));
    }
    return requestMethods;
  }

  private Integer score(UrlMapping mapping) {
    String method = defaultIfAbsent(mapping.getHttpMethod(), "*");
    String action = Optional.ofNullable(mapping.getActionName())
        .map(Object::toString)
        .orElse("*");

    int methodScore = method.equals("*") ? 1000 : 0;
    int actionScore = action.equals("*") ? 1000 : 0;
    return (actionScore * 10) + methodScore;
  }

  private Collection<RequestMethod> httpMethods(String httpMethod) {
    if (Objects.equals(httpMethod, "*")) {
      return ALL_HTTP_METHODS;
    }
    return Collections.singleton(RequestMethod.valueOf(httpMethod));
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
        .toLowerCase();
  }

}
