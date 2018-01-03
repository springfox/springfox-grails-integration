package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spring.web.plugins.CombinedRequestHandler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.*;

class GrailsRequestHandler implements RequestHandler {
  private final GrailsActionContext actionContext;
  private final ActionSpecification actionSpecification;

  GrailsRequestHandler(
      GrailsActionContext actionContext,
      ActionSpecification actionSpecification) {
    this.actionContext = actionContext;
    this.actionSpecification = actionSpecification;
  }

  @Override
  public Class<?> declaringClass() {
    return actionContext.getController().getClazz();
  }

  @Override
  public boolean isAnnotatedWith(Class<? extends Annotation> annotation) {
    return findAnnotation(annotation).isPresent();
  }

  @Override
  public PatternsRequestCondition getPatternsCondition() {
    return new PatternsRequestCondition(actionSpecification.getPath());
  }

  @Override
  public String groupName() {
    return actionContext.getController().getLogicalPropertyName();
  }

  @Override
  public String getName() {
    return java.util.Optional.ofNullable(actionContext.getDomainClass())
        .map(domain -> String.format(
            "%s%s",
            actionContext.getAction(),
            capitalize(domain.getName())))
        .orElse(actionContext.getAction());
  }

  @Override
  public Set<RequestMethod> supportedMethods() {
    return ImmutableSet.copyOf(actionSpecification.getSupportedMethods());
  }

  @Override
  public Set<? extends MediaType> produces() {
    return actionSpecification.getProduces();
  }

  @Override
  public Set<? extends MediaType> consumes() {
    return actionSpecification.getConsumes();
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
    return Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getMethod(), annotation));
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
    return actionSpecification.getReturnType();
  }

  @SuppressWarnings("Guava")
  @Override
  public <T extends Annotation> Optional<T> findControllerAnnotation(Class<T> annotation) {
    return Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getBeanType(), annotation));
  }

  @Override
  public HandlerMethod getHandlerMethod() {
    return actionSpecification.getHandlerMethod();
  }

  @Override
  public RequestMappingInfo getRequestMapping() {
    throw new UnsupportedOperationException();
  }

  @Override
  public RequestHandler combine(RequestHandler other) {
    return new CombinedRequestHandler(this, other);
  }

}
