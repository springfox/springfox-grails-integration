package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ShowActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  @Autowired
  public ShowActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsDomainClass domain) {
    return new ActionSpecification(
        new HashSet<>(Collections.singletonList(RequestMethod.GET)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new ArrayList<>(Collections.singletonList(pathParameter(1, "id", resolver.resolve(idType(domain))))),
        resolver.resolve(domain.getClazz()));

  }
}
