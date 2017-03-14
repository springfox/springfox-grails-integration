package springfox.documentation.grails;

import grails.core.GrailsDomainClass;


@FunctionalInterface
public interface ActionSpecificationFactory {
  default Class<?> idType(GrailsDomainClass domain) {
    return domain != null ? domain.getIdentifier().getType() : Void.TYPE;
  }

  default Class domainClass(GrailsDomainClass domain) {
    if (domain != null) {
      return domain.getClazz();
    }
    return Void.TYPE;
  }

  ActionSpecification create(GrailsActionContext actionContext);
}
