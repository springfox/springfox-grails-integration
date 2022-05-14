package springfox.documentation.grails.doubles

import grails.validation.Validateable


class Book implements Validateable {
    Long id
    String name

    static constraints = {
        name nullable: false
    }
}
