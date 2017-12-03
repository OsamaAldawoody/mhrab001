package com.newsolution.almhrab.Weather;

public class Weather {
    public Clouds clouds = new Clouds();
    public CurrentCondition currentCondition = new CurrentCondition();
    public String iconData;
    public Location location;
    public Rain rain = new Rain();
    public Snow snow = new Snow();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();

    public class Clouds {
        private int perc;

        public int getPerc() {
            return this.perc;
        }

        public void setPerc(int perc) {
            this.perc = perc;
        }
    }

    public class CurrentCondition {
        private String condition;
        private String descr;
        private float humidity;
        private String icon;
        private float pressure;
        private int weatherId;

        public int getWeatherId() {
            return this.weatherId;
        }

        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
        }

        public String getCondition() {
            return this.condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getDescr() {
            return this.descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getPressure() {
            return this.pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public float getHumidity() {
            return this.humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }
    }

    public class Rain {
        private float ammount;
        private String time;

        public String getTime() {
            return this.time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return this.ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }
    }

    public class Snow {
        private float ammount;
        private String time;

        public String getTime() {
            return this.time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return this.ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }
    }

    public class Temperature {
        private float maxTemp;
        private float minTemp;
        private float temp;

        public float getTemp() {
            return this.temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getMinTemp() {
            return this.minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public float getMaxTemp() {
            return this.maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }
    }

    public class Wind {
        private float deg;
        private float speed;

        public float getSpeed() {
            return this.speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDeg() {
            return this.deg;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }
    }
}
