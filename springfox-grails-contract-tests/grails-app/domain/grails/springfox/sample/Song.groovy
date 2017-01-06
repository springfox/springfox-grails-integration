package grails.springfox.sample

class Song {

    long id
    String title
    Artist artist
    Genre genre

    static belongsTo = [album: Album]

    static constraints = {
    }
}
