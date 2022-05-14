package springfox.documentation.grails.definitions;

import io.micronaut.core.util.StringUtils;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.model.types.ToOne;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {
    @Override
    public AlternateTypePropertyBuilder apply(PersistentProperty property) {
        Class<?> type = property.getType();
        if (property instanceof ToOne) {
            ToOne<?> toOneRelation = (ToOne<?>) property;
            return new AlternateTypePropertyBuilder()
                .withName(property.getName()
                    + StringUtils.capitalize(toOneRelation.getAssociatedEntity().getIdentity().getName()))
                .withType(toOneRelation.getAssociatedEntity().getIdentity().getType())
                .withCanRead(true)
                .withCanWrite(true);
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
