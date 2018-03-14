package az.kerimov.fin.finance.finamance;

import az.kerimov.fin.finance.exception.RequestException;
import az.kerimov.fin.finance.exception.UserNotFoundException;
import az.kerimov.fin.finance.pojo.Error;
import az.kerimov.fin.finance.pojo.Data;
import az.kerimov.fin.finance.pojo.Response;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import az.kerimov.fin.finance.pojo.Request;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

@RestController
public class FinController {
    @Autowired
    private FinService finService;

    private Response response = new Response();
    private Data data = new Data();
    private Error error = new Error();

    /**
     * @return
     */
    @PostMapping("/getAllCurrencies")
    @HystrixCommand
    public Response getAllCurrencies(@RequestBody Request request) {

        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setSysCurrencies(finService.getAllCurrencies());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    /**
     * @return
     */
    @PostMapping("/getUserCurrencies")
    @HystrixCommand
    public Response getUserCurrencies(@RequestBody Request request) {

        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setCurrencies(finService.getAllUserCurrencies(request.getSessionKey()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getCurrencyByCode")
    @HystrixCommand
    public Response getCurrencyByCode(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setCurrency(finService.getUserCurrencyByCode(request.getSessionKey(), request.getCurrencyCode()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }
/*
    @PostMapping("/getCurrencyByShortDecription")
    @HystrixCommand
    public Response getCurrencyByShortDecription(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setCurrency(finService.getCurrencyByShortDescription(request.getCurrencyShortDescription()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }
*/
    @PutMapping("/addCurrency")
    @HystrixCommand
    public Response addCurrency(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setNewId(
                    finService.addCurrency(
                            request.getSessionKey(),
                            request.getCurrencyCode()
                    )
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getRateForDate")
    @HystrixCommand
    public Response getRateForDate(@RequestBody Request request) throws ParseException {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setRate(finService.getRateForDate(request.getSessionKey(), request.getCurrencyCode(), request.getRateDate()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getActualRates")
    @HystrixCommand
    public Response getActualRates(@RequestBody Request request) throws ParseException {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setRates(finService.getLatestRates());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PutMapping("/addRate")
    @HystrixCommand
    public Response addRate(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            finService.addRate(request.getSessionKey(), request.getCurrencyCode(), request.getRateDate(), request.getRate());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/login")
    @HystrixCommand
    public Response login(@RequestBody Request request) throws RequestException, UserNotFoundException, NoSuchAlgorithmException {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            Session session = finService.getSessionByLoginPassword(request.getLogin(), request.getPassword());
            data.setSessionKey(session.getSessionKey());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
           response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/logoff")
    @HystrixCommand
    public Response logoff(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            finService.logoff(request.getSessionKey());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getUserBySession")
    @HystrixCommand
    public Response getUserBySession(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            User user = finService.getUserBySessionKey(request.getSessionKey());
            data.setUser(user);
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getWalletById")
    @HystrixCommand
    public Response getWalletById(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setWallet(finService.getWalletById(request.getSessionKey(), request.getWalletId()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getUserWallets")
    @HystrixCommand
    public Response getUserWallets(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setWallets(finService.getUserWallets(request.getSessionKey()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PutMapping("/addWallet")
    @HystrixCommand
    public Response addWallet(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setNewId(
                    finService.addWallet(request.getSessionKey(), request.getCurrencyCode(), request.getCustomName())
            );
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/changeWalletBalance")
    @HystrixCommand
    public Response changeWalletBalance(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setWallet(finService.changeWalletBalance(finService.getWalletById(request.getSessionKey(), request.getWalletId()), request.getNewBalance()));
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }


    @PostMapping("/getCategories")
    @HystrixCommand
    public Response getCategories(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setCategories(finService.getCategories(request.getSessionKey()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getSubCategories")
    @HystrixCommand
    public Response getSubCategories(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setSubCategories(finService.getSubCategories(request.getSessionKey(), request.getCategoryId()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getCategoryById")
    @HystrixCommand
    public Response getCategoryById(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setCategory(finService.getCategoryById(request.getCategoryId()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getSubCategoryById")
    @HystrixCommand
    public Response getSubCategoryById(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setSubCategory(finService.getSubCategoryById(request.getSubCategoryId()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }


    @PostMapping("/getOrientations")
    @HystrixCommand
    public Response getOrientations(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            data.setOrientations(finService.getOrientations());
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PutMapping("/addCategory")
    @HystrixCommand
    public Response addCategory(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setNewId(
                    finService.addCategory(
                            request.getSessionKey(),
                            request.getOrientationId(),
                            request.getCustomName(),
                            request.isDebt()
                    )
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PutMapping("/addSubCategory")
    @HystrixCommand
    public Response addSubCategory(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setNewId(
                    finService.addSubCategory(
                            request.getSessionKey(),
                            request.getCategoryId(),
                            request.getCustomName()
                    )
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }



    @PutMapping("/addTransaction")
    @HystrixCommand
    public Response addTransaction(@RequestBody Request request) throws ParseException {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setNewId(
                    finService.addTransaction(
                            request.getSessionKey(),
                            request.getCategoryId(),
                            request.getSubCategoryId(),
                            request.getCurrencyCode(),
                            request.getAmount(),
                            request.getNotes(),
                            request.getDate(),
                            request.getWalletId(),
                            request.getWalletIdOther()
                    )
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;

    }
}
