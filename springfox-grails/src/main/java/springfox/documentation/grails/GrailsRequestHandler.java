package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;
import grails.web.mapping.LinkGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.service.ResolvedMethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class GrailsRequestHandler implements RequestHandler {
  private static final Map<String, ActionSpecificationFactory> actionSpecifications = actionSpecifications();
  private final GrailsDomainClass domain;
  private final GrailsControllerClass controller;
  private final LinkGenerator linkGenerator;
  private final String action;
  private final TypeResolver resolver;
  private ActionSpecification actionSpecification;

  public GrailsRequestHandler(
      GrailsDomainClass domain,
      GrailsControllerClass controller,
      LinkGenerator linkGenerator,
      String action,
      TypeResolver resolver) {

    this.resolver = resolver;
    this.action = action;
    this.domain = domain;
    this.controller = controller;
    this.linkGenerator = linkGenerator;
    actionSpecification = actionSpecifications.get(action).create(domain);
  }

  private static LinkedHashMap<String, ActionSpecificationFactory> actionSpecifications() {
    LinkedHashMap<String, ActionSpecificationFactory> map = new LinkedHashMap<>(8);
    TypeResolver resolver = new TypeResolver();
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

  @Override
  public Class<?> declaringClass() {
    return (Class<?>) controller.getClazz();
  }

  @Override
  public boolean isAnnotatedWith(Class<? extends Annotation> annotation) {
    return AnnotationUtils.findAnnotation(declaringClass(), annotation) != null;
  }

  @Override
  public PatternsRequestCondition getPatternsCondition() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>(2);
    map.put("controller", controller.getName());
    map.put("action", action);
    map.put("params", pathParameters());
    String link = linkGenerator.link(map)
        .replace("%7B", "{").replace("%7D", "}")
        .replace(linkGenerator.getServerBaseURL(), "")
        .toLowerCase();
    return new PatternsRequestCondition(link);
  }

  private Map<String, String> pathParameters() {
    return actionSpecification
        .getParameters().stream()
        .filter(p -> p.hasParameterAnnotation(PathVariable.class))
        .filter(p -> p.defaultName().isPresent())
        .collect(
            Collectors.toMap(
                p -> p.defaultName().get(),
                p -> String.format("{%s}", p.defaultName().get())));
  }

  @Override
  public String groupName() {
    return controller.getLogicalPropertyName();
  }

  @Override
  public String getName() {
    return action;
  }

  @Override
  public Set<RequestMethod> supportedMethods() {
    return actionSpecification.getSupportedMethods();
  }

  @Override
  public Set<? extends MediaType> produces() {
    return new HashSet<>(singletonList(MediaType.APPLICATION_JSON));
  }

  @Override
  public Set<? extends MediaType> consumes() {
    return new HashSet<>(singletonList(MediaType.APPLICATION_JSON));
  }

  @Override
  public Set<NameValueExpression<String>> headers() {
    return new HashSet<>();
  }

  @Override
  public Set<NameValueExpression<String>> params() {
    return new HashSet<>();
  }

  @Override
  @SuppressWarnings("Guava")
  public <T extends Annotation> Optional<T> findAnnotation(Class<T> annotation) {
    return com.google.common.base.Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getMethod()
        , annotation));
  }

  @Override
  public RequestHandlerKey key() {
    return new RequestHandlerKey(getPatternsCondition().getPatterns(), supportedMethods(), consumes(), produces());
  }

  @Override
  public List<ResolvedMethodParameter> getParameters() {
    return new ArrayList<>(actionSpecification.getParameters());
  }

  @Override
  public ResolvedType getReturnType() {
    if (domain == null) {
      return resolver.resolve(Void.TYPE);
    }

    return resolver.resolve(domain.getClazz());
  }

  @SuppressWarnings("Guava")
  @Override
  public <T extends Annotation> Optional<T> findControllerAnnotation(Class<T> annotation) {
    return Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getBeanType(), annotation));
  }

  @Override
  public RequestMappingInfo getRequestMapping() {
    throw new UnsupportedOperationException();
  }

  @Override
  public HandlerMethod getHandlerMethod() {
    return new HandlerMethod(controller.getClazz(), methodByAction());
  }

  @Override
  public RequestHandler combine(RequestHandler other) {
    throw new UnsupportedOperationException();
  }

  private Method methodByAction() {
    return Arrays.stream(controller.getClazz().getMethods())
        .filter(m -> Objects.equals(m.getName(), action))
        .findFirst()
        .orElse(null);
  }

}
