package grails.springfox.sample

import grails.test.mixin.integration.Integration
import groovy.json.JsonOutput
import org.junit.Assert
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.boot.context.embedded.LocalServerPort
import spock.lang.Specification

import static io.restassured.RestAssured.get

@Integration
class SpringFoxSpec extends Specification implements FileAccess {
    @LocalServerPort
    private int port
    void "test something"() {
      given:
        def expected = fileContents("/expected-service-description.json")
        def actual = get("http://localhost:$port/v2/api-docs").asString()

      expect:
        try {
          JSONAssert.assertEquals(
              expected.replaceAll("__PORT__", "$port"),
              actual,
              JSONCompareMode.NON_EXTENSIBLE)
        } catch (AssertionError e) {
          Assert.fail("${e.getMessage()}${System.getProperty("line.separator")}${JsonOutput.prettyPrint(actual)}")
        }


    }
}
