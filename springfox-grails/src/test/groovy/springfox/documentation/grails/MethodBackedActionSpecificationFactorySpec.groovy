package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.web.mapping.UrlMapping
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod

class MethodBackedActionSpecificationFactorySpec extends ActionSpecificationFactorySpec {

  def "Resolves all method backed actions"() {
    given:
      def resolver = new TypeResolver()
      def sut = new MethodBackedActionSpecificationFactory(resolver)
    and:
      urlMappings.urlMappings >> [otherMapping(Mock(UrlMapping))]
    when:
      def spec = sut.create(new GrailsActionContext(regularController, domain, actionAttributes, "save", resolver))
    then: "All http attributes match"
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.POST] as Set
      spec.handlerMethod.method == BookController.methods.find {it.name == "save" }
      spec.path == "/book.{format}"

    and: "Parameters match"
      spec.parameters.size() == 2
      spec.parameters[0].parameterType == resolver.resolve(String)
      spec.parameters[0].parameterIndex == 0
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "format"

      spec.parameters[1].parameterType == resolver.resolve(Book)
      spec.parameters[1].parameterIndex == 0
      spec.parameters[1].defaultName().isPresent()
      spec.parameters[1].defaultName().get() == "book"

    and: "Return type matches"
      spec.returnType == resolver.resolve(Object)

  }

  def otherMapping(UrlMapping urlMapping) {
    urlMapping.controllerName >> "A"
    urlMapping.actionName >> "other"
    urlMapping.httpMethod >> "POST"
    urlMapping
    urlMapping
  }
}
