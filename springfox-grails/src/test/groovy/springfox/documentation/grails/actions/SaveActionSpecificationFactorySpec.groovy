package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.ADomain
import springfox.documentation.grails.GrailsActionContext

class SaveActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {

  def "Save action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new SaveActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, actionAttributes, "save", resolver))

    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.POST] as Set
      spec.handlerMethod.method == AController.methods.find {it.name == "save" }
      spec.path == "/a"

    and: "Parameters match"
      spec.parameters.size() == 1
      spec.parameters[0].parameterType == resolver.resolve(ADomain)
      spec.parameters[0].parameterIndex == 0
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "body"

    and: "Return type matches"
      spec.returnType == resolver.resolve(ADomain)
  }

  def "Save action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new SaveActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, actionAttributes, "unknown", resolver))
    then:
      thrown(NullPointerException)
  }
}
