package springfox.documentation.grails.definitions

import grails.core.GrailsApplication
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import spock.lang.Specification
import springfox.documentation.grails.definitions.DefaultGrailsPropertySelector
import springfox.documentation.grails.definitions.DefaultGrailsPropertyTransformer
import springfox.documentation.grails.definitions.GrailsSerializationTypeGenerator
import springfox.documentation.grails.doubles.Pet
import springfox.documentation.grails.naming.DefaultGeneratedClassNamingStrategy

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
    def domain = Mock(PersistentEntity)
    domain.name >> "Pet"
    domain.javaClass >> Pet
    domain.persistentProperties >> [property("name", String)]
    domain
  }

  def property(name, type) {
    def property = Mock(PersistentProperty)
    property.name >> name
    property.type >> type
    property
  }

  def grailsGenerator() {
    new GrailsSerializationTypeGenerator(
        new DefaultGrailsPropertySelector(),
        new DefaultGrailsPropertyTransformer(),
        new DefaultGeneratedClassNamingStrategy())
  }
}
