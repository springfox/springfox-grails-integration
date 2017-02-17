package springfox.documentation.grails

import grails.rest.RestfulController
import grails.web.Action

class AController extends RestfulController<ADomain> {
  AController(Class<ADomain> resource) {
    super(resource, false)
  }

  @Action
  ADomain other(Integer first, ADomain domain) {

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
