package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import spock.lang.Specification

class RestfulActionSpecificationFactorySpec extends Specification {
  def controller = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)
  def identifierProperty = Mock(GrailsDomainClassProperty)

  def setup() {
    controller.clazz >> AController
    domain.clazz >> ADomain
    domain.identifier >> identifierProperty
    domain.identifier.type >> Integer
  }

  def "Resolves all restful actions"() {
    given:
      def resolver = new TypeResolver()
      def sut = new RestfulActionSpecificationFactory(resolver)
    when:
      def actionSpec = sut.create(new GrailsActionContext(controller, domain, action.toLowerCase()))
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
      sut.create(new GrailsActionContext(controller, domain, "unknown"))
    then:
      def exception = thrown(IllegalArgumentException)
      exception.message.contains("Action unknown is not a restful action")
  }
}
