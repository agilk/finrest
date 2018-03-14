package az.kerimov.fin.finance.finamance;

import javax.persistence.*;

@Entity(name = "fn_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "orientation_id")
    private Orientation orientation;

    private String name;

    @Column(name = "is_debt")
    private boolean debt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDebt() {
        return debt;
    }

    public void setDebt(boolean debt) {
        this.debt = debt;
    }
}
