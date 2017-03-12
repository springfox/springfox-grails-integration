package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import grails.core.GrailsApplication;
import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

import java.util.Arrays;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.*;

@Component
public class DefaultGrailsAlternateTypeRuleConvention implements AlternateTypeRuleConvention {
  private static final int DEFAULT_CONVENTION_PRECEDENCE = Ordered.LOWEST_PRECEDENCE + 1000;
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
    return FluentIterable.from(Arrays.asList(application.getArtefacts("Domain")))
        .filter(GrailsDomainClass.class)
        .transform(toAlternateTypeRule())
        .toList();
  }

  private Function<GrailsDomainClass, AlternateTypeRule> toAlternateTypeRule() {
    return new Function<GrailsDomainClass, AlternateTypeRule>() {
      @Override
      public AlternateTypeRule apply(GrailsDomainClass domain) {
        return newRule(
            domain.getClazz(),
            resolver.resolve(typeGenerator.from(domain)), getOrder());
      }
    };
  }

  @Override
  public int getOrder() {
    return DEFAULT_CONVENTION_PRECEDENCE;
  }
}
