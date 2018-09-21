package com.erp.pattanasin.server;
 
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
 
import com.erp.pattanasin.server.HelloWorld;
 
public class HelloWorldApplication extends Application
{
    private Set<Object> singletons = new HashSet();
    private Set<Class<?>> empty = new HashSet();
 
    public HelloWorldApplication() {
        // ADD YOUR RESTFUL RESOURCES HERE
        this.singletons.add(new HelloWorld());
        this.singletons.add(new LoginServlet());
        //this.singletons.add(new SetTableID());
        //this.singletons.add(new Restaurant());
    }
 
    public Set<Class<?>> getClasses()
    {
        return this.empty;
    }
 
    public Set<Object> getSingletons()
    {
        return this.singletons;
    }
}