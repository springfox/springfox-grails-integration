package springfox.documentation.grails;

import com.google.common.base.Function;
import grails.core.GrailsDomainClassProperty;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

public interface GrailsPropertyTransformer
    extends Function<GrailsDomainClassProperty, AlternateTypePropertyBuilder> {
}
