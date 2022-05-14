package springfox.documentation.grails.definitions;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsApplication;
import org.grails.datastore.mapping.model.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Component
public class DefaultGrailsAlternateTypeRuleConvention implements AlternateTypeRuleConvention {
    private static final Logger log = LoggerFactory.getLogger(DefaultGrailsAlternateTypeRuleConvention.class);
    private static final int DEFAULT_CONVENTION_PRECEDENCE = Ordered.HIGHEST_PRECEDENCE + 2000;
    private final TypeResolver resolver;
    private final GrailsApplication application;
    private final GrailsSerializationTypeGenerator typeGenerator;

    @Autowired
    public DefaultGrailsAlternateTypeRuleConvention(TypeResolver resolver,
                                                    GrailsApplication application,
                                                    GrailsSerializationTypeGenerator typeGenerator) {

        this.resolver = resolver;
        this.application = application;
        this.typeGenerator = typeGenerator;
    }

    @Override
    public List<AlternateTypeRule> rules() {
        MappingContext mappingContext = application.getMappingContext();
        if (mappingContext == null) {
            log.warn("No MappingContext present in GrailsApplication");
            return Collections.emptyList();
        }
        return mappingContext.getPersistentEntities().stream()
            .map(domain -> newRule(domain.getJavaClass(),
                resolver.resolve(typeGenerator.from(domain)), getOrder()))
            .collect(Collectors.toList());
    }

    @Override
    public int getOrder() {
        return DEFAULT_CONVENTION_PRECEDENCE;
    }
}
