package grails.springfox.sample

import grails.rest.RestfulController

class AlbumController extends RestfulController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']
    AlbumController() {
        super(Album)
    }
}
