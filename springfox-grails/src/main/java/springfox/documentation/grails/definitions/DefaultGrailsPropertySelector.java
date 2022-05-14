package springfox.documentation.grails.definitions;

import com.google.common.base.Objects;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.model.types.Association;

public class DefaultGrailsPropertySelector implements GrailsPropertySelector {
    @Override
    public boolean test(PersistentProperty each) {
        return !(each instanceof Association) && !Objects.equal(each.getName(), "version");
    }
}
