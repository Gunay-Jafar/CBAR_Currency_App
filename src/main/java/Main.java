import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
//        Exchanger exchanger=new Exchanger();
//        exchanger.run();

//        StoreDB db = new StoreDB();
//        db.run();
//        HibernateUtil.shutdown();

        DBExchanger db = new DBExchanger();
        db.run();

    }
}
