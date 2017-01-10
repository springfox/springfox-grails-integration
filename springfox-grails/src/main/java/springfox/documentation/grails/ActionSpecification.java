package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.List;
import java.util.Set;

public class ActionSpecification {
  private final Set<RequestMethod> supportedMethods;
  private final Set<? extends MediaType> produces;
  private final Set<? extends MediaType> consumes;
  private final List<ResolvedMethodParameter> parameters;
  private final ResolvedType resolvedType;

  public ActionSpecification(
      Set<RequestMethod> supportedMethods,
      Set<? extends MediaType> produces,
      Set<? extends MediaType> consumes,
      List<ResolvedMethodParameter> parameters,
      ResolvedType resolvedType) {
    this.supportedMethods = supportedMethods;
    this.produces = produces;
    this.consumes = consumes;
    this.parameters = parameters;
    this.resolvedType = resolvedType;
  }

  public Set<RequestMethod> getSupportedMethods() {
    return supportedMethods;
  }

  public Set<? extends MediaType> getProduces() {
    return produces;
  }

  public Set<? extends MediaType> getConsumes() {
    return consumes;
  }

  public List<ResolvedMethodParameter> getParameters() {
    return parameters;
  }

  public ResolvedType getResolvedType() {
    return resolvedType;
  }
}
