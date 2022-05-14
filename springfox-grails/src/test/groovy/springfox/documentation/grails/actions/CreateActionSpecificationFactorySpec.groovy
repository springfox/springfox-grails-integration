package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain
import springfox.documentation.grails.GrailsActionContext

class CreateActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {
  def "Create action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new CreateActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "create", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "create" }
      spec.path == "/a/create"

    and: "Parameters match"
      spec.parameters.size() == 1
      spec.parameters[0].parameterType == resolver.resolve(ADomain)
      spec.parameters[0].parameterIndex == 1
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "body"

    and: "Return type matches"
      spec.returnType == resolver.resolve(ADomain)
  }

  def "Create action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new CreateActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
