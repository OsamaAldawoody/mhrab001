package com.newsolution.almhrab.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 9/17/2017.
 */
// جميع اعدادات الموقع
public class OptionSiteClass {
    /// <summary>
    /// حالة التنبيه الصوتي للأذان
    /// </summary>

    public boolean isStatusAthanVoiceF() {
        return StatusAthanVoiceF;
    }

    public void setStatusAthanVoiceF(boolean statusAthanVoice) {
        StatusAthanVoiceF = statusAthanVoice;
    }

    public boolean isStatusEkamaVoiceF() {
        return StatusEkamaVoiceF;
    }

    public void setStatusEkamaVoiceF(boolean statusEkamaVoice) {
        StatusEkamaVoiceF = statusEkamaVoice;
    }

    /////
    public boolean isStatusAthanVoiceD() {
        return StatusAthanVoiceD;
    }

    public void setStatusAthanVoiceD(boolean statusAthanVoice) {
        StatusAthanVoiceD = statusAthanVoice;
    }

    public boolean isStatusEkamaVoiceD() {
        return StatusEkamaVoiceD;
    }

    public void setStatusEkamaVoiceD(boolean statusEkamaVoice) {
        StatusEkamaVoiceD = statusEkamaVoice;
    }

    /////
    public boolean isStatusAthanVoiceA() {
        return StatusAthanVoiceA;
    }

    public void setStatusAthanVoiceA(boolean statusAthanVoice) {
        StatusAthanVoiceA = statusAthanVoice;
    }

    public boolean isStatusEkamaVoiceA() {
        return StatusEkamaVoiceA;
    }

    public void setStatusEkamaVoiceA(boolean statusEkamaVoice) {
        StatusEkamaVoiceA = statusEkamaVoice;
    }

    /////
    public boolean isStatusAthanVoiceM() {
        return StatusAthanVoiceM;
    }

    public void setStatusAthanVoiceM(boolean statusAthanVoice) {
        StatusAthanVoiceM = statusAthanVoice;
    }

    public boolean isStatusEkamaVoiceM() {
        return StatusEkamaVoiceM;
    }

    public void setStatusEkamaVoiceM(boolean statusEkamaVoice) {
        StatusEkamaVoiceM = statusEkamaVoice;
    }

    /////
    public boolean isStatusAthanVoiceI() {
        return StatusAthanVoiceI;
    }

    public void setStatusAthanVoiceI(boolean statusAthanVoice) {
        StatusAthanVoiceI = statusAthanVoice;
    }

    public boolean isStatusEkamaVoiceI() {
        return StatusEkamaVoiceI;
    }

    public void setStatusEkamaVoiceI(boolean statusEkamaVoice) {
        StatusEkamaVoiceI = statusEkamaVoice;
    }

    public int getFajrEkama() {
        return FajrEkama;
    }

    public void setFajrEkama(int fajrEkama) {
        FajrEkama = fajrEkama;
    }

    public int getFajrAzkar() {
        return FajrAzkar;
    }

    public void setFajrAzkar(int fajrAzkar) {
        FajrAzkar = fajrAzkar;
    }

    public int getFajrAzkarTimer() {
        return FajrAzkarTimer;
    }

    public void setFajrAzkarTimer(int fajrAzkarTimer) {
        FajrAzkarTimer = fajrAzkarTimer;
    }

    public int getDhuhrEkama() {
        return DhuhrEkama;
    }

    public void setDhuhrEkama(int dhuhrEkama) {
        DhuhrEkama = dhuhrEkama;
    }

    public int getDhuhrAzkar() {
        return DhuhrAzkar;
    }

    public void setDhuhrAzkar(int dhuhrAzkar) {
        DhuhrAzkar = dhuhrAzkar;
    }

    public int getDhuhrAzkarTimer() {
        return DhuhrAzkarTimer;
    }

    public void setDhuhrAzkarTimer(int dhuhrAzkarTimer) {
        DhuhrAzkarTimer = dhuhrAzkarTimer;
    }

    public int getAsrEkama() {
        return AsrEkama;
    }

    public void setAsrEkama(int asrEkama) {
        AsrEkama = asrEkama;
    }

    public int getAsrAzkar() {
        return AsrAzkar;
    }

    public void setAsrAzkar(int asrAzkar) {
        AsrAzkar = asrAzkar;
    }

    public int getAsrAzkarTimer() {
        return AsrAzkarTimer;
    }

    public void setAsrAzkarTimer(int asrAzkarTimer) {
        AsrAzkarTimer = asrAzkarTimer;
    }

    public int getMagribEkama() {
        return MagribEkama;
    }

    public void setMagribEkama(int magribEkama) {
        MagribEkama = magribEkama;
    }

    public int getMagribAzkar() {
        return MagribAzkar;
    }

    public void setMagribAzkar(int magribAzkar) {
        MagribAzkar = magribAzkar;
    }

    public int getMagribAzkarTimer() {
        return MagribAzkarTimer;
    }

    public void setMagribAzkarTimer(int magribAzkarTimer) {
        MagribAzkarTimer = magribAzkarTimer;
    }

    public int getIshaEkama() {
        return IshaEkama;
    }

    public void setIshaEkama(int ishaEkama) {
        IshaEkama = ishaEkama;
    }

    public int getIshaAzkar() {
        return IshaAzkar;
    }

    public void setIshaAzkar(int ishaAzkar) {
        IshaAzkar = ishaAzkar;
    }

    public int getIshaAzkarTimer() {
        return IshaAzkarTimer;
    }

    public void setIshaAzkarTimer(int ishaAzkarTimer) {
        IshaAzkarTimer = ishaAzkarTimer;
    }

    public boolean isPhoneStatusAlerts() {
        return PhoneStatusAlerts;
    }

    public void setPhoneStatusAlerts(boolean phoneStatusAlerts) {
        PhoneStatusAlerts = phoneStatusAlerts;
    }

    public int getPhoneShowAlertsBeforEkama() {
        return PhoneShowAlertsBeforEkama;
    }

    public void setPhoneShowAlertsBeforEkama(int phoneShowAlertsBeforEkama) {
        PhoneShowAlertsBeforEkama = phoneShowAlertsBeforEkama;
    }

    public String getPhoneAlertsArabic() {
        return PhoneAlertsArabic;
    }

    public void setPhoneAlertsArabic(String phoneAlertsArabic) {
        PhoneAlertsArabic = phoneAlertsArabic;
    }

    public String getPhoneAlertsEnglish() {
        return PhoneAlertsEnglish;
    }

    public void setPhoneAlertsEnglish(String phoneAlertsEnglish) {
        PhoneAlertsEnglish = phoneAlertsEnglish;
    }

    public String getPhoneAlertsUrdu() {
        return PhoneAlertsUrdu;
    }

    public void setPhoneAlertsUrdu(String phoneAlertsUrdu) {
        PhoneAlertsUrdu = phoneAlertsUrdu;
    }

    public boolean isPhoneStatusVoice() {
        return PhoneStatusVoice;
    }

    public void setPhoneStatusVoice(boolean phoneStatusVoice) {
        PhoneStatusVoice = phoneStatusVoice;
    }

    public int getDateHijri() {
        return DateHijri;
    }

    public void setDateHijri(int dateHijri) {
        DateHijri = dateHijri;
    }

    private boolean FajrEkamaIsTime;
    private boolean DhuhrEkamaIsTime;
    private boolean AsrEkamaIsTime;
    private boolean MagribEkamaIsTime;
    private boolean IshaEkamaIsTime;

    private String FajrEkamaTime;
    private String DhuhrEkamaTime;
    private String AsrEkamaTime;
    private String MagribEkamaTime;
    private String IshaEkamaTime;

    public boolean isFajrEkamaIsTime() {
        return FajrEkamaIsTime;
    }

    public void setFajrEkamaIsTime(boolean fajrEkamaIsTime) {
        FajrEkamaIsTime = fajrEkamaIsTime;
    }

    public boolean isDhuhrEkamaIsTime() {
        return DhuhrEkamaIsTime;
    }

    public void setDhuhrEkamaIsTime(boolean dhuhrEkamaIsTime) {
        DhuhrEkamaIsTime = dhuhrEkamaIsTime;
    }

    public boolean isAsrEkamaIsTime() {
        return AsrEkamaIsTime;
    }

    public void setAsrEkamaIsTime(boolean asrEkamaIsTime) {
        AsrEkamaIsTime = asrEkamaIsTime;
    }

    public boolean isMagribEkamaIsTime() {
        return MagribEkamaIsTime;
    }

    public void setMagribEkamaIsTime(boolean magribEkamaIsTime) {
        MagribEkamaIsTime = magribEkamaIsTime;
    }

    public boolean ishaEkamaIsTime() {
        return IshaEkamaIsTime;
    }

    public void setIshaEkamaIsTime(boolean ishaEkamaIsTime) {
        IshaEkamaIsTime = ishaEkamaIsTime;
    }

    public String getFajrEkamaTime() {
        return FajrEkamaTime;
    }

    public void setFajrEkamaTime(String fajrEkamaTime) {
        FajrEkamaTime = fajrEkamaTime;
    }

    public String getDhuhrEkamaTime() {
        return DhuhrEkamaTime;
    }

    public void setDhuhrEkamaTime(String dhuhrEkamaTime) {
        DhuhrEkamaTime = dhuhrEkamaTime;
    }

    public String getAsrEkamaTime() {
        return AsrEkamaTime;
    }

    public void setAsrEkamaTime(String asrEkamaTime) {
        AsrEkamaTime = asrEkamaTime;
    }

    public String getMagribEkamaTime() {
        return MagribEkamaTime;
    }

    public void setMagribEkamaTime(String magribEkamaTime) {
        MagribEkamaTime = magribEkamaTime;
    }

    public String getIshaEkamaTime() {
        return IshaEkamaTime;
    }

    public void setIshaEkamaTime(String ishaEkamaTime) {
        IshaEkamaTime = ishaEkamaTime;
    }

    public int getAlShrouqEkama() {
        return AlShrouqEkama;
    }

    public void setAlShrouqEkama(int alShrouqEkama) {
        AlShrouqEkama = alShrouqEkama;
    }

    public String getAlShrouqEkamaTime() {
        return AlShrouqEkamaTime;
    }

    public void setAlShrouqEkamaTime(String alShrouqEkamaTime) {
        AlShrouqEkamaTime = alShrouqEkamaTime;
    }

    public boolean isAlShrouqEkamaIsTime() {
        return AlShrouqEkamaIsTime;
    }

    public void setAlShrouqEkamaIsTime(boolean alShrouqEkamaIsTime) {
        AlShrouqEkamaIsTime = alShrouqEkamaIsTime;
    }


    private int AsrAzkar;

    private int AsrEkama;
    private int AsrAzkarTimer;

    private int MagribEkama;

    private int MagribAzkar;

    private int MagribAzkarTimer;

    private int IshaEkama;

    private int IshaAzkar;

    private int IshaAzkarTimer;


    private boolean StatusAthanVoiceF;
    private boolean StatusEkamaVoiceF;
    private boolean StatusAthanVoiceD;
    private boolean StatusEkamaVoiceD;
    private boolean StatusAthanVoiceA;
    private boolean StatusEkamaVoiceA;
    private boolean StatusAthanVoiceM;
    private boolean StatusEkamaVoiceM;
    private boolean StatusAthanVoiceI;
    private boolean StatusEkamaVoiceI;
    private int FajrEkama;
    private int FajrAzkar;
    private int FajrAzkarTimer;


    private int DhuhrEkama;
    private int DhuhrAzkar;
    private int DhuhrAzkarTimer;


    private int AlShrouqEkama;
    private String AlShrouqEkamaTime;
    private boolean AlShrouqEkamaIsTime;

    /// <summary>
    /// حالة التنبيهات المرئية لإغلاق الهاتف
    /// </summary>
    private boolean PhoneStatusAlerts;
    /// <summary>
    /// اظهار التنبيهات قبل إقامة الصلاة بـ ثانية
    /// </summary>
    private int PhoneShowAlertsBeforEkama;
    private String PhoneAlertsArabic;
    private String PhoneAlertsEnglish;
    private String PhoneAlertsUrdu;
    private boolean PhoneStatusVoice;
    private int DateHijri;
    private int CloseScreenAfterIsha;
    private int RunScreenBeforeFajr;

    public int getCloseScreenAfterIsha() {
        return CloseScreenAfterIsha;
    }

    public void setCloseScreenAfterIsha(int closeScreenAfterIsha) {
        CloseScreenAfterIsha = closeScreenAfterIsha;
    }

    public int getRunScreenBeforeFajr() {
        return RunScreenBeforeFajr;
    }

    public void setRunScreenBeforeFajr(int runScreenBeforeFajr) {
        RunScreenBeforeFajr = runScreenBeforeFajr;
    }

    @Override
    public String toString() {
        return "OptionSiteClass: PhoneStatusVoice='" + PhoneStatusVoice
                + "' ,PhoneStatusAlerts='" + PhoneStatusAlerts
                + "' ,PhoneAlertsUrdu='" + PhoneAlertsUrdu
                + "' ,PhoneAlertsEnglish='" + PhoneAlertsEnglish
                + "' ,PhoneAlertsArabic='" + PhoneAlertsArabic
                + "' ,StatusAthanVoiceF='" + StatusAthanVoiceF
                + "' ,StatusEkamaVoiceF='" + StatusEkamaVoiceF
                + "' ,StatusAthanVoiceD='" + StatusAthanVoiceD
                + "' ,StatusEkamaVoiceD='" + StatusEkamaVoiceD
                + "' ,StatusAthanVoiceA='" + StatusAthanVoiceA
                + "' ,StatusEkamaVoiceA='" + StatusEkamaVoiceA
                + "' ,StatusAthanVoiceM='" + StatusAthanVoiceM
                + "' ,StatusEkamaVoiceM='" + StatusEkamaVoiceM
                + "' ,StatusAthanVoiceI='" + StatusAthanVoiceI
                + "' ,StatusEkamaVoiceI='" + StatusEkamaVoiceI
                + "', PhoneShowAlertsBeforEkama=" + PhoneShowAlertsBeforEkama
                + "', IshaAzkarTimer=" + IshaAzkarTimer
                + "', IshaAzkar=" + IshaAzkar
                + "', IshaEkama=" + IshaEkama
                + "', MagribEkama=" + MagribEkama
                + "', MagribAzkar=" + MagribAzkar
                + "', MagribAzkarTimer=" + MagribAzkarTimer
                + "', AsrEkama=" + AsrEkama
                + "', AsrAzkar=" + AsrAzkar
                + "', AsrAzkarTimer=" + AsrAzkarTimer
                + "', DhuhrEkama=" + DhuhrEkama
                + "', DhuhrAzkar=" + DhuhrAzkar
                + "', DhuhrAzkarTimer=" + DhuhrAzkarTimer
                + "', FajrEkama=" + FajrEkama
                + "', FajrAzkar=" + FajrAzkar
                + "', FajrAzkarTimer=" + FajrAzkarTimer
                + "', DateHijri=" + DateHijri;
    }

    public Map<String, Object> toMap() {
        PhoneAlertsArabic = PhoneAlertsArabic.replace("!", "");
        PhoneAlertsEnglish = PhoneAlertsEnglish.replace("!", "");
        HashMap<String, Object> result = new HashMap<>();
        result.put("DateHijri", DateHijri);
        result.put("PhoneStatusVoice", PhoneStatusVoice);
        result.put("PhoneStatusAlerts", PhoneStatusAlerts);
        result.put("PhoneAlertsUrdu", PhoneAlertsUrdu);
        result.put("PhoneAlertsEnglish", PhoneAlertsEnglish);
        result.put("PhoneAlertsArabic", PhoneAlertsArabic);
        result.put("PhoneShowAlertsBeforEkama", PhoneShowAlertsBeforEkama);
        result.put("StatusAthanVoiceF", StatusAthanVoiceF);
        result.put("StatusEkamaVoiceF", StatusEkamaVoiceF);
        result.put("StatusAthanVoiceD", StatusAthanVoiceD);
        result.put("StatusEkamaVoiceD", StatusEkamaVoiceD);
        result.put("StatusAthanVoiceA", StatusAthanVoiceA);
        result.put("StatusEkamaVoiceA", StatusEkamaVoiceA);
        result.put("StatusAthanVoiceM", StatusAthanVoiceM);
        result.put("StatusEkamaVoiceM", StatusEkamaVoiceM);
        result.put("StatusAthanVoiceI", StatusAthanVoiceI);
        result.put("StatusEkamaVoiceI", StatusEkamaVoiceI);
        result.put("IshaAzkarTimer", IshaAzkarTimer);
        result.put("IshaAzkar", IshaAzkar);
        result.put("IshaEkama", IshaEkama);
        result.put("MagribEkama", MagribEkama);
        result.put("MagribAzkar", MagribAzkar);
        result.put("MagribAzkarTimer", MagribAzkarTimer);
        result.put("AsrEkama", AsrEkama);
        result.put("AsrAzkar", AsrAzkar);
        result.put("AsrAzkarTimer", AsrAzkarTimer);
        result.put("DhuhrEkama", DhuhrEkama);
        result.put("DhuhrAzkar", DhuhrAzkar);
        result.put("DhuhrAzkarTimer", DhuhrAzkarTimer);
        result.put("FajrEkama", FajrEkama);
        result.put("FajrAzkar", FajrAzkar);
        result.put("FajrAzkarTimer", FajrAzkarTimer);
        return result;

    }
}