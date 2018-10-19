package az.kerimov.fin.finance.finamance;

import az.kerimov.fin.finance.exception.RequestException;
import az.kerimov.fin.finance.pojo.Data;
import az.kerimov.fin.finance.pojo.Error;
import az.kerimov.fin.finance.pojo.Request;
import az.kerimov.fin.finance.pojo.Response;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FinController {
    @Autowired
    private FinService finService;

    private Response response = new Response();
    private Data data = new Data();
    private Error error = new Error();

    /**
     * @return Response
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
     * @return Response
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

    @DeleteMapping("/deleteCurrency")
    @HystrixCommand
    public Response deleteCurrency(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            finService.deleteCurrency(
                    request.getSessionKey(),
                    request.getCurrencyCode()

            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/setDefaultCurrency")
    @HystrixCommand
    public Response setDefaultCurrency(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            finService.setDefaultCurrency(
                    request.getSessionKey(),
                    request.getCurrencyCode()

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
    public Response getRateForDate(@RequestBody Request request)  {
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
    public Response getActualRates(@RequestBody Request request) {
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
    public Response login(@RequestBody Request request) {
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

    @PostMapping("/changePassword")
    @HystrixCommand
    public Response changePassword(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            finService.changePassword(request.getSessionKey(), request.getPassword());
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

    @PostMapping("/getUserInactiveWallets")
    @HystrixCommand
    public Response getUserInactiveWallets(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setWallets(finService.getUserInActiveWallets(request.getSessionKey()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/activateWallet")
    @HystrixCommand
    public Response activateWallet(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setNewId(finService.activateWallet(request.getSessionKey(), request.getWalletId()));
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @DeleteMapping("/deleteWallet")
    @HystrixCommand
    public Response deleteWallet(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            finService.deleteWallet(request.getSessionKey(), request.getWalletId());
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

    @PostMapping("/setDefaultWallet")
    @HystrixCommand
    public Response setDefaultWallet(@RequestBody Request request){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            if (request.getSessionKey() == null) throw new RequestException();
            finService.setDefaultWallet(
                    request.getSessionKey(),
                    request.getWalletId()

            );
            response.setData(data);
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
            data.setCategory(finService.getCategoryById(request.getCategoryId(), request.getSessionKey()));
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
            data.setSubCategory(finService.getSubCategoryById(request.getSubCategoryId(), request.getSessionKey()));
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

    @DeleteMapping("/deleteCategory")
    @HystrixCommand
    public Response deleteCategory(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            finService.deleteCategory(request.getSessionKey(), request.getCategoryId());
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

    @DeleteMapping("/deleteSubCategory")
    @HystrixCommand
    public Response deleteSubCategory(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            finService.deleteSubCategory(request.getSessionKey(), request.getSubCategoryId());
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getLastTransactions")
    @HystrixCommand
    public Response getLastTransactions(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setTransactions(
                    finService.getLastTransactions(request.getSessionKey())
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getReportList")
    @HystrixCommand
    public Response getReportList(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setReports(
                    finService.getReportList(request.getSessionKey())
            );
            response.setData(data);
        } catch (Exception ex) {
            error = new Error(ex, request.getLang());
            response.setError(error);
        }
        finService.writeResponseLog(log, method, request.getSessionKey(), response);
        return response;
    }

    @PostMapping("/getTransactionsBetweenDates")
    @HystrixCommand
    public Response getTransactionsBetweenDates(@RequestBody Request request) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        Log log = finService.writeRequestLog(method, request.getSessionKey(), request);
        data = new Data();
        response = new Response();
        try {
            data.setTransactions(
                    finService.getTransactionsBetweenDates(request.getSessionKey(), request.getStartDate(), request.getEndDate())
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
    public Response addTransaction(@RequestBody Request request) {
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
