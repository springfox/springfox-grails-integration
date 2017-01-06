package grails.springfox.sample


import grails.rest.*
import grails.converters.*

class LabelController extends RestfulController {
    static responseFormats = ['json', 'xml']
    LabelController() {
        super(Label)
    }
}
