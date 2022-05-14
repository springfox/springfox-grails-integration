package springfox.documentation.grails.doubles

import grails.rest.RestfulController
import grails.web.Action

class AController extends RestfulController<ADomain> {
    AController(Class<ADomain> resource) {
        super(resource, false)
    }

    @Action
    ADomain other(Integer first, ADomain domain) {
        return new ADomain()
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
