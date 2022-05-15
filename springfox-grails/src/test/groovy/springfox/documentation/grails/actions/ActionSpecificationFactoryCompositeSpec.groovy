package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import grails.web.mapping.UrlMapping
import spock.lang.Unroll
import springfox.documentation.grails.GrailsActionContext

class ActionSpecificationFactoryCompositeSpec extends ActionSpecificationFactorySpec {
    def resolver = new TypeResolver()

    @Unroll
    def "Resolves action #action"() {
        given:
        def sut = new ActionSpecificationFactoryComposite(
            new RestfulActionSpecificationFactory(resolver),
            new MethodBackedActionSpecificationFactory(resolver)
        )
        and:
        urlMappings.urlMappings >> [otherMapping(Stub(UrlMapping))]
        when:
        def spec = sut.resolve(new GrailsActionContext(controller, domain, actionAttributes, action, resolver))
        then:
        spec != null
        spec.handlerMethod.method.name == action
        where:
        action << ["index", "save", "show", "edit", "update", "delete", "patch", "create", "other"]
    }

    @Unroll
    def "Resolves action #action on non-restful controller"() {
        given:
        def sut = new ActionSpecificationFactoryComposite(
            new RestfulActionSpecificationFactory(resolver),
            new MethodBackedActionSpecificationFactory(resolver)
        )
        and:
        urlMappings.urlMappings >> [otherMapping(Stub(UrlMapping))]
        when:
        def spec = sut.resolve(new GrailsActionContext(regularController, null, actionAttributes, action, resolver))
        then:
        spec != null
        spec.handlerMethod.method.name == action
        where:
        action << ["index", "save", "show", "edit", "update", "delete", "create"]
    }

    def otherMapping(UrlMapping urlMapping) {
        urlMapping.controllerName >> "A"
        urlMapping.actionName >> "other"
        urlMapping.httpMethod >> "POST"
        urlMapping
        urlMapping
    }
}
