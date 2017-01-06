package grails.springfox.sample


import grails.rest.*
import grails.converters.*

class SongController extends RestfulController {
    static responseFormats = ['json', 'xml']
    SongController() {
        super(Song)
    }
}
