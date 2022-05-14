package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import springfox.documentation.service.ResolvedMethodParameter;

import static java.util.Collections.*;
import static springfox.documentation.grails.SynthesizedAnnotations.REQUEST_BODY_ANNOTATION;
import static springfox.documentation.grails.SynthesizedAnnotations.pathVariable;
import static springfox.documentation.grails.SynthesizedAnnotations.requestParam;

public class Parameters {
  private Parameters() {
    throw new UnsupportedOperationException();
  }

  public static ResolvedMethodParameter pathParameter(
      int index,
      String name,
      ResolvedType resolvedType) {
    return new ResolvedMethodParameter(
        index,
        name,
        singletonList(pathVariable(name)),
        resolvedType);
  }

  public static ResolvedMethodParameter queryParameter(
      int index,
      String name,
      ResolvedType resolvedType,
      boolean required,
      String defaultStaticValue) {
    return new ResolvedMethodParameter(
        index,
        name,
        singletonList(requestParam(name, name, required, defaultStaticValue)),
        resolvedType);
  }

  public static ResolvedMethodParameter bodyParameter(int parameterIndex, ResolvedType resolvedType) {
    return new ResolvedMethodParameter(
        parameterIndex,
        "body",
        singletonList(REQUEST_BODY_ANNOTATION), resolvedType);
  }
}
