package az.kerimov.fin.finance.finamance;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "fn_transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "wallet_id_other")
    private Wallet walletOther;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private UserCurrency currency;

    @ManyToOne
    @JoinColumn(name = "orientation_id")
    private Orientation orientation;

    @ManyToOne
    @JoinColumn(name = "rate_transaction")
    private Rate rateTransaction;

    @ManyToOne
    @JoinColumn(name = "rate_wallet")
    private Rate rateWallet;

    @ManyToOne
    @JoinColumn(name = "rate_wallet_other")
    private Rate rateWalletOther;

    @ManyToOne
    @JoinColumn(name = "rate_usd")
    private Rate rateUsd;

    @JoinColumn(name = "amount_usd")
    private Double amountUsd;

    private Double amount;

    private String notes;

    private String cdate;

    private Date ctime;

    private Date added;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWalletOther() {
        return walletOther;
    }

    public void setWalletOther(Wallet walletOther) {
        this.walletOther = walletOther;
    }

    public UserCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(UserCurrency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Rate getRateTransaction() {
        return rateTransaction;
    }

    public void setRateTransaction(Rate rateTransaction) {
        this.rateTransaction = rateTransaction;
    }

    public Rate getRateWallet() {
        return rateWallet;
    }

    public void setRateWallet(Rate rateWallet) {
        this.rateWallet = rateWallet;
    }

    public Rate getRateWalletOther() {
        return rateWalletOther;
    }

    public void setRateWalletOther(Rate rateWalletOther) {
        this.rateWalletOther = rateWalletOther;
    }

    public Rate getRateUsd() {
        return rateUsd;
    }

    public void setRateUsd(Rate rateUsd) {
        this.rateUsd = rateUsd;
    }

    public Double getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(Double amountUsd) {
        this.amountUsd = amountUsd;
    }
}
