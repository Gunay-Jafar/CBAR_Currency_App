package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "public",name = "cbar_date")
public class Cbar_date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private Date date;

    public Cbar_date() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Cbar_date(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Cbar_date{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }


}
