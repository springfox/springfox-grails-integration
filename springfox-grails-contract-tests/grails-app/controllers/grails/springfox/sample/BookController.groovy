package grails.springfox.sample

import grails.gorm.transactions.Transactional
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import springfox.documentation.annotations.ApiIgnore

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class BookController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  @ApiOperation(value = "index", httpMethod = "GET", notes="Creates a book")
  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond Book.list(params), model:[bookCount: Book.count()]
  }

  @ApiIgnore
  def show(Book book) {
    respond book
  }

  @ApiOperation(value = "create", httpMethod = "POST", notes="Creates a book")
  @ApiImplicitParams([
      @ApiImplicitParam(name = "name", dataType = "string", required = true, paramType = "form",
          value = "Name of the book")
  ])
  def create() {
    respond new Book(params)
  }

  @Transactional
  @ApiOperation(value = "save", httpMethod = "POST", notes="Saves a book")
  def save(Book book) {
    if (book == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (book.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond book.errors, view:'create'
      return
    }

    book.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'book.label', default: 'Book'), book.id])
        redirect book
      }
      '*' { respond book, [status: CREATED] }
    }
  }

  @ApiIgnore
  def edit(Book book) {
    respond book
  }

  @Transactional
  @ApiOperation(value = "update", httpMethod = "PUT", notes="Updates a book")
  def update(Book book) {
    if (book == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (book.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond book.errors, view:'edit'
      return
    }

    book.save flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'book.label', default: 'Book'), book.id])
        redirect book
      }
      '*'{ respond book, [status: OK] }
    }
  }

  @Transactional
  @ApiOperation(value = "delete", httpMethod = "DELETE", notes="Deletes a book")
  @ApiImplicitParams([
      @ApiImplicitParam(name = "id", dataType = "long", required = true, paramType = "form",
          value = "Id of the book")
  ])
  def delete(Book book) {

    if (book == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    book.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'book.label', default: 'Book'), book.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'book.label', default: 'Book'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }
}