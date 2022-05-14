package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import grails.core.GrailsControllerClass;
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
    private final ActionSpecificationFactory actionSpecificationFactoryComposite;

    @Autowired
    public GrailsRequestHandlerProvider(TypeResolver resolver,
                                        GrailsApplication grailsApplication,
                                        GrailsActionAttributes urlProvider,
                                        ActionSpecificationFactory actionSpecificationFactoryComposite) {
        this.resolver = resolver;
        this.urlProvider = urlProvider;
        this.grailsApplication = grailsApplication;
        this.actionSpecificationFactoryComposite = actionSpecificationFactoryComposite;
    }

    @Override
    public List<RequestHandler> requestHandlers() {
        return Arrays.stream(grailsApplication.getArtefacts("Controller"))
            .map(GrailsControllerClass.class::cast)
            .flatMap(this::fromGrailsAction)
            .collect(Collectors.toList());
    }

    private Stream<? extends RequestHandler> fromGrailsAction(GrailsControllerClass controller) {
        PersistentEntity inferredDomain = grailsApplication.getMappingContext().getPersistentEntities()
            .stream()
            .filter(p -> Objects.equals(p.getDecapitalizedName(), controller.getLogicalPropertyName()))
            .findFirst()
            .orElse(null);
        return controller.getActions()
            .stream()
            .map(action -> {
                GrailsActionContext actionContext = new GrailsActionContext(
                    controller,
                    inferredDomain,
                    urlProvider,
                    action,
                    resolver);
                return new GrailsRequestHandler(actionContext,
                    actionSpecificationFactoryComposite.create(actionContext));
            })
            .filter(handler -> !handler.supportedMethods().isEmpty());
    }


}
