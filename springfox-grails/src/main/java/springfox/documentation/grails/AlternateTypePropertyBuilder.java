package springfox.documentation.grails;

import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;

import static org.springframework.util.StringUtils.*;

public class AlternateTypePropertyBuilder {
  private Class<?> clazz;
  private String name;
  private boolean canRead;
  private boolean canWrite;

  public AlternateTypePropertyBuilder withType(Class<?> clazz) {
    this.clazz = clazz;
    return this;
  }

  public AlternateTypePropertyBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public AlternateTypePropertyBuilder withCanRead(boolean canRead) {
    this.canRead = canRead;
    return this;
  }

  public AlternateTypePropertyBuilder withCanWrite(boolean canWrite) {
    this.canWrite = canWrite;
    return this;
  }

  public DynamicType.Builder<Object> apply(DynamicType.Builder<Object> builder) {
    return builder
        .defineField(name, clazz, Visibility.PRIVATE)
        .defineMethod("get" + capitalize(name), clazz, Visibility.PUBLIC)
        .intercept(FieldAccessor.ofField(name))
        .defineMethod("set" + capitalize(name), Void.TYPE, Visibility.PUBLIC)
        .withParameters(clazz)
        .intercept(FieldAccessor.ofField(name));
  }
}