package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Preconditions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.List;
import java.util.Set;

class ActionSpecification {
  private final Set<RequestMethod> supportedMethods;
  private final Set<MediaType> produces;
  private final Set<MediaType> consumes;
  private final List<ResolvedMethodParameter> parameters;
  private final ResolvedType returnType;
  private final HandlerMethod handlerMethod;

  public ActionSpecification(
      Set<RequestMethod> supportedMethods,
      Set<MediaType> produces,
      Set<MediaType> consumes,
      HandlerMethod handlerMethod,
      List<ResolvedMethodParameter> parameters,
      ResolvedType returnType) {

    Preconditions.checkNotNull(handlerMethod, "Handler method is null");

    this.supportedMethods = supportedMethods;
    this.produces = produces;
    this.consumes = consumes;
    this.parameters = parameters;
    this.returnType = returnType;
    this.handlerMethod = handlerMethod;
  }

  public Set<RequestMethod> getSupportedMethods() {
    return supportedMethods;
  }

  public Set<MediaType> getProduces() {
    return produces;
  }

  public Set<MediaType> getConsumes() {
    return consumes;
  }

  public List<ResolvedMethodParameter> getParameters() {
    return parameters;
  }

  public ResolvedType getReturnType() {
    return returnType;
  }

  public HandlerMethod getHandlerMethod() {
    return handlerMethod;
  }
}
