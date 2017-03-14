package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.MediaType;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.*;
import static springfox.documentation.grails.Parameters.*;

class IndexActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  public IndexActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    return new ActionSpecification(
        context.path(),
        Collections.singleton(context.getRequestMethod()),
        context.supportedMediaTypes(),
        context.supportedMediaTypes(),
        context.handlerMethod(),
        parameters(context.supportedMediaTypes()),
        resolver.resolve(List.class, domainClass(context.getDomainClass())));
  }

  private List<ResolvedMethodParameter> parameters(Set<MediaType> mediaTypes) {
    List<ResolvedMethodParameter> parameters = newArrayList(queryParameter(
        1,
        "max",
        resolver.resolve(Integer.class),
        false,
        ""));
    if (mediaTypes.size() > 1) {
      parameters.add(pathParameter(
          2,
          "format",
          resolver.resolve(String.class)));
    }
    return parameters;
  }
}
