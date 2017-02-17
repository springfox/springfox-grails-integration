package springfox.documentation.grails

import grails.web.Action


class AController {

  @Action
  def index() {
  }

  def nonAction() {

  }

  private privateMethod() {
  }

  protected void protectedMethod() {

  }

  void packagePrivateMethod() {

  }
}
