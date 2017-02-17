package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification

class MethodBackedActionSpecificationFactorySpec extends Specification {
  def resolver = new TypeResolver()
  def controller = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)
  def identifierProperty = Mock(GrailsDomainClassProperty)
  def links = Mock(LinkGenerator)
  def urlMappings = Mock(UrlMappings)
  GrailsActionAttributes actionAttributes

  def setup() {
    controller.clazz >> AController
    controller.name >> "A"
    domain.clazz >> ADomain
    domain.identifier >> identifierProperty
    domain.identifier.type >> Integer
    actionAttributes = new GrailsActionAttributes(links, urlMappings)
  }

  def "Resolves all method backed actions"() {
    given:
      def sut = new MethodBackedActionSpecificationFactory(resolver, actionAttributes)
    and:
      urlMappings.urlMappings >> [otherMapping(Mock(UrlMapping))]
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, "other"))
    then:
      spec.consumes == [] as Set
      spec.produces == [] as Set
      spec.supportedMethods == [RequestMethod.POST] as Set
      spec.parameters.size() == 2
      spec.parameters[0].parameterType == resolver.resolve(Integer)
      spec.parameters[0].parameterIndex == 0
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "first"

      spec.parameters[1].parameterType == resolver.resolve(ADomain)
      spec.parameters[1].parameterIndex == 1
      spec.parameters[1].defaultName().isPresent()
      spec.parameters[1].defaultName().get() == "domain"
      spec.returnType == resolver.resolve(ADomain)
      spec.handlerMethod.method == AController.methods.find {it.name == "other" }
  }

  def otherMapping(UrlMapping urlMapping) {
    urlMapping.controllerName >> "A"
    urlMapping.actionName >> "other"
    urlMapping.httpMethod >> "POST"
    urlMapping
    urlMapping
  }
}
