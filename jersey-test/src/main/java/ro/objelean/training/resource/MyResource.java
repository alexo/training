package ro.objelean.training.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ro.objelean.training.JSONP;
import ro.objelean.training.model.Person;
import ro.objelean.training.service.SimpleService;


/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Component
@Scope("prototype")
// @Scope("request")
@Path("/hello")
public class MyResource {
  @Context
  private UriInfo uriInfo;
  @Autowired
  private SimpleService service;

  /**
   * Method processing HTTP GET requests, producing "text/plain" MIME media type.
   *
   * @return String that will be send back as a response of type "text/plain".
   */
  @GET
  @Produces({
    "application/json"
  })
  public Object getIt(@Context final HttpServletRequest request,
      @QueryParam("callback") @DefaultValue("jsoncallback") final String callback) {
    final Person person = new Person();
    person.setFirstName("alex");
    person.setLastName("objelean");
    return person;
  }

  @GET
  @Path("person/{id}.jsonp")
  @Produces({
    "application/x-javascript", MediaType.APPLICATION_JSON
  })
  public JSONP getPersonJSONP(@Context final HttpServletRequest request,
      @PathParam("id") final String id, @DefaultValue("jsoncallback") @QueryParam("callback") final String callback) {
    final Person person = new Person();
    person.setFirstName("alex-" + id);
    person.setLastName("objelean");
    return new JSONP(person, callback);
  }

  @GET
  @Path("person/{id}.json")
  @Produces({"application/json"})
  public Person getPersonJSON(@Context final HttpServletRequest request, @PathParam("id") final String id, @QueryParam("callback") final String callback) {
    final Person person = new Person();
    person.setFirstName("alex");
    person.setLastName("objelean");
    return person;
  }

  @GET
  @Path("{username}")
  @Produces(MediaType.TEXT_PLAIN)
  public String getAccount(@PathParam("username") final String username) {
    return "getAccount" + username;
  }

  @GET
  @Path("{username}/portofolios/")
  @Produces(MediaType.TEXT_PLAIN)
  public String getPortofolios(@PathParam("username") final String username) {
    return "getPortofolios" + username;
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public String getAsPost() {
    return service.sayHello() + " POST";
  }

  @DELETE
  @Produces("text/plain")
  public String getAsDelete() {
    return service.sayHello() + " DELETE";
  }

  @HEAD
  @Produces("text/plain")
  public String getAsHEAD() {
    return service.sayHello() + " HEAD";
  }

  @PUT
  @Produces("text/plain")
  public String getAsPUT() {
    return service.sayHello() + " PUT";
  }
}
