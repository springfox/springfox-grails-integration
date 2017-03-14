package grails.springfox.sample

class BootStrap {

    def init = { servletContext ->
        ['Rock', 'Pop', 'Metal', 'Folk'].each {
            new Genre(name: it).save(flush:true)
        }
    }
    def destroy = {
    }
}
