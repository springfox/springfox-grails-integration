package springfox.documentation.grails.actions

import grails.core.GrailsControllerClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMappings
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Simple
import org.grails.web.mapping.DefaultLinkGenerator
import org.grails.web.mapping.DefaultUrlMappingsHolder
import spock.lang.Specification
import springfox.documentation.grails.GrailsActionAttributes
import springfox.documentation.grails.UrlMappingSupport
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain
import springfox.documentation.grails.doubles.BookController

class ActionSpecificationFactorySpec extends Specification implements UrlMappingSupport {
  GrailsControllerClass controller
  PersistentEntity domain
  LinkGenerator links
  UrlMappings urlMappings
  GrailsActionAttributes actionAttributes
  GrailsControllerClass regularController

  def setup() {
    urlMappings = new DefaultUrlMappingsHolder(urlMappings())
    links = new DefaultLinkGenerator("http://localhost:8080", "")
    links.urlMappingsHolder = urlMappings
    actionAttributes = new GrailsActionAttributes(links, urlMappings)

    domain = stubDomain()
    controller = stubController()
    regularController = stubRegularController()
  }

  GrailsControllerClass stubRegularController() {
    def regularController = Stub(GrailsControllerClass)
    regularController.clazz >> BookController
    regularController.name >> "Book"
    regularController.logicalPropertyName >> "Book"
    regularController
  }

  def stubController() {
    def controller = Stub(GrailsControllerClass)
    controller.clazz >> AController
    controller.name >> "AController"
    controller.logicalPropertyName >> "A"
    controller
  }

  def stubDomain() {
    def domain = Stub(PersistentEntity)
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
    def property = Stub(Simple)
    property.type >> String
    property.name >> name
    property
  }

  PersistentProperty idProperty() {
    def property = Stub(Simple)
    property.type >> Long
    property
  }
}
