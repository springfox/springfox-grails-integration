package springfox.documentation.grails.definitions

import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Simple
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
    def property = Stub(Simple)
    property.type >> propertyType
    property.name >> propertyName
    property
  }

  def relatedEntityProperty() {
    def property = Stub(Simple)
    property.type >> RelatedEntity
    property.name >> "relatedEntity"
    property.getOwner() >> relatedEntityDomain()
    property
  }

  def relatedEntityIdProperty() {
    def property = Stub(Simple)
    property.type >> Long
    property.name >> "relatedEntityId"
    property.getOwner() >> domainClass()
    property
  }

  PersistentEntity domainClass() {
    def domain = Stub(PersistentEntity)
    domain.getPropertyByName("relatedEntity") >> relatedEntityProperty()
    domain.getPersistentPropertyNames().contains(_) >> {args -> "format" != args[0]}
    domain
  }

  PersistentEntity relatedEntityDomain() {
    def domain = Stub(PersistentEntity)
    domain.getIdentity() >> id()
    domain
  }

  class RelatedEntity {
  }
}
