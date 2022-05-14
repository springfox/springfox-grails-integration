package springfox.documentation.grails.definitions;

import com.google.common.base.Objects;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.model.types.Identity;
import org.grails.datastore.mapping.model.types.Simple;
import org.grails.datastore.mapping.model.types.ToOne;

public class DefaultGrailsPropertySelector implements GrailsPropertySelector {
    @Override
    public boolean test(PersistentProperty each) {
        return (each instanceof Simple || each instanceof ToOne || each instanceof Identity)
            && !Objects.equal(each.getName(), "version");
    }
}
