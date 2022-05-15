package springfox.documentation.grails.definitions

import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Identity
import org.grails.datastore.mapping.model.types.ManyToOne
import org.grails.datastore.mapping.model.types.OneToMany
import org.grails.datastore.mapping.model.types.OneToOne
import org.grails.datastore.mapping.model.types.Simple
import spock.lang.Specification
import spock.lang.Unroll

class DefaultGrailsPropertySelectorSpec extends Specification {

    @Unroll
    def "selects scalar,id and toOne properties except version"() {
        given:
        def sut = new DefaultGrailsPropertySelector()
        expect:
        sut.test(property) == expected
        where:
        property                  | expected
        identityProperty('id')    | true
        scalarProperty('name')    | true
        manyToOne('artist')       | true
        oneToOne('artist')        | true
        oneToMany('books')        | false
        scalarProperty('version') | false
    }

    PersistentProperty scalarProperty(String name) {
        return simpleProperty(name)
    }

    def manyToOne(name) {
        stubProperty(ManyToOne, name)
    }

    def oneToMany(name) {
        stubProperty(OneToMany, name)
    }

    def simpleProperty(String name) {
        stubProperty(Simple, name)
    }

    def identityProperty(String name) {
        stubProperty(Identity, name)
    }

    def oneToOne(String name) {
        stubProperty(OneToOne, name)
    }

    protected PersistentProperty stubProperty(Class<? extends PersistentProperty> clazz, String name) {
        def property = Stub(clazz)
        property.name >> name
        return property
    }
}
