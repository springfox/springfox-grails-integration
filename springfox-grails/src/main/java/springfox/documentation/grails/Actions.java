package springfox.documentation.grails;

import grails.web.Action;
import groovy.lang.GroovyObject;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

class Actions {

  private Actions() {
    throw new UnsupportedOperationException();
  }

  static Map<String, HandlerMethod> actionsToHandler(Class grailsController) {
    Map<String, HandlerMethod> handlerLookup = new HashMap<>();
    Class superClass = grailsController;
    while (superClass != Object.class && superClass != GroovyObject.class) {
      for (Method method : superClass.getMethods()) {
        if (Modifier.isPublic(method.getModifiers()) && method.getAnnotation(Action.class) != null) {
          handlerLookup.put(method.getName(), new HandlerMethod(grailsController, method));
        }
      }
      superClass = superClass.getSuperclass();
    }
    return handlerLookup;
  }
}
