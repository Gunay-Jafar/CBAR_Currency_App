import entity.Cbar_content;
import entity.Cbar_date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.security.auth.login.Configuration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class StoreDB {
    Session session;

    public void insertRow(String date, StringBuilder contents) {

    }

    public void run() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        session.createNativeQuery("delete from public.cbar_content").executeUpdate();
        session.createNativeQuery("delete from public.cbar_date").executeUpdate();

        Cbar_date cd = new Cbar_date();
        cd.setDate(LocalDate.now());
        session.save(cd);


        Cbar_content cc = new Cbar_content();
        cc.setCbar_date(cd);
        cc.setCode("USD");
        cc.setName("dollar");
        cc.setNominal("1 usd");
        cc.setValue("1.00");
        session.save(cc);

        session.getTransaction().commit();

        System.out.println("JPQL: " + session.createQuery("select cc.value from Cbar_content cc " +
                "where cc.cbar_date.date='2020-09-11' and cc.code='USD' ").getSingleResult());

        System.out.println("Native: " + session.createNativeQuery("select value " +
                "from public.cbar_content cc " +
                "join public.cbar_date cd on cd.id=cc.fk_cbar_date " +
                "where cd.date='2020-09-11' and cc.code='USD'").getSingleResult());

//        TypedQuery<Cbar_content> query = session.createNamedQuery("Cbar.findByCode", Cbar_content.class)
//                .setParameter("code", "USD");
//
//        System.out.println("Named query: " + query.getSingleResult().getValue());
//        session.close();


    }
}