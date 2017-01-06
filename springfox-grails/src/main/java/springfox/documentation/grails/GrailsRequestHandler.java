package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;
import grails.web.mapping.LinkGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.service.ResolvedMethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class GrailsRequestHandler implements RequestHandler {
    private final GrailsDomainClass domain;
    private final GrailsControllerClass controller;
    private final LinkGenerator linkGenerator;
    private final String action;
    private final TypeResolver resolver;
    //TODO: This needs to be dynamic based on how the controller is configured
    private final Map<String, Set<RequestMethod>> defaultSupportedMethodsByOperation;
    private final Map<String, Function<GrailsDomainClass, Set<ResolvedMethodParameter>>> parameterLookup;

    public GrailsRequestHandler(
            GrailsDomainClass domain,
            GrailsControllerClass controller,
            LinkGenerator linkGenerator,
            String action,
            TypeResolver resolver) {

        this.resolver = resolver;
        this.action = action;
        this.domain = domain;
        this.controller = controller;
        this.linkGenerator = linkGenerator;
        this.defaultSupportedMethodsByOperation = supportedMethodsByOperation();
        this.parameterLookup = parameterLookup();
    }

    @Override
    public Class<?> declaringClass() {
        return (Class<?>) controller.getClazz();
    }

    @Override
    public boolean isAnnotatedWith(Class<? extends Annotation> annotation) {
        return AnnotationUtils.findAnnotation(declaringClass(), annotation) != null;
    }

    @Override
    public PatternsRequestCondition getPatternsCondition() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>(2);
        map.put("controller", controller.getName());
        map.put("action", action);
        map.put("params", paramsFor(action));
        String link = linkGenerator.link(map)
                .replace("%7B", "{").replace("%7D", "}")
                .replace(linkGenerator.getServerBaseURL(), "");
        return new PatternsRequestCondition(link);
    }

    private Map<String, String> paramsFor(String action) {
        return parameterLookup.get(action).apply(domain).stream()
                .filter(p -> p.hasParameterAnnotation(PathVariable.class))
                .filter(p -> p.defaultName().isPresent())
                .collect(
                        Collectors.toMap(
                                p -> p.defaultName().get(),
                                p -> String.format("{%s}", p.defaultName().get())));
    }

    @Override
    public String groupName() {
        return controller.getLogicalPropertyName();
    }

    @Override
    public String getName() {
        return action;
    }

    @Override
    public Set<RequestMethod> supportedMethods() {
        return defaultSupportedMethodsByOperation.get(action);
    }

    @Override
    public Set<? extends MediaType> produces() {
        return new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public Set<? extends MediaType> consumes() {
        return new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public Set<NameValueExpression<String>> headers() {
        return new HashSet<>();
    }

    @Override
    public Set<NameValueExpression<String>> params() {
        return new HashSet<>();
    }

    @Override
    public <T extends Annotation> Optional<T> findAnnotation(Class<T> annotation) {
        return com.google.common.base.Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getMethod(), annotation));
    }

    @Override
    public RequestHandlerKey key() {
        return new RequestHandlerKey(getPatternsCondition().getPatterns(), supportedMethods(), consumes(), produces());
    }

    @Override
    public List<ResolvedMethodParameter> getParameters() {
        return new ArrayList<>(parameterLookup.get(action).apply(domain));
    }

    @Override
    public ResolvedType getReturnType() {
        if (domain == null) {
            return resolver.resolve(Void.TYPE);
        }

        return resolver.resolve(domain.getClazz());
    }

    @Override
    public <T extends Annotation> Optional<T> findControllerAnnotation(Class<T> annotation) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(getHandlerMethod().getBeanType(), annotation));
    }

    @Override
    public RequestMappingInfo getRequestMapping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HandlerMethod getHandlerMethod() {
        return new HandlerMethod(controller.getClazz(), methodByAction());
    }

    @Override
    public RequestHandler combine(RequestHandler other) {
        throw new UnsupportedOperationException();
    }

    public ResolvedMethodParameter idParameter(GrailsDomainClass domain) {
        Class<?> type = idType(domain);
        return new ResolvedMethodParameter(1, "id", Collections.singletonList(SynthesizedAnnotations.PATH_VARIABLE_ANNOTATION), resolver.resolve(type));
    }

    public ResolvedMethodParameter maxRows(GrailsDomainClass domain) {
        return new ResolvedMethodParameter(1, "max", Collections.singletonList(SynthesizedAnnotations.REQUEST_PARAM_ANNOTATION), resolver.resolve(Integer.class));
    }

    public Class<?> domainType(GrailsDomainClass domain) {
        return domain != null ? domain.getClazz() : Void.TYPE;
    }

    public Class<?> idType(GrailsDomainClass domain) {
        return domain != null ? domain.getIdentifier().getType() : Void.TYPE;
    }

    public ResolvedMethodParameter bodyParameter(GrailsDomainClass domain) {
        return new ResolvedMethodParameter(1, "body", Collections.singletonList(SynthesizedAnnotations.REQUEST_BODY_ANNOTATION), resolver.resolve(domainType(domain)));
    }

    public Method methodByAction() {
        return Arrays.stream(controller.getClazz().getMethods())
                .filter(m -> Objects.equals(m.getName(), action))
                .findFirst()
                .orElse(null);
    }

    private LinkedHashMap<String, Set<RequestMethod>> supportedMethodsByOperation() {
        LinkedHashMap<String, Set<RequestMethod>> map = new LinkedHashMap<>(8);
        map.put("index", new HashSet<>(Collections.singletonList(RequestMethod.GET)));
        map.put("show", new HashSet<>(Collections.singletonList(RequestMethod.GET)));
        map.put("create", new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)));
        map.put("edit", new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)));
        map.put("update", new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)));
        map.put("save", new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)));
        map.put("patch", new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)));
        map.put("delete", new HashSet<>(Collections.singletonList(RequestMethod.POST)));
        return map;
    }

    private LinkedHashMap<String, Function<GrailsDomainClass, Set<ResolvedMethodParameter>>> parameterLookup() {
        LinkedHashMap<String, Function<GrailsDomainClass, Set<ResolvedMethodParameter>>> map = new LinkedHashMap<>(8);
        map.put("index", d -> new HashSet<>(Collections.singletonList(idParameter(d))));
        map.put("show", d -> new HashSet<>(Collections.singletonList(idParameter(d))));
        map.put("update", d -> new HashSet<>(Arrays.asList(idParameter(d), bodyParameter(d))));
        map.put("edit", d -> new HashSet<>(Arrays.asList(idParameter(d), bodyParameter(d))));
        map.put("create", d -> new HashSet<>(Collections.singletonList(bodyParameter(d))));
        map.put("save", d -> new HashSet<>(Arrays.asList(idParameter(d), bodyParameter(d))));
        map.put("patch", d -> new HashSet<>(Arrays.asList(idParameter(d), bodyParameter(d))));
        map.put("delete", d -> new HashSet<>(Collections.singletonList(idParameter(d))));
        return map;
    }

}
