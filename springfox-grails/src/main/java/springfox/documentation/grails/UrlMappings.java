package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Strings;
import grails.core.GrailsDomainClass;
import grails.validation.ConstrainedProperty;
import grails.web.mapping.UrlMapping;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.grails.Parameters.pathParameter;

class UrlMappings {

  private UrlMappings() {
    throw new UnsupportedOperationException();
  }

  public static Predicate<UrlMapping> selector(GrailsActionContext context, String methodName) {
    return u -> httpMethodMatches(methodName, u)
        && controllerMatches(context, u)
        && actionMatches(context, u);
  }

  public static Map<String, String> pathParameters(UrlMapping mapping) {
    ConstrainedProperty[] constraints = mapping.getConstraints();
    return IntStream.range(0, constraints.length)
        .filter(indicesToUse(mapping))
        .mapToObj(i -> constraints[i])
        .collect(Collectors.toMap(
            ConstrainedProperty::getPropertyName,
            c -> String.format("{%s}", c.getPropertyName())));
  }

  private static IntPredicate indicesToUse(UrlMapping mapping) {
    return index -> {
      ConstrainedProperty property = mapping.getConstraints()[index];
      return !property.getPropertyName().equals("controller")
          && !property.getPropertyName().equals("action");
    };
  }

  public static List<ResolvedMethodParameter> resolvedPathParameters(
      TypeResolver resolver,
      UrlMapping mapping,
      GrailsDomainClass domainClass) {
    ConstrainedProperty[] constraints = mapping.getConstraints();
    List<ConstrainedProperty> pathProperties = IntStream.range(0, constraints.length)
        .filter(indicesToUse(mapping))
        .mapToObj(i -> constraints[i])
        .collect(Collectors.toList());
    List<ResolvedMethodParameter> resolved = newArrayList();
    for (int index = 0; index < pathProperties.size(); index++) {
      resolved.add(
          pathParameter(
              index,
              pathProperties.get(index).getPropertyName(),
              resolvedPropertyType(resolver, domainClass, pathProperties.get(index))));
    }

    return resolved;
  }

  private static ResolvedType resolvedPropertyType(
      TypeResolver resolver,
      GrailsDomainClass domainClass,
      ConstrainedProperty property) {
    if (domainClass.hasProperty(property.getPropertyName())) {
      return resolver.resolve(domainClass.getPropertyByName(property.getPropertyName()).getType());
    }
    return resolver.resolve(String.class);
  }

  private static boolean httpMethodMatches(String methodName, UrlMapping urlMapping) {
    return anyMethod(methodName) || Objects.equals(urlMapping.getHttpMethod(), methodName);
  }

  private static boolean anyMethod(String methodName) {
    return Strings.isNullOrEmpty(methodName);
  }

  private static boolean actionMatches(GrailsActionContext context, UrlMapping urlMapping) {
    return isWildcardAction(urlMapping) || explicitAction(context, urlMapping);
  }

  private static boolean explicitAction(GrailsActionContext context, UrlMapping urlMapping) {
    return Objects.equals(urlMapping.getActionName(), context.getAction());
  }

  private static boolean controllerMatches(GrailsActionContext context, UrlMapping urlMapping) {
    return isWildcardController(urlMapping) || explicitController(urlMapping, context);
  }

  private static boolean explicitController(UrlMapping urlMapping, GrailsActionContext context) {
    return Objects.equals(urlMapping.getControllerName(), context.getController().getLogicalPropertyName());
  }

  private static boolean isWildcardController(UrlMapping urlMapping) {
    return urlMapping.getControllerName() == null
        && hasControllerConstraint(urlMapping, "controller");
  }

  private static boolean isWildcardAction(UrlMapping urlMapping) {
    return urlMapping.getControllerName() == null
        && hasControllerConstraint(urlMapping, "action");
  }

  private static boolean hasControllerConstraint(UrlMapping urlMapping, String name) {
    return !Arrays.stream(urlMapping.getConstraints())
        .filter(c -> c.getPropertyName().equals(name))
        .collect(Collectors.toList()).isEmpty();
  }
}
