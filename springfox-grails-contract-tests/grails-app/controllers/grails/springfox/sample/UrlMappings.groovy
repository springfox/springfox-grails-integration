package grails.springfox.sample

class UrlMappings {

  static mappings = {
    delete "/$controller/$id(.$format)?"(action: "delete")
    get "/$controller(.$format)?"(action: "index")
    get "/$controller/$id(.$format)?"(action: "show")
    post "/$controller(.$format)?"(action: "save")
    put "/$controller/$id(.$format)?"(action: "update")
    patch "/$controller/$id(.$format)?"(action: "patch")

    "/"(controller: 'application', action: 'index')
    "500"(view: '/error')
    "404"(view: '/notFound')

    //for testing
    group "/store", {
      group "/product", {
        "/$id"(controller: "product")
      }
    }
    group "/product", {
      "/apple"(controller:"product", id:"apple")
      "/htc"(controller:"product", id:"htc")
    }
    "/product"(controller: "product", action: "list")
    "/books"(resources:'book')
    "/books"(resources:'book', excludes:['delete', 'update'])
    "/books"(resources:'book', includes:['index', 'show'])
    "/books"(resources:'book') {
      "/authors"(resources:"author")
    }
  }
}
