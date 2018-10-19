package az.kerimov.fin.finance.finamance;

import az.kerimov.fin.finance.exception.AddCurrencyException;
import az.kerimov.fin.finance.exception.UserNotFoundException;
import az.kerimov.fin.finance.pojo.Request;
import az.kerimov.fin.finance.pojo.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FinService {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private SubCategoryVRepository subCategoryVRepository;
    @Autowired
    private OrientationRepository orientationRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionReportRepository transactionReportRepository;
    @Autowired
    private UserCurrencyRepository userCurrencyRepository;

    private DateFormat df = new SimpleDateFormat("yyyyMMdd");

    private DateFormat dfTransactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormat dfTransaction = new SimpleDateFormat("yyyyMMdd");

/*
    public String getLatestRateDate() throws ParseException {
        List<Rate> rates = rateRepository.findAll();
        Date currentDate = new Date(0);
        Date rateDate;
        for (Rate rate : rates) {
            rateDate = df.parse(rate.getCtime());
            if (currentDate.getTime() < rateDate.getTime()) {
                currentDate = rateDate;
            }
        }
        return df.format(currentDate);
    }
*/
    private String getCurrencyLatestRateDate(UserCurrency currency, String ctime) throws ParseException {
        List<Rate> rates = rateRepository.findByCurrencyAndCtimeIsLessThanEqual(currency, ctime);
        Date currentDate = new Date(0);
        Date rateDate;
        for (Rate rate : rates) {
            rateDate = df.parse(rate.getCtime());
            if (currentDate.getTime() < rateDate.getTime()) {
                currentDate = rateDate;
            }
        }
        return df.format(currentDate);
    }

    private Rate getRateForDate(UserCurrency currency, String date) throws ParseException {
        return rateRepository.findByCurrencyAndCtime(currency,
                getCurrencyLatestRateDate(currency, date));
    }

    private Rate getRateForDate(UserCurrency currency, Date date) throws ParseException {
        return getRateForDate(currency, df.format(date));
    }

    public Rate getRateForDate(String sessionKey, String currencyCode, String date) throws ParseException, UserNotFoundException {
        return getRateForDate(userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(
                getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode)
        ), date);
    }

    public List<Rate> getLatestRates() throws ParseException {
        List<UserCurrency> currencies = userCurrencyRepository.findAll();
        List<Rate> rates = new ArrayList<>();
        for (UserCurrency currency : currencies) {
            if (currency.getId() != 0)
                rates.add(getRateForDate(currency, new Date()));
        }
        return rates;
    }

    private void addRate(UserCurrency currency, String date, Double rate) {
        Rate result;
        result = rateRepository.findByCurrencyAndCtime(currency, date);
        if (result == null){
            result = new Rate();
            result.setCurrency(currency);
            result.setCtime(date);
        }
        result.setRate(rate);
        rateRepository.save(result);
    }

    public void addRate(String sessionKey, String currencyCode, String date, Double rate) throws UserNotFoundException {
        addRate(userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode)), date, rate);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAllByIdGreaterThan(0);
    }

    public List<UserCurrency> getAllUserCurrencies(String sessionKey) throws UserNotFoundException {
        return userCurrencyRepository.findAllByUserAndActiveIsTrue(getUserBySessionKey(sessionKey));
    }

    public UserCurrency getUserCurrencyByCode(String sessionKey, String code) throws UserNotFoundException {
        return userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(getUserBySessionKey(sessionKey), currencyRepository.findByCode(code));
    }

    private Integer addCurrency(UserCurrency currency) throws AddCurrencyException {
        try {
            userCurrencyRepository.save(currency);
            return currency.getId();
        } catch (Exception ex) {
            throw new AddCurrencyException();
        }
    }

    public Integer addCurrency(String sessionKey, String currencyCode) throws AddCurrencyException, UserNotFoundException {
        UserCurrency currency = new UserCurrency();
        currency.setUser(getUserBySessionKey(sessionKey));
        currency.setCurrency(currencyRepository.findByCode(currencyCode));
        currency.setActive(true);
        UserCurrency existCurr = userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(currency.getUser(), currency.getCurrency());
        if (existCurr != null){
            currency.setId(existCurr.getId());
        }
        return addCurrency(currency);
    }

    private void setDefaultCurrency(UserCurrency currency){
        List<UserCurrency> userCurrencies = userCurrencyRepository.findAllByUserAndActiveIsTrueAndDefaultElementIsTrue(currency.getUser());
        for (UserCurrency userCurrency : userCurrencies) {
            userCurrency.setDefaultElement(false);
            try {
                userCurrencyRepository.save(userCurrency);
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
        currency.setDefaultElement(true);
        userCurrencyRepository.save(currency);
        //return currency.getId();
    }

    public void setDefaultCurrency(String sessionKey, String currencyCode) throws UserNotFoundException {
        UserCurrency currency = userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode));
        setDefaultCurrency(currency);
    }

    private void deleteCurrency(UserCurrency currency){
        currency.setActive(false);
        currency.setDefaultElement(false);
        userCurrencyRepository.save(currency);
    }

    public void deleteCurrency(String sessionKey, String currencyCode) throws UserNotFoundException{
        UserCurrency currency = userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode));
        deleteCurrency(currency);
    }

    public Log writeRequestLog(String method, String sessionKey, Request request) {
        Log log = new Log();
        Gson gson = new Gson();
        String logString = gson.toJson(request);
        log.setLogDate(new Date());
        log.setLogDay(df.format(log.getLogDate()));
        log.setLogType("REQUEST");
        log.setLogString(logString);
        log.setProcName(method);
        log.setSessionKey(sessionKey);
        logRepository.save(log);
        return log;
    }

    public void writeResponseLog(Log logParent, String method, String sessionKey, Response response) {
        Log log = new Log();
        Gson gson = new Gson();
        String logString = gson.toJson(response);
        log.setLogDate(new Date());
        log.setLogDay(df.format(log.getLogDate()));
        log.setLogType("RESPONSE");
        log.setLogString(logString);
        log.setProcName(method);
        log.setSessionKey(sessionKey);
        log.setParentLog(logParent.getId());
        logRepository.save(log);
    }


    private String generateSessionKey() throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String hex = Integer.toHexString(0xFF & aMessageDigest);
            if (hex.length() == 1) {
                // could use a for loop, but we're only dealing with a single
                // byte
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Session getSessionByLoginPassword(String login, String password) throws UserNotFoundException, NoSuchAlgorithmException {
        User user = userRepository.findByLoginAndPassword(login, password);
        if (user == null) throw new UserNotFoundException();
        setAllSessionsInActive(user);
        Session session = new Session();
        session.setSessionKey(generateSessionKey());
        session.setUser(user);
        session.setActive(true);
        sessionRepository.save(session);
        return session;
    }

    public User getUserBySessionKey(String sessionKey) throws UserNotFoundException {
        Session session = sessionRepository.findBySessionKeyAndActiveIsTrue(sessionKey);
        if (session.getUser() == null) throw new UserNotFoundException();
        return session.getUser();
    }

    private void setAllSessionsInActive(User user) {
        List<Session> sessions = sessionRepository.findAllByUserAndActiveIsTrue(user);
        for (Session session : sessions) {
            session.setActive(false);
            sessionRepository.save(session);
        }
    }

    public void logoff(String sessionKey) throws UserNotFoundException {
        setAllSessionsInActive(getUserBySessionKey(sessionKey));
    }

    private void changePassword(User user, String newPassword){
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void changePassword(String sessionKey, String newPassword) throws UserNotFoundException {
        changePassword(getUserBySessionKey(sessionKey), newPassword);
    }

    private List<Wallet> getUserWallets(User user) {
        return walletRepository.findByUserAndActiveIsTrue(user);
    }

    public List<Wallet> getUserWallets(String sessionKey) throws UserNotFoundException {
        User user = getUserBySessionKey(sessionKey);
        return getUserWallets(user);
    }

    private List<Wallet> getUserInActiveWallets(User user){
        return walletRepository.findByUserAndActiveIsFalse(user);
    }

    public List<Wallet> getUserInActiveWallets(String sessionKey) throws UserNotFoundException {
        return getUserInActiveWallets(getUserBySessionKey(sessionKey));
    }

    private void deleteWallet(Wallet wallet){
        wallet.setActive(false);
        walletRepository.save(wallet);
    }

    public void deleteWallet(String sessionKey, Integer walletId) throws UserNotFoundException {
        deleteWallet(getWalletById(sessionKey, walletId));
    }

    private Integer addWallet(Wallet wallet) {
        wallet.setActive(true);
        walletRepository.save(wallet);
        return wallet.getId();
    }

    public Integer activateWallet(String sessionKey, Integer walletId) throws UserNotFoundException {
        return addWallet(getWalletById(sessionKey, walletId));
    }

    public Integer addWallet(String sessionKey, String currencyCode, String customName) throws UserNotFoundException {
        Wallet wallet = new Wallet();
        User user = getUserBySessionKey(sessionKey);
        wallet.setUser(user);
        wallet.setBalanceAmount(0.0);
        wallet.setCurrency(getUserCurrencyByCode(sessionKey, currencyCode));
        wallet.setCustomName(customName);
        return addWallet(wallet);
    }

    private void setDefaultWallet(Wallet wallet){
        List<Wallet> wallets = walletRepository.findAllByUserAndDefaultElementIsTrue(wallet.getUser());
        for (Wallet wallet1 : wallets) {
            wallet1.setDefaultElement(false);
            try {
                walletRepository.save(wallet1);
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
        wallet.setDefaultElement(true);
        walletRepository.save(wallet);
        //return wallet.getId();
    }

    public void setDefaultWallet(String sessionKey, Integer walletId) throws UserNotFoundException {
        Wallet wallet = walletRepository.findByIdAndUser(walletId, getUserBySessionKey(sessionKey));

        setDefaultWallet(wallet);
    }


    public Wallet changeWalletBalance(Wallet wallet, Double balance) {
        wallet.setBalanceAmount(balance);
        walletRepository.save(wallet);
        return wallet;
    }

    public Wallet getWalletById(String sessionKey, Integer id) throws UserNotFoundException {
        return walletRepository.findByIdAndUser(id, getUserBySessionKey(sessionKey));
    }

    private Wallet getWalletById(Integer id, User user)  {
        return walletRepository.findByIdAndUser(id, user);
    }

    private List<Category> getCategories(User user) {
        return categoryRepository.findByUserAndActiveIsTrue(user);
    }

    public List<Category> getCategories(String sessionKey) throws UserNotFoundException {
        return getCategories(getUserBySessionKey(sessionKey));
    }

    private List<SubCategory> getSubCategories(User user, Category category) {
        List<SubCategory> res = new ArrayList<>();
        List<SubCategoryV> vCats = subCategoryVRepository.findByUserAndCategoryAndActiveIsTrueOrderByTransactionsDesc(user, category);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(vCats);
        res = gson.fromJson(jsonString, List.class);

        return res;

        //return subCategoryRepository.findByUserAndCategoryAndActiveIsTrue(user, category);
    }

    public List<SubCategory> getSubCategories(String sessionKey, Integer categoryId) throws UserNotFoundException {
        return getSubCategories(getUserBySessionKey(sessionKey), getCategoryById(categoryId, getUserBySessionKey(sessionKey)));
    }

    private Category getCategoryById(Integer id, User user) {
        return categoryRepository.findByIdAndUser(id, user);
    }

    public Category getCategoryById(Integer id, String sessionKey) throws UserNotFoundException {
        return categoryRepository.findByIdAndUser(id, getUserBySessionKey(sessionKey));
    }

    private SubCategory getSubCategoryById(Integer id, User user) {
        return subCategoryRepository.findByIdAndUser(id, user);
    }

    public SubCategory getSubCategoryById(Integer id, String sessionKey) throws UserNotFoundException {
        return subCategoryRepository.findByIdAndUser(id, getUserBySessionKey(sessionKey));
    }

    public List<Orientation> getOrientations() {
        return orientationRepository.findAll();
    }

    private Orientation getOrientationById(Integer id) {
        return orientationRepository.findById(id);
    }

    private void deleteCategory(Category category){
        category.setActive(false);
        categoryRepository.save(category);
    }

    public void deleteCategory(String sessionKey, Integer categoryId) throws UserNotFoundException {
        deleteCategory(getCategoryById(categoryId, getUserBySessionKey(sessionKey)));
    }

    private Integer addCategory(Category category) {
        category.setActive(true);
        categoryRepository.save(category);
        return category.getId();
    }

    public Integer addCategory(String sessionKey, Integer orientationId, String name, boolean debt) throws UserNotFoundException {
        Category category = new Category();
        category.setDebt(debt);
        category.setName(name);
        category.setOrientation(getOrientationById(orientationId));
        category.setUser(getUserBySessionKey(sessionKey));
        return addCategory(category);
    }

    private void deleteSubCategory(SubCategory subCategory){
        subCategory.setActive(false);
        subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(String sessionKey, Integer subCategoryId) throws UserNotFoundException {
        deleteSubCategory(getSubCategoryById(subCategoryId, getUserBySessionKey(sessionKey)));
    }

    private Integer addSubCategory(SubCategory subCategory) {
        subCategory.setActive(true);
        subCategoryRepository.save(subCategory);
        return subCategory.getId();
    }

    public Integer addSubCategory(String sessionKey, Integer categoryId, String name) throws UserNotFoundException {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(getCategoryById(categoryId, getUserBySessionKey(sessionKey)));
        subCategory.setName(name);
        subCategory.setUser(getUserBySessionKey(sessionKey));
        return addSubCategory(subCategory);
    }

    private List<TransactionReport> getLastTransactions(User user) {
        List<TransactionReport> list = transactionReportRepository.findAllByUserOrderByIdDesc(user);
        List<TransactionReport> result = new ArrayList<>();
        int max = 10;
        if (list.size() < 10) {
            max = list.size();
        }

        for (int i = 0; i < max; i++) {
            result.add(list.get(i));
        }
        return result;
    }

    private List<Report> getReportList(User user){
        List<Report> reports = new ArrayList<>();
        Report report = new Report();
        report.setId(1);
        report.setName("getTransactionsBetweenDates");
        List<ReportParam> params = new ArrayList<>();
        ReportParam param = new ReportParam();
        param.setName("startDate");
        param.setLabel("Start Date");
        param.setType("date");
        params.add(param);
        param.setName("endDate");
        param.setLabel("End Date");
        param.setType("date");
        params.add(param);
        report.setParamList(params);
        reports.add(report);
        return reports;
    }

    public List<Report> getReportList(String sessionKey) throws UserNotFoundException {
        return getReportList(getUserBySessionKey(sessionKey));
    }

    public List<TransactionReport> getLastTransactions(String sessionKey) throws UserNotFoundException {
        return getLastTransactions(getUserBySessionKey(sessionKey));
    }

    private List<TransactionReport> getTransactionsBetweenDates(User user, String begDate, String endDate){
        return transactionReportRepository.findAllByUserAndCdateBetween(user, begDate, endDate);
    }

    public List<TransactionReport> getTransactionsBetweenDates(String sessionKey, String begDate, String endDate) throws UserNotFoundException {
        return getTransactionsBetweenDates(getUserBySessionKey(sessionKey), begDate, endDate);
    }

    private Integer addTransaction(Transaction transaction) throws ParseException {
        TransactionDetails transactionDetails = getTransactionDetails(transaction);
        transaction.setRateTransaction(transactionDetails.getRateTransaction());
        transaction.setRateWallet(transactionDetails.getRateWallet());
        transaction.setRateWalletOther(transactionDetails.getRateWalletOther());
        transaction.setRateUsd(transactionDetails.getRateUsd());
        transaction.setAmountUsd(transactionDetails.getAmountUsd());
        transactionRepository.save(transaction);
        Double balanceChange = calculateTransactionAmount(transaction, true);
        changeWalletBalance(transaction.getWallet(), transaction.getWallet().getBalanceAmount() - balanceChange);
        if (transaction.getWalletOther() != null) {
            balanceChange = calculateTransactionAmount(transaction, false);
            changeWalletBalance(transaction.getWalletOther(), transaction.getWalletOther().getBalanceAmount() + balanceChange);
        }
        return transaction.getId();
    }


    public Integer addTransaction(
            String sessionKey,
            Integer categoryId,
            Integer subCategoryId,
            String currencyCode,
            Double amount,
            String notes,
            String date,
            Integer walletId,
            Integer walletIdOther

    ) throws ParseException, UserNotFoundException {
        Transaction transaction = new Transaction();
        transaction.setAdded(new Date());
        transaction.setUser(getUserBySessionKey(sessionKey));
        transaction.setCategory(getCategoryById(categoryId, getUserBySessionKey(sessionKey)));
        transaction.setSubCategory(getSubCategoryById(subCategoryId, getUserBySessionKey(sessionKey)));
        transaction.setCurrency(getUserCurrencyByCode(sessionKey, currencyCode));
        transaction.setAmount(amount);
        transaction.setNotes(notes);
        transaction.setCdate(date);
        transaction.setCtime(dfTransactionDate.parse(date));
        transaction.setWallet(getWalletById(walletId, getUserBySessionKey(sessionKey)));
        if (walletIdOther != null) {
            transaction.setWalletOther(getWalletById(walletIdOther, getUserBySessionKey(sessionKey)));
        }
        transaction.setOrientation(transaction.getCategory().getOrientation());
        return addTransaction(transaction);
    }

    private Double calculateTransactionAmount(Transaction transaction, boolean debtWallet) throws ParseException {
        Integer sign = transaction.getCategory().getOrientation().getSign();
        UserCurrency walletCurrency;
        if (debtWallet)
            walletCurrency = transaction.getWallet().getCurrency();
        else {
            walletCurrency = transaction.getWalletOther().getCurrency();
        }

        UserCurrency transactionCurrency = transaction.getCurrency();

        String rateDate = dfTransaction.format(transaction.getCtime());

        Rate rateWallet = getRateForDate(walletCurrency, rateDate);

        Rate rateTransaction = getRateForDate(transactionCurrency, rateDate);

        Double rate = rateTransaction.getRate()/rateWallet.getRate();

        Double amount = transaction.getAmount();

        return amount*rate*sign;
    }


    private TransactionDetails getTransactionDetails(Transaction transaction) throws ParseException {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setOrientationSign(transaction.getCategory().getOrientation().getSign());

        UserCurrency walletCurrency = transaction.getWallet().getCurrency();


        UserCurrency transactionCurrency = transaction.getCurrency();

        String rateDate = dfTransaction.format(transaction.getCtime());

        transactionDetails.setRateWallet(getRateForDate(walletCurrency, rateDate));

        transactionDetails.setRateTransaction(getRateForDate(transactionCurrency, rateDate));

        transactionDetails.setAmountWallet(transaction.getAmount()*transactionDetails.getOrientationSign()*transactionDetails.getRateTransaction().getRate()/transactionDetails.getRateWallet().getRate());

        if (transaction.getWalletOther() != null){
            UserCurrency walletOtherCurrency = transaction.getWalletOther().getCurrency();
            transactionDetails.setRateWalletOther(getRateForDate(walletOtherCurrency, rateDate));
            transactionDetails.setAmountWalletOther(transaction.getAmount() * transactionDetails.getOrientationSign() * transactionDetails.getRateTransaction().getRate() / transactionDetails.getRateWalletOther().getRate());
        }

        Currency usd = currencyRepository.findByCode("840");
        UserCurrency userUsd = null;
        if (usd!= null) {
            userUsd = userCurrencyRepository.findByUserAndCurrencyAndActiveIsTrue(transaction.getUser(), usd);
        }

        if (userUsd != null) {
            transactionDetails.setRateUsd(getRateForDate(userUsd, rateDate));
            transactionDetails.setAmountUsd(transaction.getAmount() * transactionDetails.getOrientationSign() * transactionDetails.getRateTransaction().getRate() / transactionDetails.getRateUsd().getRate());
        }

        return transactionDetails;
    }
}
