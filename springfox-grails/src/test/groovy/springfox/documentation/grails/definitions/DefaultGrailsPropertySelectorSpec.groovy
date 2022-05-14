package springfox.documentation.grails.definitions


import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association
import spock.lang.Specification
import spock.lang.Unroll

class DefaultGrailsPropertySelectorSpec extends Specification {

    @Unroll
    def "selects scalar properties except version"() {
        given:
        def sut = new DefaultGrailsPropertySelector()
        expect:
        sut.test(property) == expected
        where:
        property        | expected
        versionProperty() | false
        scalarProperty() | true
        associationProperty('name') | false
        associationProperty('version') | false
    }

    PersistentProperty versionProperty() {
        property("version")
    }

    PersistentProperty scalarProperty() {
        property("name")
    }


    PersistentProperty associationProperty(String name) {
        association(name)
    }

    def association(name) {
        def property = Mock(Association)
        property.name >> name
        property
    }

    def property(name) {
        def property = Mock(PersistentProperty)
        property.name >> name
        property
    }
}
