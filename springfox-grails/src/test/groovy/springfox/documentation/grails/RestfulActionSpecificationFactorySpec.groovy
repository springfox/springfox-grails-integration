package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.web.mapping.LinkGenerator
import spock.lang.Specification

class RestfulActionSpecificationFactorySpec extends Specification implements UrlMappingSupport {
  def controller = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)
  def identifierProperty = Mock(GrailsDomainClassProperty)
  def urlMappings = Mock(grails.web.mapping.UrlMappings)
  def links = Mock(LinkGenerator)
  def actionAttributes = new GrailsActionAttributes(links, urlMappings)

  def setup() {
    controller.clazz >> AController
    controller.logicalPropertyName >> "A"
    domain.clazz >> ADomain
    domain.identifier >> identifierProperty
    domain.identifier.type >> Integer
    domain.getPropertyByName(_) >> {args -> property(args[0])}
    domain.hasProperty(_) >> {args -> "format" != args[0]}
    urlMappings.urlMappings >> urlMappings()
    links.getServerBaseURL() >> "http://localhost:8080"
  }

  def property(name) {
    def mock = Mock(GrailsDomainClassProperty)
    mock.name >> name
    mock.type >> (ADomain.declaredFields.find { it.name == name }?.type ?: String)
    mock
  }

  def "Resolves all restful actions"() {
    given:
      def resolver = new TypeResolver()
      def sut = new RestfulActionSpecificationFactory(resolver)
    when:
      def actionSpec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, action.toLowerCase(), resolver))
    then:
      actionSpec.handlerMethod.method.name == action
    where:
      action << ["index", "save", "show", "edit", "update", "delete", "patch", "create"]
  }

  def "Resolves ONLY restful actions"() {
    given:
      def resolver = new TypeResolver()
      def sut = new RestfulActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      def exception = thrown(IllegalArgumentException)
      exception.message.contains("Action unknown is not a restful action")
  }
}
