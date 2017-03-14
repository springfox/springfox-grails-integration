package springfox.documentation.grails;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;

@SuppressWarnings({"squid:S1188"})
class SynthesizedAnnotations {
  static final RequestBody REQUEST_BODY_ANNOTATION = new RequestBody() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return RequestBody.class;
    }

    @Override
    public boolean required() {
      return true;
    }
  };

  private SynthesizedAnnotations() {
    throw new UnsupportedOperationException();
  }

  static PathVariable pathVariable(final String name) {
    return new PathVariable() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return PathVariable.class;
      }

      @Override
      public String value() {
        return name;
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public boolean required(){
        return true;
      }
    };
  }

  static RequestParam requestParam(final String name, final String value, final boolean required, final String
      defaultValue) {
    return new RequestParam() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return RequestParam.class;
      }

      @Override
      public String value() {
        return value;
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public boolean required() {
        return required;
      }

      @Override
      public String defaultValue() {
        return defaultValue;
      }
    };
  }
}