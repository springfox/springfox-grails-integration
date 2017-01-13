package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.List;
import java.util.Set;

public class ActionSpecification {
  private final Set<RequestMethod> supportedMethods;
  private final Set<? extends MediaType> produces;
  private final Set<? extends MediaType> consumes;
  private final List<ResolvedMethodParameter> parameters;
  private final ResolvedType returnType;

  private final HandlerMethod handlerMethod;

  public ActionSpecification(
      Set<RequestMethod> supportedMethods,
      Set<? extends MediaType> produces,
      Set<? extends MediaType> consumes,
      HandlerMethod handlerMethod,
      List<ResolvedMethodParameter> parameters,
      ResolvedType returnType) {
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

  public Set<? extends MediaType> getProduces() {
    return produces;
  }

  public Set<? extends MediaType> getConsumes() {
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
