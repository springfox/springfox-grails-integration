package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentProperty;

import java.util.function.Predicate;

public interface GrailsPropertySelector extends Predicate<PersistentProperty> {
}
