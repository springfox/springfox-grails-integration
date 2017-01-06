package grails.springfox.sample

class Genre {

    long id
    String name

    static hasMany = [albums: Album]

    static constraints = {
    }
}
