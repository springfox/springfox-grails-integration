package grails.springfox.sample

class Artist {

    long id
    String name
    boolean isBand
    Label signedTo

    static hasMany = [albums: Album, songs: Song]

    static constraints = {
        name nullable: false
        isBand nullable: true
        signedTo nullable: true
    }
}
