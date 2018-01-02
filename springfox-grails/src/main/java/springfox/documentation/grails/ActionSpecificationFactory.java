package springfox.documentation.grails;

import org.grails.datastore.mapping.model.PersistentEntity;


@FunctionalInterface
public interface ActionSpecificationFactory {
  default Class<?> idType(PersistentEntity domain) {
    return domain != null ? domain.getIdentity().getType() : Void.TYPE;
  }

  default Class domainClass(PersistentEntity domain) {
    if (domain != null) {
      return domain.getJavaClass();
    }
    return Void.TYPE;
  }

  ActionSpecification create(GrailsActionContext actionContext);
}
