# idea-mvn-derby-hibernate-template #

IntelliJ IDEA 12 CE project template with Maven, demonstrating access to Apache Derby 10.10.1.1 with Hibernate 4.2.1.

The project implements the [hibernate annotations tutorial][1] with custom test class AppTest.java.

[1]:http://docs.jboss.org/hibernate/orm/3.6/quickstart/en-US/html/hibernate-gsg-tutorial-annotations.html

## How to use ##

1. Clone.
1. Open project in IntelliJ IDEA.
1. Click File->Project Structure.
1. Set the project JDK.
1. Launch Apache Derby Server ([instructions][2]).
1. Update src/main/resources/hibernate.cfg.xml ; specifically properties for:
   * connection.url
   * connection.username
   * connection.password
1. Click on View->Tool Windows->Maven Projects, then.
   1. Expand the project.
   1. Expand Lifecycle.
   1. Choose test.
   1. Click "Run Maven Build".

If all tests pass, everything has been successfully set up. If there are test failures, check:

* Your Derby server is set up properly.
* Your hibernate.cfg.xml file contains correct settings.

[2]:http://db.apache.org/derby/docs/10.10/adminguide/tadmincbdjhhfd.html
