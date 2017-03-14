package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod

class PatchActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {
  def "Patch action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new PatchActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "patch", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.PATCH] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "patch" }
      spec.path == "/a/{id}"

    and: "Parameters match"
      spec.parameters.size() == 2
      spec.parameters[0].parameterType == resolver.resolve(Long)
      spec.parameters[0].parameterIndex == 1
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "id"

      spec.parameters[1].parameterType == resolver.resolve(ADomain)
      spec.parameters[1].parameterIndex == 2
      spec.parameters[1].defaultName().isPresent()
      spec.parameters[1].defaultName().get() == "body"

    and: "Return type matches"
      spec.returnType == resolver.resolve(ADomain)
  }

  def "Patch action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new PatchActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
