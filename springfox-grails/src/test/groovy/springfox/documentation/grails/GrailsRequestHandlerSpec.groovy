package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import com.google.common.base.Objects
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMappings
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition
import spock.lang.Specification
import springfox.documentation.RequestHandlerKey
import springfox.documentation.service.ResolvedMethodParameter

class GrailsRequestHandlerSpec extends Specification {
  def resolver = new TypeResolver()
  def controller = new AnotherController()

  def "Adapts action specification"() {
    given:
      def paths = new PatternsRequestCondition("/test")
      def mediaTypes = [MediaType.APPLICATION_JSON] as Set
    when:
      def sut = grailsRequestHandler()
    and:
      sut.requestMapping
    then:
      thrown(UnsupportedOperationException)
    and:
      sut.declaringClass() == AnotherController
      !sut.isAnnotatedWith(Api)
      sut.getPatternsCondition() == paths
      sut.findControllerAnnotation(Api).isPresent()
      !sut.findAnnotation(Api).isPresent()
      sut.consumes() == mediaTypes
      sut.produces() == mediaTypes
      sut.parameters.size() == 1
      areEqual(sut.parameters[0], new ResolvedMethodParameter(0, "id", [], resolver.resolve(Integer)))
      sut.returnType == resolver.resolve(Pet)
      sut.key() == new RequestHandlerKey(paths.getPatterns(), [RequestMethod.GET] as Set, mediaTypes, mediaTypes)
      sut.handlerMethod.equals(handlerMethod())
  }

  def areEqual(ResolvedMethodParameter a, ResolvedMethodParameter b) {
    Objects.equal(a.defaultName(), b.defaultName()) &&
        Objects.equal(a.getParameterIndex(), b.getParameterIndex()) &&
        Objects.equal(a.getParameterType(), b.getParameterType())
  }

  def grailsRequestHandler(){
    def attributes = new GrailsActionAttributes(
        linkGenerator(),
        urlMappings())
    new GrailsRequestHandler(
          new GrailsActionContext(
              controller(),
              domain(),
              attributes,
              "test"
          ),
          attributes,
          actionSpecification()
      )
  }

  ActionSpecification actionSpecification() {
    new ActionSpecification(
        [RequestMethod.GET] as Set,
        [MediaType.APPLICATION_JSON] as Set,
        [MediaType.APPLICATION_JSON] as Set,
        handlerMethod(),
        [new ResolvedMethodParameter(0, "id", [], resolver.resolve(Integer))],
        resolver.resolve(Pet)
    )
  }

  def handlerMethod() {
    new HandlerMethod(
        controller,
        AnotherController.methods[0])
  }

  UrlMappings urlMappings() {
    Mock(UrlMappings)
  }

  LinkGenerator linkGenerator() {
    def links = Mock(LinkGenerator)
    links.link(_) >> "/test"
    links.serverBaseURL >> "http://localhost:8080"
    links
  }

  GrailsDomainClass domain() {
    Mock(GrailsDomainClass)
  }

  GrailsControllerClass controller() {
    def controller = Mock(GrailsControllerClass)
    controller.getClazz() >> AnotherController
    controller.name >> "Another"
    controller
  }

  @Api
  class AnotherController {
    def show(int id) {
    }
  }
}
