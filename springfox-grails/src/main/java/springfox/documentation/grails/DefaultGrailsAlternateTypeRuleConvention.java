package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static springfox.documentation.schema.AlternateTypeRules.*;

@Component
public class DefaultGrailsAlternateTypeRuleConvention implements AlternateTypeRuleConvention {
  private static final int DEFAULT_CONVENTION_PRECEDENCE = Ordered.HIGHEST_PRECEDENCE + 2000;
  private final TypeResolver resolver;
  private final GrailsApplication application;
  private final GrailsSerializationTypeGenerator typeGenerator;

  @Autowired
  public DefaultGrailsAlternateTypeRuleConvention(
      TypeResolver resolver,
      GrailsApplication application,
      GrailsSerializationTypeGenerator typeGenerator) {

    this.resolver = resolver;
    this.application = application;
    this.typeGenerator = typeGenerator;
  }

  @Override
  public List<AlternateTypeRule> rules() {
    return Arrays.stream(application.getArtefacts("Domain"))
        .filter(GrailsDomainClass.class::isInstance)
        .map(domain -> newRule(
            domain.getClazz(),
            resolver.resolve(typeGenerator.from((GrailsDomainClass) domain)), getOrder()))
        .collect(Collectors.toList());
  }

  @Override
  public int getOrder() {
    return DEFAULT_CONVENTION_PRECEDENCE;
  }
}
