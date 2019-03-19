package springfox.documentation.grails;

import grails.core.GrailsApplicationLifeCycle;
import groovy.lang.Closure;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;

import java.util.Map;

@Component
public class SwaggerGrailsApplicationPostProcessor implements GrailsApplicationLifeCycle {

    private DocumentationPluginsBootstrapper documentationPluginsBootstrapper;

    public SwaggerGrailsApplicationPostProcessor(DocumentationPluginsBootstrapper documentationPluginsBootstrapper) {
        this.documentationPluginsBootstrapper = documentationPluginsBootstrapper;
    }

    @Override
    public Closure doWithSpring() {
        return null;
    }

    @Override
    public void doWithDynamicMethods() {
    }

    @Override
    public void doWithApplicationContext() {
    }

    @Override
    public void onConfigChange(Map<String, Object> event) {
    }

    @Override
    public void onStartup(Map<String, Object> event) {
        documentationPluginsBootstrapper.start();
    }

    @Override
    public void onShutdown(Map<String, Object> event) {
    }
}
