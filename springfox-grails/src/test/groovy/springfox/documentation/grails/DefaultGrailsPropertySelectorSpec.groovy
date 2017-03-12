package springfox.documentation.grails

import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import spock.lang.Specification
import spock.lang.Unroll


class DefaultGrailsPropertySelectorSpec extends Specification {
  @Unroll
  def "selects scalar properties except version" () {
    given:
      def sut = new DefaultGrailsPropertySelector()
    expect:
      sut.test(property) == expected
    where:
      property        | expected
      version()       | false
      scalar()        | true
      entity()        | false
      versionEntity() | false
  }

  GrailsDomainClassProperty version() {
    property("version", null)
  }

  GrailsDomainClassProperty scalar() {
    property("name", null)
  }

  GrailsDomainClassProperty entity() {
    property("name", Mock(GrailsDomainClass))
  }

  GrailsDomainClassProperty versionEntity() {
    property("version", Mock(GrailsDomainClass))
  }
  
  def property(name, domain) {
    def property = Mock(GrailsDomainClassProperty)
    property.name >> name
    property.referencedDomainClass >> domain
    property
  }
}
