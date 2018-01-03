package springfox.documentation.grails;

import org.grails.datastore.mapping.model.PersistentProperty;

import java.util.function.Predicate;

public interface GrailsPropertySelector extends Predicate<PersistentProperty> {
}
