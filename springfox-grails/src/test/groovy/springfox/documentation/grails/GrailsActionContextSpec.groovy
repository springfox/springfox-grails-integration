package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import org.apache.commons.lang.StringUtils
import org.grails.web.mapping.DefaultLinkGenerator
import org.grails.web.mapping.DefaultUrlMappingsHolder
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification
import spock.lang.Unroll

class GrailsActionContextSpec extends Specification implements UrlMappingSupport {
  @Unroll
  def "Generates the paths correctly for #controller #action"() {
    given:
    def actionAttributes = actionAttributes()
    def sut = new GrailsActionContext(
        controllerToTest(controller, AController),
        domain(),
        actionAttributes,
        action,
        new TypeResolver())

    expect:
    sut.path() == path
    sut.pathParameters().collect { it.defaultName().get() } == params
    sut.requestMethods == [RequestMethod.valueOf(method)] as Set

    where:
    controller | action   | params                      | method    | path
    "author"   | "index"  | ["novelId", "format"]       | "GET"     | "/novels/{novelid}/authors?format={format}"
    "author"   | "update" | ["novelId", "id", "format"] | "PUT"     | "/novels/{novelid}/authors/{id}?format={format}"
    "author"   | "create" | ["novelId"]                 | "GET"     | "/novels/{novelid}/authors/create"
    "author"   | "show"   | ["novelId", "id", "format"] | "GET"     | "/novels/{novelid}/authors/{id}?format={format}"
    "author"   | "save"   | ["novelId", "format"]       | "POST"    | "/novels/{novelid}/authors?format={format}"
    "author"   | "edit"   | ["novelId", "id"]           | "GET"     | "/novels/{novelid}/authors/{id}/edit"
    "author"   | "delete" | ["novelId", "id", "format"] | "DELETE"  | "/novels/{novelid}/authors/{id}?format={format}"
    "artist"   | "index"  | ["format"]                  | "GET"     | "/artists?format={format}"
    "artist"   | "update" | ["id", "format"]            | "PUT"     | "/artist/{id}.{format}"
    "artist"   | "delete" | ["id", "format"]            | "DELETE"  | "/artist/{id}.{format}"
    "artist"   | "create" | []                          | "OPTIONS" | "/artist/create"
    "artist"   | "show"   | ["id", "format"]            | "GET"     | "/artists/{id}?format={format}"
    "artist"   | "save"   | ["format"]                  | "POST"    | "/artist.{format}"
    "artist"   | "edit"   | ["id"]                      | "GET"     | "/artist/{id}/edit"
    "album"    | "index"  | ["format"]                  | "GET"     | "/albums?format={format}"
    "album"    | "update" | ["id", "format"]            | "PUT"     | "/album/{id}.{format}"
    "album"    | "delete" | ["id", "format"]            | "DELETE"  | "/album/{id}.{format}"
    "album"    | "create" | []                          | "GET"     | "/albums/create"
    "album"    | "show"   | ["id", "format"]            | "GET"     | "/albums/{id}?format={format}"
    "album"    | "save"   | ["format"]                  | "POST"    | "/albums?format={format}"
    "album"    | "edit"   | ["id"]                      | "GET"     | "/albums/{id}/edit"
    "book"     | "index"  | ["format"]                  | "GET"     | "/books?format={format}"
    "book"     | "update" | ["id", "format"]            | "PUT"     | "/books/{id}?format={format}"
    "book"     | "delete" | ["id", "format"]            | "DELETE"  | "/books/{id}?format={format}"
    "book"     | "create" | []                          | "GET"     | "/books/create"
    "book"     | "show"   | ["id", "format"]            | "GET"     | "/books/{id}?format={format}"
    "book"     | "save"   | ["format"]                  | "POST"    | "/books?format={format}"
    "book"     | "edit"   | ["id"]                      | "GET"     | "/books/{id}/edit"
    "product"  | "list"   | []                          | "GET"     | "/product"
    "product"  | "other"  | []                          | "OPTIONS" | "/product/other"
    "product"  | null     | ["id"]                      | "GET"     | "/store/product/{id}"
    "generic"  | "index"  | ["format"]                  | "GET"     | "/generic.{format}"
    "generic"  | "update" | ["id", "format"]            | "PUT"     | "/generic/{id}.{format}"
    "generic"  | "delete" | ["id", "format"]            | "DELETE"  | "/generic/{id}.{format}"
    "generic"  | "create" | []                          | "OPTIONS" | "/generic/create"
    "generic"  | "show"   | ["id", "format"]            | "GET"     | "/generic/{id}.{format}"
    "generic"  | "save"   | ["format"]                  | "POST"    | "/generic.{format}"
    "generic"  | "edit"   | ["id"]                      | "GET"     | "/generic/{id}/edit"
    "generic"  | "other"  | []                          | "OPTIONS" | "/generic/other"
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

  GrailsDomainClass domain() {
    def mock = Mock(GrailsDomainClass)
    mock.getPropertyByName(_) >> { args -> property(args[0]) }
    mock.hasProperty(_) >> {args -> "format" != args[0]}
    mock
  }

  def property(name) {
    def mock = Mock(GrailsDomainClassProperty)
    mock.getType() >> String
    mock.name >> name
    mock
  }

  def controllerToTest(name, clazz) {
    def mock = Mock(GrailsControllerClass)
    mock.name >> "${StringUtils.capitalize(name)}Controller"
    mock.logicalPropertyName >> name
    mock.clazz >> clazz
    mock
  }
}
