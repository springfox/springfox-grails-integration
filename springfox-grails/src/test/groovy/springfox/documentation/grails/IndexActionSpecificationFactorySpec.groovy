package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification

class IndexActionSpecificationFactorySpec extends Specification {
  def controller = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)

  def setup() {
    controller.clazz >> AController
    domain.clazz >> ADomain
  }

  def "Index action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new IndexActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, "index"))
    then:
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.GET] as Set
      spec.parameters.size() == 1
      spec.parameters[0].parameterType == resolver.resolve(Integer)
      spec.parameters[0].parameterIndex == 1
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "max"
      spec.returnType == resolver.resolve(List, ADomain)
      spec.handlerMethod.method == AController.methods.find {it.name == "index" }
  }

  def "Index action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new IndexActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, "unknown"))
    then:
      def exception = thrown(NullPointerException)
      exception.message.contains("Handler method is null")
  }
}
