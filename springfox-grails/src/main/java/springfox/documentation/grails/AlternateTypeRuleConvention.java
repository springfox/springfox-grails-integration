package springfox.documentation.grails;

import org.springframework.core.Ordered;
import springfox.documentation.schema.AlternateTypeRule;

import java.util.List;

public interface AlternateTypeRuleConvention extends Ordered {
  List<AlternateTypeRule> rules();
}
