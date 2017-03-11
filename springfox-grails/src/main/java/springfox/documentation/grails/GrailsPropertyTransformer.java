package springfox.documentation.grails;

import com.google.common.base.Function;
import grails.core.GrailsDomainClassProperty;

public interface GrailsPropertyTransformer
    extends Function<GrailsDomainClassProperty, AlternateTypePropertyBuilder> {
}
