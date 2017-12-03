package com.newsolution.almhrab.Tempreture;

public class TemperatureUnitUtil {
    private double _Temperature;
    private final int number;

    public TemperatureUnitUtil() {
        this.number = 1;
        this._Temperature = -1000.0d;
    }

    public TemperatureUnitUtil(double celsius) {
        this.number = 1;
        this._Temperature = celsius;
    }

    public double GetCelsius() {
        return Double.parseDouble(StringUtil.ToString(this._Temperature, 1));
    }

    public String GetStringCelsius() {
        return StringUtil.ToString(this._Temperature, 1);// + "℃";
    }

    public double GetFahrenheit() {
        return Double.parseDouble(StringUtil.ToString((this._Temperature * 1.8d) + 32.0d, 1));
    }

    public String GetStringFahrenheit() {
        return StringUtil.ToString((this._Temperature * 1.8d) + 32.0d, 1) + "℉";
    }

    public void SetFahrenheit(double res) {
        this._Temperature = (res - 32.0d) / 1.8d;
    }

    public double GetKelvin() {
        return Double.parseDouble(StringUtil.ToString(this._Temperature + 273.15d, 1));
    }

    public String GetStringKelvin() {
        return StringUtil.ToString(this._Temperature + 273.15d, 1) + "K";
    }

    public void SetKelvin(double res) {
        this._Temperature = res - 273.15d;
    }

    public double GetRankine() {
        return Double.parseDouble(StringUtil.ToString(((this._Temperature * 1.8d) + 32.0d) + 459.67d, 1));
    }

    public String GetStringRankine() {
        return StringUtil.ToString(((this._Temperature * 1.8d) + 32.0d) + 459.67d, 1) + "°R";
    }

    public void SetRankine(double res) {
        this._Temperature = ((res - 459.67d) - 32.0d) / 1.8d;
    }

    public double GetReaumur() {
        return Double.parseDouble(StringUtil.ToString(this._Temperature * 0.8d, 1));
    }

    public String GetStringReaumur() {
        return StringUtil.ToString(this._Temperature * 0.8d, 1) + "°Re";
    }

    public void SetReaumur(double res) {
        this._Temperature = res / 0.8d;
    }

    public double GetTemperature(int unit) {
        switch (unit) {
            case 1:
                return GetFahrenheit();
            case 2:
                return GetKelvin();
            case 3:
                return GetRankine();
            case 4:
                return GetReaumur();
            default:
                return GetCelsius();
        }
    }

    public double GetTemperature(int unit, double res) {
        switch (unit) {
            case 1:
                SetFahrenheit(res);
                break;
            case 2:
                SetKelvin(res);
                break;
            case 3:
                SetRankine(res);
                break;
            case 4:
                SetReaumur(res);
                break;
            default:
                this._Temperature = res;
                break;
        }
        return GetCelsius();
    }

    public String GetStringTemperature(int unit) {
        switch (unit) {
            case 1:
                return GetStringFahrenheit();
            case 2:
                return GetStringKelvin();
            case 3:
                return GetStringRankine();
            case 4:
                return GetStringReaumur();
            default:
                return GetStringCelsius();
        }
    }
}
