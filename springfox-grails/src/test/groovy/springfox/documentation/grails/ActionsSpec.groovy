package springfox.documentation.grails

import spock.lang.Specification


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
}
