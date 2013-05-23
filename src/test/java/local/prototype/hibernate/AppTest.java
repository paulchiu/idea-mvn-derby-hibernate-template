package local.prototype.hibernate;

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */

import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.service.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

/**
 * Test based on work by Steve Ebersole downloaded from http://javasourcecode.org/html/open-source/hibernate/hibernate-3.6.0.Final/org/hibernate/tutorial/annotations/AnnotationsIllustrationTest.java.html
 *
 * Successful tests confirms the correct installation/configuration of Hibernate with annotations.
 *
 * @author Paul Chiu
  */
public class AppTest 
    extends TestCase
{

    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session session;

    @Override
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        Configuration configuration = new Configuration().configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp()
    {
        assertTrue( true );
    }

    public void testInsert() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.save( new Event( "Our very first event!", new Date() ) );
        session.save( new Event( "A follow up event", new Date() ) );
        session.getTransaction().commit();
        session.close();
    }        

    public void testSelect() {
        List result = getEvents();
        for ( Event event : (List<Event>) result )
            System.out.println( "Event (" + event.getId() + ") : " + event.getTitle() );
    }

    private List getEvents() {
        session = sessionFactory.openSession();
        List result = session.createQuery( "from Event" ).list();
        session.close();
        return result;
    }

    public void testSelectById() {
        assertEquals("Title match", getEventById(1L).getTitle(), "Our very first event!");
    }

    private Event getEventById(Long id) {
        session = sessionFactory.openSession();
        Query selectQuery = getSelectByIdQuery(id);
        List results = selectQuery.list();
        if (results.size() == 1)
            return (Event)results.get(0);
        else
            return new Event();
    }

    public void testUpdate() {
        Event eventOld = getEventById(1L);
        eventOld.setTitle("hello world");
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(eventOld);
        session.getTransaction().commit();
        session.close();
        Event eventNew = getEventById(1L);
        assertEquals("Updated title match", eventOld.getTitle(), eventNew.getTitle());
    }

    private Query getSelectByIdQuery(Long id) {
        Query query = session.createQuery("from Event where id = :id");
        query.setParameter("id", id);
        return query;
    }

    public void testDelete() {
        List result = getEvents();
        session = sessionFactory.openSession();
        session.beginTransaction();
        for ( Event event : (List<Event>) result )
            session.delete(event);
        session.getTransaction().commit();
        session.close();

        assertEquals("All events deleted", 0, getEvents().size());
    }
}
