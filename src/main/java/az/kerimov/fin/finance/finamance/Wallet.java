package az.kerimov.fin.finance.finamance;

import javax.persistence.*;

@Entity(name = "fn_wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private UserCurrency currency;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "balance")
    private Double balanceAmount;

    @Column(name = "is_default")
    private boolean defaultElement;

    @Column(name = "is_active")
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public UserCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(UserCurrency currency) {
        this.currency = currency;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    @Override
    public String toString() {
        return user.getLogin()+"("+customName+"-["+balanceAmount+" "+currency.getCurrency().getShortDescription()+"]"+")";
    }
}
