import entity.Cbar_content;
import entity.Cbar_date;
import org.hibernate.Session;

import java.util.Date;

public class StoreDB {

    public void run(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Cbar_date cd = new Cbar_date();
        cd.setDate(new Date());
        session.save(cd);


        Cbar_content cc = new Cbar_content();
        cc.setCbar_date(cd);
        cc.setCode("USD");
        cc.setName("dollar");
        cc.setNominal("1 usd");
        cc.setValue("1.00");
        session.save(cc);

        session.getTransaction().commit();

        session.close();
    }
}