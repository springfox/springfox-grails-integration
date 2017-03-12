package springfox.documentation.grails;

import grails.core.GrailsDomainClassProperty;

import java.util.function.Predicate;

public interface GrailsPropertySelector extends Predicate<GrailsDomainClassProperty> {
}
