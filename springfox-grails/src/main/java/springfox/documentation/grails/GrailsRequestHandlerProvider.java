package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import grails.core.GrailsControllerClass;
import org.grails.datastore.mapping.model.MappingContext;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class GrailsRequestHandlerProvider implements RequestHandlerProvider {


  private final TypeResolver resolver;
  private final GrailsActionAttributes urlProvider;
  private final GrailsApplication grailsApplication;
  private final ActionSpecificationResolver actionResolver;


  @Autowired
  public GrailsRequestHandlerProvider(
      TypeResolver resolver,
      GrailsApplication grailsApplication,
      GrailsActionAttributes urlProvider,
      ActionSpecificationResolver actionResolver) {
    this.resolver = resolver;
    this.urlProvider = urlProvider;
    this.grailsApplication = grailsApplication;
    this.actionResolver = actionResolver;
    grailsApplication.initialise();
  }

  @Override
  public List<RequestHandler> requestHandlers() {
    return Arrays.stream(grailsApplication.getArtefacts("Controller"))
        .flatMap(this::fromGrailsAction)
        .collect(Collectors.toList());
  }

  private Stream<? extends RequestHandler> fromGrailsAction(GrailsClass grailsClass) {
    PersistentEntity inferredDomain = grailsApplication.getMappingContext()
        .getPersistentEntities().stream()
        .filter(d -> Objects.equals(d.getName(), grailsClass.getLogicalPropertyName()))
        .findFirst()
        .orElse(null);
    GrailsControllerClass controller = (GrailsControllerClass) grailsClass;
    return controller.getActions()
        .stream()
        .map(action -> {
          GrailsActionContext actionContext = new GrailsActionContext(
              controller,
              inferredDomain,
              urlProvider,
              action,
              resolver);
          return new GrailsRequestHandler(
              actionContext,
              actionResolver.resolve(actionContext)
          );
        })
        .filter(handler -> !handler.supportedMethods().isEmpty());
  }


}
