package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.web.Action;
import groovy.lang.GroovyObject;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

  static Map<String, ActionSpecificationFactory> restfulActions(TypeResolver resolver) {
    LinkedHashMap<String, ActionSpecificationFactory> map = new LinkedHashMap<>(8);
    map.put("index", new IndexActionSpecificationFactory(resolver));
    map.put("show", new ShowActionSpecificationFactory(resolver));
    map.put("create", new CreateActionSpecificationFactory(resolver));
    map.put("edit", new EditActionSpecificationFactory(resolver));
    map.put("update", new UpdateActionSpecificationFactory(resolver));
    map.put("save", new SaveActionSpecificationFactory(resolver));
    map.put("patch", new PatchActionSpecificationFactory(resolver));
    map.put("delete", new DeleteActionSpecificationFactory(resolver));
    return map;
  }
}
