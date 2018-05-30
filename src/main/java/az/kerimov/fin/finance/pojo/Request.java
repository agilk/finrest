package az.kerimov.fin.finance.pojo;

import az.kerimov.fin.finance.exception.RequestException;

public class Request {
    //for Currencies
    private String currencyCode;
    private String currencyShortDescription;
    private String currencyDescription;

    //for Rates
    private Double rate;
    private String rateDate;

    //for Users
    private String login;
    private String password;
    private Integer userId;
    private Integer walletId;
    private String customName;
    private Double newBalance;
    private String firstName;
    private String lastName;
    private String mobilePhone;

    //for Categories
    private Integer orientationId;
    private Integer categoryId;
    private Integer subCategoryId;
    private Boolean debt;

    //for Transactions
    private Double amount;
    private String notes;
    private String date;
    private Integer walletIdOther;
    private Integer transactionId;
    private String startDate;
    private String endDate;

    //Common
    private String lang;
    private String sessionKey;
    private Boolean defaultElement;

    public String getCurrencyCode() throws RequestException  {
        if (currencyCode == null) throw new RequestException();
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyShortDescription() {
        return currencyShortDescription;
    }

    public void setCurrencyShortDescription(String currencyShortDescription) {
        this.currencyShortDescription = currencyShortDescription;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public Double getRate() throws RequestException {
        if ( rate == null) throw new RequestException();
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getRateDate() throws RequestException {
        if (rateDate == null) throw new RequestException();
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getLogin()  throws RequestException {
        if (login == null) throw new RequestException();
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() throws RequestException  {
        if (password == null) throw new RequestException();
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWalletId() throws RequestException  {
        if (walletId == null) throw new RequestException();
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public String getCustomName() throws RequestException  {
        if (customName == null) throw new RequestException();
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Double getNewBalance()  {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

    public Integer getOrientationId() throws RequestException  {
        if (orientationId == null) throw new RequestException();
        return orientationId;
    }

    public void setOrientationId(Integer orientationId) {
        this.orientationId = orientationId;
    }

    public Integer getCategoryId() throws RequestException  {
        if (categoryId == null) throw new RequestException();
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategoryId() throws RequestException  {
        if (subCategoryId == null) throw new RequestException();
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Boolean isDebt() {
        return debt;
    }

    public void setDebt(Boolean debt) {
        this.debt = debt;
    }

    public Double getAmount() throws RequestException  {
        if (amount == null) throw new RequestException();
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

    public String getDate() throws RequestException  {
        if (date == null) throw new RequestException();
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWalletIdOther() {
        return walletIdOther;
    }

    public void setWalletIdOther(Integer walletIdOther) {
        this.walletIdOther = walletIdOther;
    }

    public Integer getTransactionId()  {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Boolean getDebt() {
        return debt;
    }

    public Boolean getDefault() {
        return defaultElement;
    }

    public void setDefault(Boolean defaultElement) {
        this.defaultElement = defaultElement;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
