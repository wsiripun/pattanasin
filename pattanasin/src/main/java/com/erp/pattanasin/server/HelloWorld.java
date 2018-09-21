package com.erp.pattanasin.server;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


 
// http://localhost:8080/pattanasin/tutorial/...		// 'tutorial' is the base.
// (Assume that the war file is 'pattanasin.war')
@Path("tutorial")
public class HelloWorld
{
	// Tomcat is at port 591.
	// URL:  http://localhost:591/pattanasin/tutorial/helloworld
    @GET
    @Path("helloworld")
    public String helloworld() {
        return "pattanasin  Hello World! Tomcat 8.5 Port 591";
    }
    
    // URL:  http://localhost:591/pattanasin/tutorial/helloname/francesco123f
    @GET
    @Path("helloname/{name}")
    public String hello(@PathParam("name") final String name) {
      return "Hello from: " +name;
    }
    
    // URI:   http://localhost:591/pattanasin/tutorial/item

    @GET
//    @Path("item")
//    @Produces({"application/json"})
//    public Item  getItem() {
//     
//      Item item = new Item("Restaurant 123 Bcomputer555",2600);
//     
//     return item;
//    }
    
    
// HOW TO PASS SIMPLE DATA FROM CLIENT TO SERVER.   <-- worked.  tested on 9/23/17
// CLIENT CODES:
//
//	$.post("/pattanasin/tutorial/multipleparms",
//	        {
//	          parm1: "post call",
//	          parm2: 12345,
//	          parm3: true
//	        },
//	        function(data,status){
//	            alert("Data: " + data + "\nStatus: " + status);
//	         });
//		
//		
//		$.ajax({
//		    type: 'POST',
//		    url: '/pattanasin/tutorial/multipleparms',
//		    contentType: 'application/x-www-form-urlencoded',
//		    data: { parm1: "ajax() call", parm2: 4567, parm3: false },
//		    dataType: 'text',
//		    success:  function(data,status){
//	            alert("Data: " + data + "\nStatus: " + status);
//	        }
//
//		});    
    
    
    @POST
    @Path("multipleparms")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED )
    public String multipleParm(@FormParam("parm1") String parm1, 
    						 @FormParam("parm2") int parm2,
    						 @FormParam("parm3") boolean parm3) {
    	System.out.println("multipleParm(): 222 parm1=" + parm1 + "   parm2=" + parm2  + "  parm3=" + parm3);
    	return parm1;
    	
    }
}