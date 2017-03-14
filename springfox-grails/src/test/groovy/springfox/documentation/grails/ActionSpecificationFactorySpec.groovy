package springfox.documentation.grails

import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import org.grails.web.mapping.DefaultLinkGenerator
import org.grails.web.mapping.DefaultUrlMappingsHolder
import spock.lang.Specification

class ActionSpecificationFactorySpec extends Specification implements UrlMappingSupport {
  def controller
  def domain
  def links
  def urlMappings
  def actionAttributes
  def regularController

  def setup() {
    urlMappings = new DefaultUrlMappingsHolder(urlMappings())
    links = new DefaultLinkGenerator("http://localhost:8080", "")
    links.urlMappingsHolder = urlMappings
    actionAttributes = new GrailsActionAttributes(links, urlMappings)

    domain = mockDomain()
    controller = mockController()
    regularController = mockRegularController()
  }

  GrailsControllerClass mockRegularController() {
    def regularController = Mock(GrailsControllerClass)
    regularController.clazz >> BookController
    regularController.name >> "Book"
    regularController.logicalPropertyName >> "Book"
    regularController
  }

  def mockController() {
    def controller = Mock(GrailsControllerClass)
    controller.clazz >> AController
    controller.name >> "AController"
    controller.logicalPropertyName >> "A"
    controller
  }

  def mockDomain() {
    def domain = Mock(GrailsDomainClass)
    domain.clazz >> ADomain
    domain.hasProperty("id") >> true
    def id = idProperty()
    domain.getPropertyByName("id") >> id
    domain.getPropertyByName(_) >> {args -> property(args[0])}
    domain.hasProperty(_) >> {args -> "format" != args[0]}
    domain.identifier >> id
    domain
  }

  GrailsDomainClassProperty property(name) {
    def property = Mock(GrailsDomainClassProperty)
    property.type >> String
    property.name >> name
    property
  }

  GrailsDomainClassProperty idProperty() {
    def property = Mock(GrailsDomainClassProperty)
    property.type >> Long
    property
  }
}
