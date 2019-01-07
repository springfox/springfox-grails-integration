package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.ImmutableSet;
import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;
import grails.web.mapping.UrlMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.grails.Actions.*;
import static springfox.documentation.grails.UrlMappings.*;

class GrailsActionContext {
  private final GrailsControllerClass controller;
  private final GrailsDomainClass domainClass;
  private final GrailsActionAttributes urlProvider;
  private final String action;
  private final TypeResolver resolver;
  private final Optional<UrlMapping> urlMapping;
  private final boolean isRestfulController;
  private final Set<MediaType> mediaTypes;
  private final Collection<RequestMethod> requestMethods;
  private final boolean missingMapping;

  public GrailsActionContext(
      GrailsControllerClass controller,
      GrailsDomainClass domainClass,
      GrailsActionAttributes urlProvider,
      String action,
      TypeResolver resolver) {
    this.controller = controller;
    this.domainClass = domainClass;
    this.urlProvider = urlProvider;
    this.action = action;
    this.resolver = resolver;
    this.isRestfulController = isRestfulController(controller, action);
    this.requestMethods = urlProvider.httpMethods(this);
    String httpMethod = requestMethods
        .stream()
        .findFirst()
        .map(Enum::toString)
        .orElse(null);
    this.missingMapping = requestMethods.isEmpty();
    this.urlMapping = urlProvider.urlMapping(
        selector(
            controller.getLogicalPropertyName(),
            action,
            httpMethod));
    this.mediaTypes = mediaTypeOverrides(this);
    maybeAddDefaultMediaType(mediaTypes);
  }

  private void maybeAddDefaultMediaType(Set<MediaType> mediaTypes) {
    if (mediaTypes.isEmpty()) {
      mediaTypes.add(MediaType.APPLICATION_JSON);
    }
  }

  public GrailsControllerClass getController() {
    return controller;
  }

  public GrailsDomainClass getDomainClass() {
    return domainClass;
  }

  public String getAction() {
    return action;
  }

  //@TODO this is probably where the controller name is messed up
  public String path() {
    String loweredAction = action.toLowerCase();
    return urlMapping
        .map(mapping -> urlProvider.actionUrl(this, mapping))
        .orElse(String.format("/%s/%s", controller.getLogicalPropertyName(), action));
//        .toLowerCase();
  }

  public List<ResolvedMethodParameter> pathParameters() {
    if (domainClass == null) {
      return newArrayList();
    }
    return urlMapping
        .map(u -> resolvedPathParameters(resolver, u, domainClass))
        .orElse(newArrayList());
  }

  public Collection<RequestMethod> getRequestMethods() {
    return requestMethods;
  }

  public HandlerMethod handlerMethod() {
    Map<String, HandlerMethod> actions = actionsToHandler(controller.getClazz());
    return actions.get(action);
  }

  public Set<MediaType> supportedMediaTypes() {
    return ImmutableSet.copyOf(mediaTypes);
  }

  public boolean isRestfulController() {
    return isRestfulController;
  }

  public boolean isMissingMapping() {
    return missingMapping;
  }

  private boolean isRestfulController(GrailsControllerClass controller, String action) {
    try {
      Class<?> restfulController = Class.forName("grails.rest.RestfulController");
      return restfulController.isAssignableFrom(controller.getClazz())
          && urlProvider.isRestfulAction(action);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
