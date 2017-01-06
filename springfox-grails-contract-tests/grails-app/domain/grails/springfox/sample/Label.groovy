package grails.springfox.sample

class Label {

    long id
    String name
    String address

    static hasMany = [artists: Artist, albums: Album]

    static constraints = {
    }
}
