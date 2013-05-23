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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test based on work by Steve Ebersole downloaded from http://javasourcecode.org/html/open-source/hibernate/hibernate-3.6.0.Final/org/hibernate/tutorial/annotations/AnnotationsIllustrationTest.java.html
 * <p/>
 * Successful tests confirms the correct installation/configuration of Hibernate with annotations.
 *
 * @author Paul Chiu
 */
public class AppTest extends TestCase {
    public static final String EVENT_ONE_TITLE = "Our very first event!";
    public static final String EVENT_TWO_TITLE = "A follow up event";
    public static final String UPDATED_EVENT_TITLE = "Updated event!";
    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private Session session;

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        Configuration configuration = new Configuration().configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    protected void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public void testApp() {
        assertTrue(true);
    }

    public void testInsert() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new Event(EVENT_ONE_TITLE, new Date()));
        session.save(new Event(EVENT_TWO_TITLE, new Date()));
        session.getTransaction().commit();
        session.close();
    }

    public void testSelect() {
        List<Event> result = getEvents();
        for (Event event : result)
            System.out.println("Event (" + event.getId() + ") : " + event.getTitle());
    }

    @SuppressWarnings("unchecked")
    private List<Event> getEvents() {
        session = sessionFactory.openSession();
        List<Event> result = session.createQuery("from Event").list();
        session.close();
        return result;
    }

    public void testSelectById() {
        assertEquals("Title match", getEventById(1L).getTitle(), EVENT_ONE_TITLE);
    }

    private Event getEventById(Long id) {
        session = sessionFactory.openSession();
        Query selectQuery = getSelectByIdQuery(id);
        List results = selectQuery.list();
        if (results.size() == 1)
            return (Event) results.get(0);
        else
            return new Event();
    }

    private Query getSelectByIdQuery(Long id) {
        Query query = session.createQuery("from Event where id = :id");
        query.setParameter("id", id);
        return query;
    }

    public void testUpdate() {
        Event eventOld = getEventById(1L);
        eventOld.setTitle(UPDATED_EVENT_TITLE);
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(eventOld);
        session.getTransaction().commit();
        session.close();
        assertEquals("Updated title match", eventOld.getTitle(), getEventById(1L).getTitle());
    }

    public void testDelete() {
        List<Event> result = getEvents();
        session = sessionFactory.openSession();
        session.beginTransaction();
        for (Event event : result)
            session.delete(event);
        session.getTransaction().commit();
        session.close();
        assertEquals("All events deleted", 0, getEvents().size());
    }
}
