package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod

class ShowActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {
  def "Show action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new ShowActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "show", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.GET] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "show" }
      spec.path == "/a/{id}"
    
    and: "Parameters match"
      spec.parameters.size() == 1
      spec.parameters[0].parameterType == resolver.resolve(Long)
      spec.parameters[0].parameterIndex == 0
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "id"

    and: "Return type matches"
      spec.returnType == resolver.resolve(ADomain)
  }

  def "Show action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new ShowActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
