package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsApplication
import grails.core.GrailsControllerClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import spock.lang.Specification
import springfox.documentation.grails.actions.ActionSpecificationFactoryComposite
import springfox.documentation.grails.actions.MethodBackedActionSpecificationFactory
import springfox.documentation.grails.actions.RestfulActionSpecificationFactory
import springfox.documentation.grails.doubles.Book
import springfox.documentation.grails.doubles.BookController

class GrailsRequestHandlerProviderSpec extends Specification implements UrlMappingSupport {
    def "Integration test"() {
        given:
        def resolver = new TypeResolver()
        def attributes = attributes()
        def application = application()
        and:
        def sut = new GrailsRequestHandlerProvider(resolver,
                                                   application,
                                                   attributes,
                                                   new ActionSpecificationFactoryComposite(
                                                       new RestfulActionSpecificationFactory(resolver),
                                                       new MethodBackedActionSpecificationFactory(resolver)
                                                   )
        )
        expect:
        sut.requestHandlers().size() == 1
    }

    UrlMappings urlMappingsHolder() {
        def mappings = Mock(UrlMappings)
        mappings.urlMappings >> urlMappings()
        mappings
    }

    def mapping() {
        def mapping = Mock(UrlMapping)
        mapping.actionName >> "save"
        mapping.httpMethod >> "POST"
        mapping.controllerName >> "BookController"
        mapping
    }

    LinkGenerator linkGenerator() {
        def links = Mock(LinkGenerator)
        links.link(_) >> "/test"
        links.serverBaseURL >> "http://localhost:8080"
        links
    }

    GrailsActionAttributes attributes() {
        new GrailsActionAttributes(
            linkGenerator(),
            urlMappingsHolder())
    }

    GrailsApplication application() {
        def application = Mock(GrailsApplication)
        application.getArtefacts("Controller") >> [bookController()]
        MappingContext mappingContext = Mock(MappingContext)
        application.getMappingContext() >> mappingContext
        mappingContext.getPersistentEntities() >> [bookDomain()]
        application
    }

    def bookDomain() {
        def domain = Mock(PersistentEntity)
        domain.name >> "Book"
        domain.decapitalizedName >> "book"
        domain.javaClass >> Book
        domain
    }

    def bookController() {
        def controller = Mock(GrailsControllerClass)
        controller.name >> "BookController"
        controller.logicalPropertyName >> "Book"
        controller.clazz >> BookController
        controller.actions >> ["save"]
        controller
    }
}
