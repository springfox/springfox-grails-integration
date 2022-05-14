package springfox.documentation.grails.actions

import grails.core.GrailsControllerClass
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.web.mapping.DefaultLinkGenerator
import org.grails.web.mapping.DefaultUrlMappingsHolder
import spock.lang.Specification
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain
import springfox.documentation.grails.doubles.BookController
import springfox.documentation.grails.GrailsActionAttributes
import springfox.documentation.grails.UrlMappingSupport

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
    def domain = Mock(PersistentEntity)
    domain.javaClass >> ADomain
    domain.hasProperty("id", Long) >> true
    def id = idProperty()
    domain.getPropertyByName("id") >> id
    domain.getPropertyByName(_) >> {args -> property(args[0])}
    domain.hasProperty(_, _) >> {args -> "format" != args[0]}
    domain.identity >> id
    domain
  }

  PersistentProperty property(name) {
    def property = Mock(PersistentProperty)
    property.type >> String
    property.name >> name
    property
  }

  PersistentProperty idProperty() {
    def property = Mock(PersistentProperty)
    property.type >> Long
    property
  }
}
