package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import spock.lang.Specification

import static springfox.documentation.grails.Parameters.*

class ParametersSpec extends Specification {
  def resolvedType = new TypeResolver().resolve(String)

  def "creates path parameter"() {
    given:
      def sut = pathParameter(0, "test", resolvedType)
    expect:
      sut.defaultName().isPresent()
      sut.defaultName().get() == "test"
      sut.annotations.size() == 1
      sut.parameterType == resolvedType
    and:
      PathVariable annotation = sut.annotations.get(0)
      annotation.name() == "test"
      annotation.required()
      annotation.value() == "test"
  }

  def "creates query parameter"() {
    given:
    def sut = queryParameter(0, "test", resolvedType, required, "default")
    expect:
      sut.defaultName().isPresent()
      sut.defaultName().get() == "test"
      sut.annotations.size() == 1
      sut.parameterType == resolvedType
    and:
      RequestParam annotation = sut.annotations.get(0)
      annotation.name() == "test"
      annotation.required() == required
      annotation.defaultValue() == "default"
    where:
      required << [true, false]
  }

  def "creates body parameter"() {
    given:
      def sut = bodyParameter(0, resolvedType)
    expect:
      sut.defaultName().isPresent()
      sut.defaultName().get() == "body"
      sut.annotations.size() == 1
      sut.parameterType == resolvedType
    and:
      RequestBody annotation = sut.annotations.get(0)
      annotation.required()
  }
}
