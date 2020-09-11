public class Main {
    public static void main(String[] args) {
//        Exchanger exchanger=new Exchanger();
//        exchanger.run();

        StoreDB db = new StoreDB();
        db.run();
        HibernateUtil.shutdown();
    }
}
