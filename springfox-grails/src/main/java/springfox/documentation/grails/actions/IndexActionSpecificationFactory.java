package springfox.documentation.grails.actions;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.grails.ActionSpecification;
import springfox.documentation.grails.ActionSpecificationFactory;
import springfox.documentation.grails.GrailsActionContext;
import springfox.documentation.service.ResolvedMethodParameter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.grails.Parameters.queryParameter;

class IndexActionSpecificationFactory implements ActionSpecificationFactory {
    private final TypeResolver resolver;

    public IndexActionSpecificationFactory(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public ActionSpecification create(GrailsActionContext context) {
        return new ActionSpecification(
            context.path(),
            context.getRequestMethods(),
            context.supportedMediaTypes(),
            context.supportedMediaTypes(),
            context.handlerMethod(),
            parameters(context),
            resolver.resolve(List.class, domainClass(context.getDomainClass())));
    }

    private List<ResolvedMethodParameter> parameters(GrailsActionContext context) {
        List<ResolvedMethodParameter> parameters = newArrayList(context.pathParameters());
        parameters.add(queryParameter(1, "max", resolver.resolve(Integer.class), false, ""));
        return parameters;
    }
}
