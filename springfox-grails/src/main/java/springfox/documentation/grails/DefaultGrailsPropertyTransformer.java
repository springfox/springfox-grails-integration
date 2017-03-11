package springfox.documentation.grails;

import grails.core.GrailsDomainClassProperty;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {
  @Override
  public AlternateTypePropertyBuilder apply(GrailsDomainClassProperty each) {
    Class type = each.getReferencedPropertyType();
    if (!each.isPersistent() && each.getName().endsWith("Id")) {
      GrailsDomainClassProperty property = each.getDomainClass().getPropertyByName(each.getName().replace("Id", ""));
      type = property.getDomainClass().getIdentifier().getType();
    }
    return new AlternateTypePropertyBuilder()
        .withName(each.getName())
        .withType(type)
        .withCanRead(true)
        .withCanWrite(true);
  }
}
