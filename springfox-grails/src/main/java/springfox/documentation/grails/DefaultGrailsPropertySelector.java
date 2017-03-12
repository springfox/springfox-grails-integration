package springfox.documentation.grails;

import com.google.common.base.Objects;
import grails.core.GrailsDomainClassProperty;

public class DefaultGrailsPropertySelector implements GrailsPropertySelector {
  @Override
  public boolean test(GrailsDomainClassProperty each) {
    return each.getReferencedDomainClass() == null
        && !Objects.equal(each.getName(), "version");
  }
}
