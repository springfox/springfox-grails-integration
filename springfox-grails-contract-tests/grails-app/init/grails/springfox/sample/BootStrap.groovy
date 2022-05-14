package grails.springfox.sample

import org.grails.plugins.web.controllers.ControllersGrailsPlugin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper

class BootStrap {

    @Autowired
    DocumentationPluginsBootstrapper bootstrapper

    def init = { servletContext ->
        Genre.withTransaction {
            ['Rock', 'Pop', 'Metal', 'Folk'].each {
                new Genre(name: it).save(flush: true)
            }
        }
        bootstrapper.start()
    }
    def destroy = {
    }
}
