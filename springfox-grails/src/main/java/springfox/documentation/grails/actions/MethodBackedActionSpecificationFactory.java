package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

import java.util.ArrayList;
import java.util.List;

@Component
public class MethodBackedActionSpecificationFactory implements ActionSpecificationFactory {
    private final HandlerMethodResolver handlerMethodResolver;

    @Autowired
    public MethodBackedActionSpecificationFactory(TypeResolver resolver) {
        this.handlerMethodResolver = new HandlerMethodResolver(resolver);
    }

    @Override
    public ActionSpecification create(GrailsActionContext context) {
        List<ResolvedMethodParameter> methodParameters = new ArrayList<>(context.pathParameters());
        methodParameters.addAll(handlerMethodResolver.methodParameters(context.handlerMethod()));
        return new ActionSpecification(
            context.path(),
            context.getRequestMethods(),
            context.supportedMediaTypes(),
            context.supportedMediaTypes(),
            context.handlerMethod(),
            methodParameters,
            handlerMethodResolver.methodReturnType(context.handlerMethod())
        );
    }
}
