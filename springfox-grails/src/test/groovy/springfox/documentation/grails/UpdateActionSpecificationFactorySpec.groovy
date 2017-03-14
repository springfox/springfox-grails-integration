package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod

class UpdateActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {

  def "Update action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new UpdateActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "update", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.PUT] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "update" }
      spec.path == "/a/{id}.{format}"

    and: "Parameters match"
      spec.parameters.size() == 3
      spec.parameters[0].parameterType == resolver.resolve(Long)
      spec.parameters[0].parameterIndex == 0
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "id"

      spec.parameters[1].parameterType == resolver.resolve(String)
      spec.parameters[1].parameterIndex == 1
      spec.parameters[1].defaultName().isPresent()
      spec.parameters[1].defaultName().get() == "format"

      spec.parameters[2].parameterType == resolver.resolve(ADomain)
      spec.parameters[2].parameterIndex == 2
      spec.parameters[2].defaultName().isPresent()
      spec.parameters[2].defaultName().get() == "body"

    and: "Return type matches"
      spec.returnType == resolver.resolve(ADomain)
  }

  def "Update action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new UpdateActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      def exception = thrown(NullPointerException)
      exception.message.contains("Handler method is null")
  }
}
