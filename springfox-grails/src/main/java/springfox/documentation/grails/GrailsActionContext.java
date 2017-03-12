package springfox.documentation.grails;

import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;

class GrailsActionContext {
  private final GrailsControllerClass controller;
  private final GrailsDomainClass domainClass;
  private final GrailsActionAttributes urlProvider;
  private final String action;

  public GrailsActionContext(
      GrailsControllerClass controller,
      GrailsDomainClass domainClass,
      GrailsActionAttributes urlProvider,
      String action) {
    this.controller = controller;
    this.domainClass = domainClass;
    this.urlProvider = urlProvider;
    this.action = action;
  }

  public GrailsControllerClass getController() {
    return controller;
  }

  public GrailsDomainClass getDomainClass() {
    return domainClass;
  }

  public String getAction() {
    return action;
  }

  public GrailsActionAttributes getUrlProvider() {
    return urlProvider;
  }
}
