package springfox.documentation.grails

import grails.core.GrailsApplication
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import spock.lang.Specification

class GrailsSerializationTypeGeneratorSpec extends Specification {
  def "Serialization properties work" () {
    given:
      def sut = grailsGenerator()
    when:
      def clazz = sut.from(petDomain())
      def instance = clazz.newInstance()
    and:
      instance.setName("Dilip")
    then:
      clazz.declaredFields.length == 1
      clazz.declaredFields[0].name == "name"
      instance.getName() == "Dilip"
  }

  def grailsApplication() {
    def app = Mock(GrailsApplication)
    app.getArtefacts("Domain") >> [petDomain()]
    app
  }

  def petDomain() {
    def domain = Mock(GrailsDomainClass)
    domain.name >> "Pet"
    domain.clazz >> Pet
    domain.properties >> [property("name", String)]
    domain
  }

  def property(name, type) {
    def property = Mock(GrailsDomainClassProperty)
    property.name >> name
    property.referencedPropertyType >> type
    property
  }

  def grailsGenerator() {
    new GrailsSerializationTypeGenerator(
        new DefaultGrailsPropertySelector(),
        new DefaultGrailsPropertyTransformer(),
        new DefaultGeneratedClassNamingStrategy())
  }
}
