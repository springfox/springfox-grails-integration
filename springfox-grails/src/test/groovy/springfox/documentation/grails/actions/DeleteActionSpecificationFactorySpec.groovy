package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain
import springfox.documentation.grails.GrailsActionContext

class DeleteActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {

  def "Delete action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new DeleteActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "delete", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.DELETE] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "delete" }
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

  def "Delete action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new DeleteActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
