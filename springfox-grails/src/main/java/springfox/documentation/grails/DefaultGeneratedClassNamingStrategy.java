package springfox.documentation.grails;

public class DefaultGeneratedClassNamingStrategy implements GeneratedClassNamingStrategy {
  @Override
  public String name(Class clazz) {
    return String.format("%s.generated.%s", clazz.getPackage().getName(), clazz.getSimpleName());
  }
}
