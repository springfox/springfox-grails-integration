package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsApplication
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import spock.lang.Specification

class DefaultGrailsAlternateTypeRuleConventionSpec extends Specification {
  def "Alternate types for grails classes are created" () {
    given:
      def app = grailsApplication()
      def resolver = new TypeResolver()
      def sut = new DefaultGrailsAlternateTypeRuleConvention(resolver, app, grailsGenerator())
    when:
      def rules = sut.rules()
    and:
      def alternate = rules[0].alternateFor(resolver.resolve(Pet))
    then:
      rules.size() == 1
      alternate.erasedType.simpleName == "Pet"
      alternate.erasedType.package.name == "springfox.documentation.grails.generated"
      alternate.erasedType.declaredFields.find { it.name == "name"} != null
      alternate.erasedType.declaredMethods.find { it.name == "getName"} != null
      alternate.erasedType.declaredMethods.find { it.name == "setName"} != null
  }

  def grailsGenerator() {
    new GrailsSerializationTypeGenerator(
        new DefaultGrailsPropertySelector(),
        new DefaultGrailsPropertyTransformer(),
        new DefaultGeneratedClassNamingStrategy())
  }

  def grailsApplication() {
    def app = Mock(GrailsApplication)
    app.mappingContext >> mappingContext()
    app
  }

  MappingContext mappingContext() {
    def mappingContext = Mock(MappingContext)
    mappingContext.persistentEntities >> [petDomain()]
    mappingContext
  }

  def petDomain() {
    def domain = Mock(PersistentEntity)
    domain.name >> "Pet"
    domain.javaClass >> Pet
    domain.persistentProperties >> [property("name", String, domain)]
    domain.associations >> []
    domain
  }

  def property(name, type, domain) {
    def property = Mock(PersistentProperty)
    property.name >> name
    property.owner >> domain
    property.type >> type
    property
  }
}
