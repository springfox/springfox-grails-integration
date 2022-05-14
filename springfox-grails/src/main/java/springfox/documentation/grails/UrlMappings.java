package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Strings;
import grails.gorm.validation.ConstrainedProperty;
import grails.web.mapping.UrlMapping;
import org.grails.datastore.mapping.model.PersistentEntity;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.grails.Parameters.pathParameter;

class UrlMappings {

    private UrlMappings() {
        throw new UnsupportedOperationException();
    }

    public static Predicate<UrlMapping> selector(
        String logicalControllerName,
        String action,
        String httpMethod) {
        return u -> httpMethodMatches(httpMethod, u)
            && controllerMatches(u, logicalControllerName)
            && actionMatches(action, u);
    }

    public static Map<String, String> pathParameters(UrlMapping mapping) {
        ConstrainedProperty[] constraints = (ConstrainedProperty[]) mapping.getConstraints();
        return IntStream.range(0, constraints.length)
            .filter(indicesToUse(mapping))
            .mapToObj(i -> constraints[i])
            .collect(Collectors.toMap(
                ConstrainedProperty::getPropertyName,
                c -> String.format("{%s}", c.getPropertyName())));
    }

    public static List<ResolvedMethodParameter> resolvedPathParameters(
        TypeResolver resolver,
        UrlMapping mapping,
        PersistentEntity domainClass) {
        ConstrainedProperty[] constraints = (ConstrainedProperty[]) mapping.getConstraints();
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

    private static IntPredicate indicesToUse(UrlMapping mapping) {
        return index -> {
            ConstrainedProperty property = (ConstrainedProperty) mapping.getConstraints()[index];
            return !property.getPropertyName().equals("controller")
                && !property.getPropertyName().equals("action")
                && !property.isNullable();
        };
    }

    private static ResolvedType resolvedPropertyType(
        TypeResolver resolver,
        PersistentEntity domainClass,
        ConstrainedProperty property) {
        if (domainClass.hasProperty(property.getPropertyName(), property.getPropertyType())) {
            return resolver.resolve(domainClass.getPropertyByName(property.getPropertyName()).getType());
        }
        return resolver.resolve(String.class);
    }

    private static boolean httpMethodMatches(String httpMethod, UrlMapping urlMapping) {
        return anyMethod(httpMethod) || Objects.equals(urlMapping.getHttpMethod(), httpMethod);
    }

    private static boolean anyMethod(String methodName) {
        return Strings.isNullOrEmpty(methodName);
    }

    private static boolean actionMatches(String context, UrlMapping urlMapping) {
        return isWildcardAction(urlMapping) || explicitAction(context, urlMapping);
    }

    private static boolean explicitAction(String action, UrlMapping urlMapping) {
        return Objects.equals(urlMapping.getActionName(), action);
    }

    private static boolean controllerMatches(UrlMapping urlMapping, String logicalControllerName) {
        return isWildcardController(urlMapping) || explicitController(urlMapping, logicalControllerName);
    }

    private static boolean explicitController(UrlMapping urlMapping, String logicalControllerName) {
        return Objects.equals(urlMapping.getControllerName(), logicalControllerName);
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
        return Arrays.stream(urlMapping.getConstraints())
            .map(ConstrainedProperty.class::cast)
            .anyMatch(c -> c.getPropertyName().equals(name));
    }
}
