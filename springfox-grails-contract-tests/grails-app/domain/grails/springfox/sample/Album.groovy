package grails.springfox.sample

class Album {

    long id
    String title, subtitle, coverImage
    Artist albumArtist
    Date releaseDate
    Rating rating
    Label label
    Genre genre

    static hasMany = [songs: Song]


    static constraints = {
        title nullable:false
        subtitle nullable:true
        coverImage nullable: true, url:true
        albumArtist nullable: false
        releaseDate nullable: true
        rating nullable: true
        label nullable:true
        genre nullable: true
    }
}
