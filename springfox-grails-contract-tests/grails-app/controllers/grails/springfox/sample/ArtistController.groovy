package grails.springfox.sample


import grails.rest.*
import grails.converters.*

class ArtistController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ArtistController() {
        super(Artist)
    }
}
