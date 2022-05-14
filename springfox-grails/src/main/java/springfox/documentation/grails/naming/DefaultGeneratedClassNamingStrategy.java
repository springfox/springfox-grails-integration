package springfox.documentation.grails.naming;

import springfox.documentation.grails.definitions.GeneratedClassNamingStrategy;

public class DefaultGeneratedClassNamingStrategy implements GeneratedClassNamingStrategy {
  @Override
  public String name(Class<?> clazz) {
    return String.format("%s.generated.%s", clazz.getPackage().getName(), clazz.getSimpleName());
  }
}
