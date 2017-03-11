package springfox.documentation.grails;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.util.List;

import static com.google.common.collect.Lists.*;

public class AlternateTypeBuilder {
  private String fullyQualifiedClassName;
  private List<AlternateTypePropertyBuilder> properties = newArrayList();

  public AlternateTypeBuilder fullyQualifiedClassName(String fullyQualifiedClassName) {
    this.fullyQualifiedClassName = fullyQualifiedClassName;
    return this;
  }

  public AlternateTypeBuilder property(AlternateTypePropertyBuilder property) {
    this.properties.add(property);
    return this;
  }

  public AlternateTypeBuilder withProperties(List<AlternateTypePropertyBuilder> properties) {
    this.properties.addAll(properties);
    return this;
  }

  public Class<?> build() {
    DynamicType.Builder<Object> builder = new ByteBuddy()
        .subclass(Object.class)
        .name(fullyQualifiedClassName);
    for (AlternateTypePropertyBuilder each : properties) {
      builder = each.apply(builder);
    }
    return builder.make()
        .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
        .getLoaded();
  }
}