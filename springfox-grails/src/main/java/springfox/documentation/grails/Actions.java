package springfox.documentation.grails;

import com.google.common.collect.ImmutableMap;
import grails.web.Action;
import groovy.lang.GroovyObject;
import org.grails.core.DefaultGrailsControllerClass;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.*;

class Actions {

  private static final String PROPERTY_RESPONSE_FORMATS = "responseFormats";
  private static final Map<String, MediaType> mediaTypes = ImmutableMap.<String, MediaType>builder()
      .put("json", MediaType.APPLICATION_JSON)
      .put("xml", MediaType.APPLICATION_XML)
      .build();

  private Actions() {
    throw new UnsupportedOperationException();
  }

  public static Map<String, HandlerMethod> actionsToHandler(Class grailsController) {
    Map<String, HandlerMethod> handlerLookup = new HashMap<>();
    List<Method> methodsWithActionAnnotation = actionAnnotatedMethods(grailsController);

    for (Method method: methodsWithActionAnnotation){
      Method actionHandlerMethod = findMethodThatHandlesActionAnnotatedMethod(method, grailsController);
      handlerLookup.put(actionHandlerMethod.getName(), new HandlerMethod(grailsController, actionHandlerMethod));
    }

    return handlerLookup;
  }

  private static List<Method> actionAnnotatedMethods(Class grailsController){
    List<Method> methodsWithActionAnnotation = new ArrayList<>();
    Class superClass = grailsController;
    while (superClass != Object.class && superClass != GroovyObject.class) {
      for (Method method : superClass.getMethods()) {
        if (Modifier.isPublic(method.getModifiers()) && method.getAnnotation(Action.class) != null) {
          methodsWithActionAnnotation.add(method);
        }
      }
      superClass = superClass.getSuperclass();
    }
    return methodsWithActionAnnotation;
  }

  private static Method findMethodThatHandlesActionAnnotatedMethod(Method actionAnnotatedMethod, Class grailsController){
    Class superClass = grailsController;

    while (superClass != Object.class && superClass != GroovyObject.class) {
      for (Method method : superClass.getMethods()) {
        if (actionAnnotatedMethod.getName() == method.getName()
                && Modifier.isPublic(method.getModifiers())
                && method.getAnnotation(Action.class) == null
                ) {
          return method;
        }
      }
      superClass = superClass.getSuperclass();
    }

    return actionAnnotatedMethod;
  }

  public static Set<RequestMethod> methodOverrides(
      GrailsActionContext context,
      Set<RequestMethod> defaultMethods) {
    Set<RequestMethod> methods = new HashSet<>();
    Map<String, String> allowedMethods;
    try {
      Class clazz = context.getController().getClazz();
      Field allowedMethodsField = clazz
          .getDeclaredField(DefaultGrailsControllerClass.ALLOWED_HTTP_METHODS_PROPERTY);
      allowedMethodsField.setAccessible(true);
      allowedMethods = (Map<String, String>) allowedMethodsField.get(clazz);
      if (allowedMethods != null && allowedMethods.containsKey(context.getAction())) {
        methods.add(RequestMethod.valueOf(allowedMethods.get(context.getAction())));
      } else {
        methods.addAll(defaultMethods);
      }
    } catch (NoSuchFieldException | IllegalAccessException ignored) {
      methods.addAll(defaultMethods);
    }
    return methods;
  }

  public static Set<RequestMethod> methodOverrides(GrailsActionContext context) {
    return methodOverrides(context, newHashSet());
  }

  public static Set<MediaType> mediaTypeOverrides(GrailsActionContext context) {
    Set<MediaType> produces = newHashSet(MediaType.APPLICATION_JSON);
    List<String> responseFormats;
    try {
      Class clazz = context.getController().getClazz();
      Field responseFormatFields = clazz.getDeclaredField(PROPERTY_RESPONSE_FORMATS);
      responseFormatFields.setAccessible(true);
      responseFormats = (List<String>) responseFormatFields.get(clazz);
      if (responseFormats != null && !responseFormats.isEmpty()) {
        return responseFormats.stream()
            .map(format -> mediaTypes.getOrDefault(format, MediaType.ALL))
            .collect(Collectors.toSet());
      }
    } catch (NoSuchFieldException | IllegalAccessException ignored) {
    }
    return produces;
  }
}
