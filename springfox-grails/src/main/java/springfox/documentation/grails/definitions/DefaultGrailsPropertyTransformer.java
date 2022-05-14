package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentProperty;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {
    @Override
    public AlternateTypePropertyBuilder apply(PersistentProperty property) {
        Class<?> type = property.getType();
        if (type == null && property.getName().endsWith("Id")) {
            type = relatedDomainIdentifierType(relatedDomainProperty(property));
        }

        return new AlternateTypePropertyBuilder()
            .withName(property.getName())
            .withType(type)
            .withCanRead(true)
            .withCanWrite(true);
    }

    private Class<?> relatedDomainIdentifierType(PersistentProperty<?> property) {
        return property.getOwner().getIdentity().getType();
    }

    private PersistentProperty<?> relatedDomainProperty(PersistentProperty<?> property) {
        String entityPropertyName = property.getName().replace("Id", "");
        return property.getOwner().getPropertyByName(entityPropertyName);
    }
}
