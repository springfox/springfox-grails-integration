package springfox.documentation.grails;

import com.google.common.base.Objects;
import org.grails.datastore.mapping.model.PersistentProperty;

public class DefaultGrailsPropertySelector implements GrailsPropertySelector {
  @Override
  public boolean test(PersistentProperty each) {
    return each.getOwner().getAssociations().stream().noneMatch(a -> a.getName().equals(each.getName()))
        && !Objects.equal(each.getName(), "version");
  }
}
