package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;
import grails.web.mapping.LinkGenerator;
import grails.web.mapping.UrlMappings;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GrailsRequestHandlerProvider implements RequestHandlerProvider {
    private final TypeResolver resolver;
    private GrailsApplication grailsApplication;
    private LinkGenerator grailsLinkGenerator;
    private UrlMappings grailsUrlMappings;

    public GrailsRequestHandlerProvider(GrailsApplication grailsApplication, TypeResolver resolver, LinkGenerator grailsLinkGenerator, UrlMappings grailsUrlMappings) {
        this.resolver = resolver;
        this.grailsUrlMappings = grailsUrlMappings;
        this.grailsLinkGenerator = grailsLinkGenerator;
        this.grailsApplication = grailsApplication;
    }

    @Override
    public List<RequestHandler> requestHandlers() {
        final List<RequestHandler> requestHandlers = new ArrayList<RequestHandler>();
        Arrays.stream(grailsApplication.getArtefacts("Controller")).forEach(it -> {
            GrailsControllerClass controller = (GrailsControllerClass) it;
//                List<UrlMapping> mappingsForController = Arrays.stream(grailsUrlMappings.getUrlMappings())
//                        .filter(m -> m.getControllerName() == controller.getName())
//                        .collect(Collectors.toList());

            GrailsClass inferredDomain = Arrays.stream(grailsApplication.getArtefacts("Domain"))
                    .filter(d -> Objects.equals(d.getLogicalPropertyName(), controller.getLogicalPropertyName()))
                    .findFirst()
                    .orElse(null);

            requestHandlers.addAll(new DefaultRequestHandlersProvider(resolver, grailsLinkGenerator, controller, (GrailsDomainClass) inferredDomain).handlers());
        });
        return requestHandlers;
    }

    public GrailsApplication getGrailsApplication() {
        return grailsApplication;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    public LinkGenerator getGrailsLinkGenerator() {
        return grailsLinkGenerator;
    }

    public void setGrailsLinkGenerator(LinkGenerator grailsLinkGenerator) {
        this.grailsLinkGenerator = grailsLinkGenerator;
    }

    public Object getGrailsUrlMappings() {
        return grailsUrlMappings;
    }

    public void setGrailsUrlMappings(UrlMappings grailsUrlMappings) {
        this.grailsUrlMappings = grailsUrlMappings;
    }
}
