package springfox.documentation.grails;

import grails.core.GrailsDomainClassProperty;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {
  @Override
  public AlternateTypePropertyBuilder apply(GrailsDomainClassProperty property) {
    Class type = property.getReferencedPropertyType();
    if (!property.isPersistent() && property.getName().endsWith("Id")) {
      type = relatedDomainIdentifierType(relatedDomainProperty(property));
    }

    return new AlternateTypePropertyBuilder()
        .withName(property.getName())
        .withType(type)
        .withCanRead(true)
        .withCanWrite(true);
  }

  private Class relatedDomainIdentifierType(GrailsDomainClassProperty property) {
    return property.getDomainClass().getIdentifier().getType();
  }

  private GrailsDomainClassProperty relatedDomainProperty(GrailsDomainClassProperty property) {
    String entityPropertyName = property.getName().replace("Id", "");
    return property.getDomainClass().getPropertyByName(entityPropertyName);
  }
}
