package springfox.documentation.grails

import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association
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

  PersistentProperty version() {
    property("version", String, petDomain(false))
  }

  PersistentProperty scalar() {
    property("name", String, petDomain(false))
  }

  PersistentProperty entity() {
    property("siblingId", Pet, petDomain(true))
  }

  PersistentProperty versionEntity() {
    property("version", Pet, petDomain(true))
  }
  
  def property(name, type, domain) {
    def property = Mock(PersistentProperty)
    property.name >> name
    property.owner >> domain
    property.type >> type
    property
  }

  def petDomain(boolean addAssociation) {
    def domain = Mock(PersistentEntity)
    domain.name >> "Pet"
    domain.javaClass >> Pet
    domain.persistentProperties >> [property("name", String, domain), property("siblingId", String, domain)]
    if (addAssociation) {
      domain.associations >> [association()]
    } else {
      domain.associations >> []
    }
    domain
  }

  def association() {
    def association = Mock(Association)
    association.inverseSide >> inverseAssociation()
    association.name >> "siblingId"
    association
  }

  def inverseAssociation() {
    def association = Mock(Association)
    association.owner >> petDomain(false)
    association
  }
}
