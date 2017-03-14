package springfox.documentation.grails

import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import spock.lang.Specification
import spock.lang.Unroll


class DefaultGrailsPropertyTransformerSpec extends Specification {
  @Unroll
  def "Infers types correctly for grails property #name" (){
    given:
      def sut = new DefaultGrailsPropertyTransformer()
    when:
      def transformed = sut.apply(property)
    then:
      transformed.name == name
      transformed.clazz == type
    where:
      property                        | name                | type
      id()                            | "id"                | Long
      relatedEntityProperty()         | "relatedEntity"     | RelatedEntity
      relatedEntityIdProperty()       | "relatedEntityId"   | Long
      scalarProperty("test", String)  | "test"              | String
  }

  def id() {
    scalarProperty("id", Long)
  }

  def scalarProperty(propertyName, propertyType) {
    def property = Mock(GrailsDomainClassProperty)
    property.referencedPropertyType >> propertyType
    property.type >> propertyType
    property.persistent >> true
    property.name >> propertyName
    property
  }

  def relatedEntityProperty() {
    def property = Mock(GrailsDomainClassProperty)
    property.referencedPropertyType >> RelatedEntity
    property.persistent >> true
    property.name >> "relatedEntity"
    property.domainClass >> relatedEntityDomain()
    property
  }

  def relatedEntityIdProperty() {
    def property = Mock(GrailsDomainClassProperty)
    property.referencedPropertyType >> Long
    property.persistent >> false
    property.name >> "relatedEntityId"
    property.domainClass >> domainClass()
    property
  }

  GrailsDomainClass domainClass() {
    def domain = Mock(GrailsDomainClass)
    domain.getPropertyByName("relatedEntity") >> relatedEntityProperty()
    domain.hasProperty(_) >> {args -> "format" != args[0]}
    domain
  }

  GrailsDomainClass relatedEntityDomain() {
    def domain = Mock(GrailsDomainClass)
    domain.getIdentifier() >> id()
    domain
  }

  class RelatedEntity {
  }
}
