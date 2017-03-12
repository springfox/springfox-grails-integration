package springfox.documentation.grails;

import grails.core.GrailsDomainClassProperty;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

import java.util.function.Function;

public interface GrailsPropertyTransformer
    extends Function<GrailsDomainClassProperty, AlternateTypePropertyBuilder> {
}
