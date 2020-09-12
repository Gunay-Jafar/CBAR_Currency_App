package entity;

import javax.persistence.*;

@Entity
@Table(schema = "public",name = "cbar_content")
@NamedQueries({
        @NamedQuery(name = "Cbar.findAll", query = "select c from Cbar_content c"),
        @NamedQuery(name = "Cbar.findByCode", query = "select c from Cbar_content c where " +
                "c.code=:code")
})
public class Cbar_content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;

    private String nominal;

    private String value;

    private String code;

    @ManyToOne
    @JoinColumn(name = "fk_cbar_date")
    private Cbar_date cbar_date;

    public Cbar_date getCbar_date() {
        return cbar_date;
    }

    public void setCbar_date(Cbar_date cbar_date) {
        this.cbar_date = cbar_date;
    }

    public Cbar_content() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNominal() {
        return nominal;
    }

    public Cbar_content(String name, String nominal, String value, String code, Cbar_date cbar_date) {
        this.name = name;
        this.nominal = nominal;
        this.value = value;
        this.code = code;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cbar_content{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nominal='" + nominal + '\'' +
                ", value='" + value + '\'' +
                ", code='" + code + '\'' +
                '}';
    }


}
