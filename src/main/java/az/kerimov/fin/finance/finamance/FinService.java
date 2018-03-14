package az.kerimov.fin.finance.finamance;

import az.kerimov.fin.finance.exception.AddCurrencyException;
import az.kerimov.fin.finance.exception.UserExistsException;
import az.kerimov.fin.finance.exception.UserNotFoundException;
import az.kerimov.fin.finance.pojo.Request;
import az.kerimov.fin.finance.pojo.Response;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

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
    private OrientationRepository orientationRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserCurrencyRepository userCurrencyRepository;

    private DateFormat df = new SimpleDateFormat("yyyyMMdd");

    private DateFormat dfTransactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormat dfTransaction = new SimpleDateFormat("yyyyMMdd");

    public String getLatestRateDate() throws ParseException {
        List<Rate> rates = rateRepository.findAll();
        String result = "";
        Date currentDate = new Date(0);
        Date rateDate;
        for (int i = 0; i < rates.size(); i++) {
            rateDate = df.parse(rates.get(i).getCtime());
            if (currentDate.getTime() < rateDate.getTime()) {
                currentDate = rateDate;
            }
        }
        return df.format(currentDate);
    }

    public String getCurrencyLatestRateDate(UserCurrency currency, String ctime) throws ParseException {
        List<Rate> rates = rateRepository.findByCurrencyAndCtimeIsLessThanEqual(currency, ctime);
        String result = "";
        Date currentDate = new Date(0);
        Date rateDate;
        for (int i = 0; i < rates.size(); i++) {
            rateDate = df.parse(rates.get(i).getCtime());
            if (currentDate.getTime() < rateDate.getTime()) {
                currentDate = rateDate;
            }
        }
        return df.format(currentDate);
    }

    public Rate getRateForDate(UserCurrency currency, String date) throws ParseException {
        return rateRepository.findByCurrencyAndCtime(currency,
                getCurrencyLatestRateDate(currency, date));
    }

    public Rate getRateForDate(UserCurrency currency, Date date) throws ParseException {
        return getRateForDate(currency, df.format(date));
    }

    public Rate getRateForDate(String sessionKey, String currencyCode, String date) throws ParseException, UserNotFoundException {
        return getRateForDate(userCurrencyRepository.findByUserAndCurrency(
                getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode)
        ), date);
    }

    public List<Rate> getLatestRates() throws ParseException {
        List<UserCurrency> currencies = userCurrencyRepository.findAll();
        List<Rate> rates = new ArrayList<>();
        for (int i = 0; i < currencies.size(); i++) {
            if (currencies.get(i).getId() != 0)
                rates.add(getRateForDate(currencies.get(i), new Date()));
        }
        return rates;
    }

    public void addRate(String sessionKey, UserCurrency currency, String date, Double rate) throws UserNotFoundException {
        Rate result = new Rate();
        result.setCurrency(currency);
        result.setCtime(date);
        result.setRate(rate);
        rateRepository.save(result);
    }

    public void addRate(String sessionKey, String currencyCode, String date, Double rate) throws UserNotFoundException {
        addRate(sessionKey, userCurrencyRepository.findByUserAndCurrency(getUserBySessionKey(sessionKey), currencyRepository.findByCode(currencyCode)), date, rate);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAllByIdGreaterThan(0);
    }

    public List<UserCurrency> getAllUserCurrencies(String sessionKey) throws UserNotFoundException {
        return userCurrencyRepository.findAllByUser(getUserBySessionKey(sessionKey));
    }

    public UserCurrency getUserCurrencyByCode(String sessionKey, String code) throws UserNotFoundException {
        return userCurrencyRepository.findByUserAndCurrency(getUserBySessionKey(sessionKey), currencyRepository.findByCode(code));
    }

    public Integer addCurrency(UserCurrency currency) throws AddCurrencyException {
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
        return addCurrency(currency);
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

    public Log writeResponseLog(Log logParent, String method, String sessionKey, Response response) {
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
        return log;
    }


    public String generateSessionKey() throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
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
        Session session = sessionRepository.findBySessionKeyAndActive(sessionKey, true);
        if (session.getUser() == null) throw new UserNotFoundException();
        return session.getUser();
    }

    public void setAllSessionsInActive(User user) {
        List<Session> sessions = sessionRepository.findAllByUserAndActive(user, true);
        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setActive(false);
            sessionRepository.save(sessions.get(i));
        }
    }

    public void logoff(String sessionKey) throws UserNotFoundException {
        setAllSessionsInActive(getUserBySessionKey(sessionKey));
    }

    public void addUser(User user) throws UserExistsException {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException | TransactionSystemException e) {
            throw new UserExistsException();
        }
    }

    public List<Wallet> getUserWallets(User user) {
        return walletRepository.findByUser(user);
    }

    public List<Wallet> getUserWallets(String sessionKey) throws UserNotFoundException {
        User user = getUserBySessionKey(sessionKey);
        return getUserWallets(user);
    }

    public Integer addWallet(Wallet wallet) {
        walletRepository.save(wallet);
        return wallet.getId();
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

    public Wallet changeWalletBalance(Wallet wallet, Double balance) {
        wallet.setBalanceAmount(balance);
        walletRepository.save(wallet);
        return wallet;
    }

    public Wallet getWalletById(String sessionKey, Integer id) throws UserNotFoundException {
        return walletRepository.findByIdAndUser(id, getUserBySessionKey(sessionKey));
    }

    private Wallet getWalletById(Integer id) throws UserNotFoundException {
        return walletRepository.findById(id);
    }

    public List<Category> getCategories(User user) {
        List<Category> categories = categoryRepository.findByUser(user);
        return categories;
    }

    public List<Category> getCategories(String sessionKey) throws UserNotFoundException {
        return getCategories(getUserBySessionKey(sessionKey));
    }

    public List<SubCategory> getSubCategories(User user, Category category) {
        return subCategoryRepository.findByUserAndCategory(user, category);
    }

    public List<SubCategory> getSubCategories(String sessionKey, Integer categoryId) throws ParseException, UserNotFoundException {
        return getSubCategories(getUserBySessionKey(sessionKey), getCategoryById(categoryId));
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public SubCategory getSubCategoryById(Integer id) {
        return subCategoryRepository.findById(id);
    }

    public List<Orientation> getOrientations() {
        return orientationRepository.findAll();
    }

    public Orientation getOrientationById(Integer id) {
        return orientationRepository.findById(id);
    }

    public Integer addCategory(Category category) {
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

    public Integer addSubCategory(SubCategory subCategory) {
        subCategoryRepository.save(subCategory);
        return subCategory.getId();
    }

    public Integer addSubCategory(String sessionKey, Integer categoryId, String name) throws UserNotFoundException {
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(getCategoryById(categoryId));
        subCategory.setName(name);
        subCategory.setUser(getUserBySessionKey(sessionKey));
        return addSubCategory(subCategory);
    }


    public Integer addTransaction(Transaction transaction) throws ParseException {
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
        transaction.setCategory(getCategoryById(categoryId));
        transaction.setSubCategory(getSubCategoryById(subCategoryId));
        transaction.setCurrency(getUserCurrencyByCode(sessionKey, currencyCode));
        transaction.setAmount(amount);
        transaction.setNotes(notes);
        transaction.setCdate(date);
        transaction.setCtime(dfTransactionDate.parse(date));
        transaction.setWallet(getWalletById(walletId));
        if (walletIdOther != null) {
            transaction.setWalletOther(getWalletById(walletIdOther));
        }
        return addTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Integer id){
        return transactionRepository.findById(id);
    }

    public Double calculateTransactionAmount(Transaction transaction) throws ParseException {
        return calculateTransactionAmount(transaction, true);
    }

    public Double calculateTransactionAmount(Transaction transaction, boolean debtWallet) throws ParseException {
        Integer sign = transaction.getCategory().getOrientation().getSign();
        UserCurrency walletCurrency = null;
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
}
