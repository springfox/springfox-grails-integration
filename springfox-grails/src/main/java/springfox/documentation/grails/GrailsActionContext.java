package springfox.documentation.grails;

import grails.core.GrailsControllerClass;
import grails.core.GrailsDomainClass;

public class GrailsActionContext {
  private final GrailsControllerClass controller;
  private final GrailsDomainClass domainClass;
  private final String action;

  public GrailsActionContext(
      GrailsControllerClass controller,
      GrailsDomainClass domainClass,
      String action) {
    this.controller = controller;
    this.domainClass = domainClass;
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
}
