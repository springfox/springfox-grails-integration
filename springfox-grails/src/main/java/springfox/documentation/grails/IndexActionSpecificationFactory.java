package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import grails.core.GrailsDomainClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class IndexActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  @Autowired
  public IndexActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsDomainClass domain) {
    return new ActionSpecification(
        new HashSet<>(Collections.singletonList(RequestMethod.GET)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new ArrayList<>(Collections.singletonList(
            queryParameter(
                1,
                "max",
                resolver.resolve(Integer.class),
                false,
                ""))),
        resolver.resolve(List.class, domainClass(domain)));

  }

  private Class domainClass(GrailsDomainClass domain) {
    if (domain != null) {
      return domain.getClazz();
    }
    return Void.TYPE;
  }
}
