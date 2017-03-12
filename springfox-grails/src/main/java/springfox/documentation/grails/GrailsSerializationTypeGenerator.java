package springfox.documentation.grails;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

import static com.google.common.collect.Lists.*;

@Component
public class GrailsSerializationTypeGenerator {

  private final GrailsPropertySelector propertySelector;
  private final GrailsPropertyTransformer propertyTransformer;
  private final GeneratedClassNamingStrategy naming;

  @Autowired
  public GrailsSerializationTypeGenerator(
      GrailsPropertySelector propertySelector,
      GrailsPropertyTransformer propertyTransformer,
      GeneratedClassNamingStrategy naming) {
    this.propertySelector = propertySelector;
    this.propertyTransformer = propertyTransformer;
    this.naming = naming;
  }

  public Class<?> from(GrailsDomainClass domain) {
    ImmutableList<AlternateTypePropertyBuilder> properties =
        FluentIterable.from(newArrayList(domain.getProperties()))
            .filter(propertySelector)
            .transform(propertyTransformer)
            .toList();
    return new AlternateTypeBuilder()
        .fullyQualifiedClassName(naming.name(domain.getClazz()))
        .withProperties(properties)
        .build();
  }
}
