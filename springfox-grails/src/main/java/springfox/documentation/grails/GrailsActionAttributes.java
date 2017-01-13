package springfox.documentation.grails;

import grails.web.mapping.LinkGenerator;
import grails.web.mapping.UrlMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GrailsActionAttributes {
  private final LinkGenerator linkGenerator;
  private final UrlMappings urlMappings;

  @Autowired
  public GrailsActionAttributes(
      LinkGenerator linkGenerator,
      UrlMappings urlMappings) {
    this.linkGenerator = linkGenerator;
    this.urlMappings = urlMappings;
  }

  String actionUrl(GrailsActionContext context, Map<String, String> value) {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>(2);
    map.put("controller", context.getController().getName());
    map.put("action", context.getAction());
    map.put("params", value);
    return linkGenerator.link(map)
        .replace("%7B", "{").replace("%7D", "}")
        .replace(linkGenerator.getServerBaseURL(), "")
        .toLowerCase();
  }

  Set<RequestMethod> httpMethod(GrailsActionContext context) {
    return Arrays.stream(urlMappings.getUrlMappings())
        .filter(mapping ->
            Objects.equals(mapping.getControllerName(), context.getController().getName())
                && Objects.equals(mapping.getActionName(), context.getAction()))
        .map(mapping -> RequestMethod.valueOf(mapping.getHttpMethod()))
        .collect(Collectors.toSet());
  }
}
