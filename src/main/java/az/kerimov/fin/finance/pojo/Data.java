package az.kerimov.fin.finance.pojo;

import az.kerimov.fin.finance.finamance.Currency;
import az.kerimov.fin.finance.finamance.Rate;
import az.kerimov.fin.finance.finamance.User;
import az.kerimov.fin.finance.finamance.Wallet;
import az.kerimov.fin.finance.finamance.Category;
import az.kerimov.fin.finance.finamance.SubCategory;
import az.kerimov.fin.finance.finamance.Orientation;

import java.util.List;

public class Data {
    private List<Currency> currencies;
    private Currency currency;
    private List<Rate> rates;
    private Rate rate;

    private User user;
    private List<Wallet> wallets;
    private Wallet wallet;
    private Integer newId;
    private String sessionKey;

    private Orientation orientation;
    private List<Orientation> orientations;
    private Category category;
    private List<Category> categories;
    private SubCategory subCategory;
    private List<SubCategory> subCategories;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Integer getNewId() {
        return newId;
    }

    public void setNewId(Integer newId) {
        this.newId = newId;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public List<Orientation> getOrientations() {
        return orientations;
    }

    public void setOrientations(List<Orientation> orientations) {
        this.orientations = orientations;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
