package grails.springfox.sample


import grails.rest.*
import grails.converters.*

class GenreController extends RestfulController {
    static responseFormats = ['json', 'xml']
    GenreController() {
        super(Genre)
    }
}
