package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMappings
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Simple
import spock.lang.Specification
import springfox.documentation.grails.GrailsActionAttributes
import springfox.documentation.grails.GrailsActionContext
import springfox.documentation.grails.UrlMappingSupport
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain

class RestfulActionSpecificationFactorySpec extends Specification implements UrlMappingSupport {
  def controller = Stub(GrailsControllerClass)
  def domain = Stub(PersistentEntity)
  def identifierProperty = Stub(Simple)
  def urlMappings = Stub(UrlMappings)
  def links = Stub(LinkGenerator)
  def actionAttributes = new GrailsActionAttributes(links, urlMappings)

  def setup() {
    controller.clazz >> AController
    controller.logicalPropertyName >> "A"
    domain.javaClass >> ADomain
    (domain as PersistentEntity).identity >> identifierProperty
    (domain as PersistentEntity).identity.type >> Integer
    domain.getPropertyByName(_) >> {args -> property(args[0])}
    domain.hasProperty(_) >> {args -> "format" != args[0]}
    urlMappings.urlMappings >> urlMappings()
    links.getServerBaseURL() >> "http://localhost:8080"
  }

  def property(name) {
    def stub = Stub(Simple)
    stub.name >> name
    stub.type >> (ADomain.declaredFields.find { it.name == name }?.type ?: String)
    stub
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
