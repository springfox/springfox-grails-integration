package springfox.documentation.grails

import grails.rest.RestfulController

class AController extends RestfulController<ADomain> {
  AController(Class<ADomain> resource) {
    super(resource, false)
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
