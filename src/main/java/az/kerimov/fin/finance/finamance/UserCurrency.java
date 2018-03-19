package az.kerimov.fin.finance.finamance;

import javax.persistence.*;

@Entity(name = "fn_user_currencies")
public class UserCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "is_default")
    private boolean defaultElement;

    public boolean isDefaultElement() {
        return defaultElement;
    }

    public void setDefaultElement(boolean defaultElement) {
        this.defaultElement = defaultElement;
    }

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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
