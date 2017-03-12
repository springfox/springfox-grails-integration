package springfox.documentation.grails

import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMappings
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification
import spock.lang.Unroll

class ActionsSpec extends Specification implements GrailsControllerSupport {
  def "Cannot instantiate this class"() {
    when:
      new Actions()
    then:
      thrown(UnsupportedOperationException)
  }

  def "Detects grails actions"() {
    given:
      def controller = aController()
    when:
      def handlerMethods = Actions.actionsToHandler(controller)
    then:
      handlerMethods.size() == 9
      handlerMethods.containsKey("index")
      handlerMethods.containsKey("show")
      handlerMethods.containsKey("create")
      handlerMethods.containsKey("update")
      handlerMethods.containsKey("save")
      handlerMethods.containsKey("edit")
      handlerMethods.containsKey("patch")
      handlerMethods.containsKey("delete")
      handlerMethods.containsKey("other")
  }

  @Unroll
  def "Detects grails method overrides for #action"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, action)
    when:
      def methods = Actions.methodOverrides(context)
    then:
      methods == expected
    where:
      controller          | action          | expected
      OverridenController | "withOverrides" | [RequestMethod.POST] as Set
      OverridenController | "noOverrides"   | [] as Set
  }

  @Unroll
  def "Detects grails method overrides with defaults for #action"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, action)
    when:
      def methods = Actions.methodOverrides(context, [RequestMethod.GET] as Set)
    then:
      methods == expected
    where:
      controller          | action          | expected
      OverridenController | "withOverrides" | [RequestMethod.POST] as Set
      OverridenController | "noOverrides"   | [RequestMethod.GET] as Set
  }

  @Unroll
  def "Detects grails produces overrides for #controller"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, "any")
    when:
      def methods = Actions.producesOverrides(context)
    then:
      methods == expected
    where:
      controller          | expected
      AController         | [MediaType.APPLICATION_JSON] as Set
      OverridenController | [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML] as Set
  }

  def grailsController(Class controller) {
    def grails = Mock(GrailsControllerClass)
    grails.clazz >> controller
    grails
  }

  GrailsActionContext context(controller, action) {
    new GrailsActionContext(
        controller,
        Mock(GrailsDomainClass),
        actionAttributes(),
        action)
  }

  def actionAttributes() {
    new GrailsActionAttributes(
        Mock(LinkGenerator),
        Mock(UrlMappings))
  }

  class OverridenController {
    static allowedMethods = [withOverrides: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']
    
    def withOverrides() {
    }

    def show() {
    }
  }
}
