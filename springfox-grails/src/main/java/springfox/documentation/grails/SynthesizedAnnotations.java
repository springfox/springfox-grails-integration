package springfox.documentation.grails;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.annotation.Annotation;

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

    static final PathVariable PATH_VARIABLE_ANNOTATION = new PathVariable() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return PathVariable.class;
        }

        @Override
        public String value() {
            return "id";
        }

        @Override
        public String name() {
            return "id";
        }

        @Override
        public boolean required() {
            return true;
        }
    };

    static final RequestParam REQUEST_PARAM_ANNOTATION = new RequestParam() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestParam.class;
        }

        @Override
        public String value() {
            return null;
        }

        @Override
        public String name() {
            return null;
        }

        @Override
        public boolean required() {
            return false;
        }

        @Override
        public String defaultValue() {
            return null;
        }
    };

    static final ApiIgnore API_IGNORE_ANNOTATION = new ApiIgnore() {
        @Override
        public String value() {
            return "Parameter is ignored";
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ApiIgnore.class;
        }
    };

    private SynthesizedAnnotations() {
        throw new UnsupportedOperationException();
    }
}