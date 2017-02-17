package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import spock.lang.Specification

class ActionSpecificationResolverSpec extends Specification {
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

  def "Resolves restful and method backed actions"() {
    given:
      def sut = new MethodBackedActionSpecificationFactory(resolver, actionAttributes)
    and:
      urlMappings.urlMappings >> [otherMapping(Mock(UrlMapping))]
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, action))
    then:
      spec != null
      spec.handlerMethod.method.name == action
    where:
      action << ["index", "save", "show", "edit", "update", "delete", "patch", "create", "other"]
  }

  def otherMapping(UrlMapping urlMapping) {
    urlMapping.controllerName >> "A"
    urlMapping.actionName >> "other"
    urlMapping.httpMethod >> "POST"
    urlMapping
    urlMapping
  }
}
