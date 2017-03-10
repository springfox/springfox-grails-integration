package grails.springfox.sample

class Book {
    Long id
    String name

    static constraints = {
        name nullable: false
    }
}
