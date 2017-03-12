package springfox.documentation.grails


class Book {
  Long id
  String name

  static constraints = {
    name nullable: false
  }
}
