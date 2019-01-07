package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

import javax.servlet.ServletContext;
import java.util.List;

public class SwaggerDocumentationPluginsBootstrapper extends DocumentationPluginsBootstrapper {

        SwaggerDocumentationPluginsBootstrapper(DocumentationPluginsManager documentationPluginsManager,
                                                List<RequestHandlerProvider> handlerProviders,
                                                DocumentationCache scanned,
                                                ApiDocumentationScanner resourceListing,
                                                TypeResolver typeResolver,
                                                Defaults defaults,
                                                ServletContext servletContext) {
            super(documentationPluginsManager, handlerProviders, scanned, resourceListing, typeResolver, defaults, servletContext);
        }

        @Override
        public boolean isAutoStartup() {
            return false;
        }

}
