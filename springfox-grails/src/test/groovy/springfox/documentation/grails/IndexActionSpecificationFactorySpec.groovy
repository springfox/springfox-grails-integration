package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.rest.RestfulController
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod

class IndexActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {
  def "Index action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new IndexActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "index", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.GET] as Set
      spec.handlerMethod.method == RestfulController.declaredMethods.find {it.name == "index" }
      spec.path == "/a"

    and: "Parameters match"
      spec.parameters.size() == 1

      spec.parameters[0].parameterType == resolver.resolve(Integer)
      spec.parameters[0].parameterIndex == 1
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "max"

    and: "Return type matches"
      spec.returnType == resolver.resolve(List, ADomain)
  }

  def "Index action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new IndexActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
