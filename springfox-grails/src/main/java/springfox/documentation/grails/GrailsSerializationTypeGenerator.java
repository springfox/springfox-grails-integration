package springfox.documentation.grails;

import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    List<AlternateTypePropertyBuilder> properties =
        Arrays.stream(domain.getProperties())
            .filter(propertySelector)
            .map(propertyTransformer)
            .collect(Collectors.toList());
    return new AlternateTypeBuilder()
        .fullyQualifiedClassName(naming.name(domain.getClazz()))
        .withProperties(properties)
        .build();
  }
}
