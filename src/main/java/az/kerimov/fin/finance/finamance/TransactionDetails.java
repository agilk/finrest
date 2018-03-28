package az.kerimov.fin.finance.finamance;

public class TransactionDetails {
    private int orientationSign;
    private Rate rateTransaction;
    private Rate rateWallet;
    private Rate rateWalletOther;
    private Double amountWallet;
    private Double amountWalletOther;
    private Rate rateUsd;
    private Double amountUsd;

    public void setOrientationSign(int orientationSign) {
        this.orientationSign = orientationSign;
    }

    public void setRateTransaction(Rate rateTransaction) {
        this.rateTransaction = rateTransaction;
    }

    public void setRateWallet(Rate rateWallet) {
        this.rateWallet = rateWallet;
    }

    public void setRateWalletOther(Rate rateWalletOther) {
        this.rateWalletOther = rateWalletOther;
    }

    public void setAmountWallet(Double amountWallet) {
        this.amountWallet = amountWallet;
    }

    public void setAmountWalletOther(Double amountWalletOther) {
        this.amountWalletOther = amountWalletOther;
    }

    public int getOrientationSign() {
        return orientationSign;
    }

    public Rate getRateTransaction() {
        return rateTransaction;
    }

    public Rate getRateWallet() {
        return rateWallet;
    }

    public Rate getRateWalletOther() {
        return rateWalletOther;
    }

    public Double getAmountWallet() {
        return amountWallet;
    }

    public Double getAmountWalletOther() {
        return amountWalletOther;
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
