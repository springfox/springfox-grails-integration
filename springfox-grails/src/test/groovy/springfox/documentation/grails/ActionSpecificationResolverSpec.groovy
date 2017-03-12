package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import spock.lang.Specification
import spock.lang.Unroll

class ActionSpecificationResolverSpec extends Specification {
  def resolver = new TypeResolver()
  def restfulController = Mock(GrailsControllerClass)
  def regularController = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)
  def identifierProperty = Mock(GrailsDomainClassProperty)
  def links = Mock(LinkGenerator)
  def urlMappings = Mock(UrlMappings)
  GrailsActionAttributes actionAttributes

  def setup() {
    regularController.clazz >> BookController
    regularController.name >> "Book"
    restfulController.clazz >> AController
    restfulController.name >> "A"
    domain.clazz >> ADomain
    domain.identifier >> identifierProperty
    domain.identifier.type >> Integer
    actionAttributes = new GrailsActionAttributes(links, urlMappings)
  }

  @Unroll
  def "Resolves action #action"() {
    given:
      def sut = new ActionSpecificationResolver(
          new RestfulActionSpecificationFactory(resolver),
          new MethodBackedActionSpecificationFactory(resolver, actionAttributes))
    and:
      urlMappings.urlMappings >> [otherMapping(Mock(UrlMapping))]
    when:
      def spec = sut.resolve(new GrailsActionContext(restfulController, domain, actionAttributes, action))
    then:
      spec != null
      spec.handlerMethod.method.name == action
    where:
      action << ["index", "save", "show", "edit", "update", "delete", "patch", "create", "other"]
  }

  @Unroll
  def "Resolves action #action on non-restful controller"() {
    given:
      def sut = new ActionSpecificationResolver(
          new RestfulActionSpecificationFactory(resolver),
          new MethodBackedActionSpecificationFactory(resolver, actionAttributes))
    and:
      urlMappings.urlMappings >> [otherMapping(Mock(UrlMapping))]
    when:
      def spec = sut.resolve(new GrailsActionContext(regularController, null, actionAttributes, action))
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
