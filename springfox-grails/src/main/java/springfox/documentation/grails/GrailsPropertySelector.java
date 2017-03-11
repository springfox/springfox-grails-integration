package springfox.documentation.grails;

import com.google.common.base.Predicate;
import grails.core.GrailsDomainClassProperty;

public interface GrailsPropertySelector extends Predicate<GrailsDomainClassProperty> {
}
