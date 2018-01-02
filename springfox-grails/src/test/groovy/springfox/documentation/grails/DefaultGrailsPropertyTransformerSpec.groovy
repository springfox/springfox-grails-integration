package springfox.documentation.grails

import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association
import spock.lang.Specification
import spock.lang.Unroll

class DefaultGrailsPropertyTransformerSpec extends Specification {
  @Unroll
  def "Infers types correctly for grails property #name"() {
    given:
    def sut = new DefaultGrailsPropertyTransformer()

    when:
    def transformed = sut.apply(property)

    then:
    transformed.name == name
    transformed.clazz == type

    where:
    property                                    | name              | type
    id(domainClass())                             | "id"              | Long
    relatedEntityProperty(domainClass())          | "relatedEntity"   | RelatedEntity
    relatedEntityIdProperty(domainClass())        | "relatedEntityId" | Long
    scalarProperty("test", String, domainClass()) | "test"            | String
  }

  def id(owner) {
    scalarProperty("id", Long, owner)
  }

  def scalarProperty(propertyName, propertyType, owner) {
    def property = Mock(PersistentProperty)
    property.type >> propertyType
    property.name >> propertyName
    property.owner >> owner

    if (propertyName == "Id") {
      owner.identity >> property
    }
    property
  }

  def relatedEntityProperty(owner) {
    def property = Mock(PersistentProperty)
    property.name >> "relatedEntity"
    property.owner >> owner
    property.type >> RelatedEntity
    property
  }

  def relatedEntityIdProperty(owner) {
    def property = Mock(PersistentProperty)
    property.type >> Long
    property.name >> "relatedEntityId"
    property.owner >> owner
    property
  }

  def domainClass() {
    def domain = Mock(PersistentEntity)
    domain.associations >> [association(domain)]
    domain
  }

  def relatedEntityDomain() {
    def domain = Mock(PersistentEntity)
    domain.identity >> id()
    domain
  }

  def association(domain) {
    def association = Mock(Association)
    association.inverseSide >> inverseAssociation()
    association.name >> "relatedEntityId"
    association.owner >> domain
    association.type >> Long
    association
  }

  def inverseAssociation() {
    def association = Mock(Association)
    association.owner >> relatedEntityDomain()
    association.type >> RelatedEntity
    association
  }

  class RelatedEntity {
  }
}
