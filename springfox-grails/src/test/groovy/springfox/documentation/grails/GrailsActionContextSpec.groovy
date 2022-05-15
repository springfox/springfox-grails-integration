package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import org.apache.commons.lang3.StringUtils
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Simple
import org.grails.web.mapping.DefaultLinkGenerator
import org.grails.web.mapping.DefaultUrlMappingsHolder
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification
import spock.lang.Unroll
import springfox.documentation.grails.doubles.AController

class GrailsActionContextSpec extends Specification implements UrlMappingSupport {
    @Unroll
    def "Generates the paths correctly for #controller #action"() {
        given:
        def actionAttributes = actionAttributes()
        def sut = new GrailsActionContext(controllerToTest(controller, AController),
                                          domain(),
                                          actionAttributes,
                                          action,
                                          new TypeResolver())

        expect:
        sut.path() == path
        sut.pathParameters().collect { it.defaultName().get() } == params
        sut.requestMethods == (method == "" ? ([] as Set) : [RequestMethod.valueOf(method)] as Set)

        where:
        controller | action   | params            | method   | path
        "author"   | "index"  | ["novelId"]       | "GET"    | "/novels/{novelid}/authors"
        "author"   | "update" | ["novelId", "id"] | "PUT"    | "/novels/{novelid}/authors/{id}"
        "author"   | "create" | ["novelId"]       | "GET"    | "/novels/{novelid}/authors/create"
        "author"   | "show"   | ["novelId", "id"] | "GET"    | "/novels/{novelid}/authors/{id}"
        "author"   | "save"   | ["novelId"]       | "POST"   | "/novels/{novelid}/authors"
        "author"   | "edit"   | ["novelId", "id"] | "GET"    | "/novels/{novelid}/authors/{id}/edit"
        "author"   | "delete" | ["novelId", "id"] | "DELETE" | "/novels/{novelid}/authors/{id}"
        "artist"   | "index"  | []                | "GET"    | "/artists"
        "artist"   | "update" | ["id"]            | "PUT"    | "/artist/{id}"
        "artist"   | "delete" | ["id"]            | "DELETE" | "/artist/{id}"
        "artist"   | "create" | []                | ""       | "/artist/create"
        "artist"   | "show"   | ["id"]            | "GET"    | "/artists/{id}"
        "artist"   | "save"   | []                | "POST"   | "/artist"
        "artist"   | "edit"   | ["id"]            | "GET"    | "/artist/{id}/edit"
        "album"    | "index"  | []                | "GET"    | "/albums"
        "album"    | "update" | ["id"]            | "PUT"    | "/album/{id}"
        "album"    | "delete" | ["id"]            | "DELETE" | "/album/{id}"
        "album"    | "create" | []                | "GET"    | "/albums/create"
        "album"    | "show"   | ["id"]            | "GET"    | "/albums/{id}"
        "album"    | "save"   | []                | "POST"   | "/albums"
        "album"    | "edit"   | ["id"]            | "GET"    | "/albums/{id}/edit"
        "book"     | "index"  | []                | "GET"    | "/books"
        "book"     | "update" | ["id"]            | "PUT"    | "/books/{id}"
        "book"     | "delete" | ["id"]            | "DELETE" | "/books/{id}"
        "book"     | "create" | []                | "GET"    | "/books/create"
        "book"     | "show"   | ["id"]            | "GET"    | "/books/{id}"
        "book"     | "save"   | []                | "POST"   | "/books"
        "book"     | "edit"   | ["id"]            | "GET"    | "/books/{id}/edit"
        "product"  | "list"   | []                | "GET"    | "/product"
        "product"  | "other"  | []                | ""       | "/product/other"
        "product"  | null     | ["id"]            | "GET"    | "/store/product/{id}"
        "generic"  | "index"  | []                | "GET"    | "/generic"
        "generic"  | "update" | ["id"]            | "PUT"    | "/generic/{id}"
        "generic"  | "delete" | ["id"]            | "DELETE" | "/generic/{id}"
        "generic"  | "create" | []                | ""       | "/generic/create"
        "generic"  | "show"   | ["id"]            | "GET"    | "/generic/{id}"
        "generic"  | "save"   | []                | "POST"   | "/generic"
        "generic"  | "edit"   | ["id"]            | "GET"    | "/generic/{id}/edit"
        "generic"  | "other"  | []                | ""       | "/generic/other"
    }

    def actionAttributes() {
        def urlMappings = new DefaultUrlMappingsHolder(urlMappings())
        new GrailsActionAttributes(links(urlMappings), urlMappings)
    }

    def links(DefaultUrlMappingsHolder urlMappings) {
        def links = new DefaultLinkGenerator("http://localhost:8080", "")
        links.urlMappingsHolder = urlMappings
        links
    }

    PersistentEntity domain() {
        def stub = Stub(PersistentEntity)
        stub.getPropertyByName(_) >> { args -> property(args[0]) }
        stub
    }

    def property(name) {
        def stub = Stub(Simple)
        stub.getType() >> String
        stub.name >> name
        stub
    }

    def controllerToTest(String name, clazz) {
        def stub = Stub(GrailsControllerClass)
        stub.name >> "${StringUtils.capitalize(name)}Controller"
        stub.logicalPropertyName >> name
        stub.clazz >> clazz
        stub
    }
}
