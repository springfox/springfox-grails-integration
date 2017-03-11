package springfox.documentation.grails;

import grails.core.GrailsDomainClass;

public interface TypeGeneratorNamingStrategy {
  String name(GrailsDomainClass domain);
}
