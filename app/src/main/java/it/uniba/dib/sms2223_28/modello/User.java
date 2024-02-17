package it.uniba.dib.sms2223_28.modello;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    private String username;
    private String email;
    private String password;
    private Integer trophies;
    private Integer maxTrophies;
    private Integer romanWins;
    private Integer persisWins;
    private Integer egyptWins;
    private Integer spartanWins;
    private Integer cardsBackRoman;

    private Integer cardsBackPersis;

    private Integer cardsBackSpartans;

    private Integer cardsBackEgypt;
    private  String status = "offline";

    public User(String email, String password, Integer trophies, Integer maxTrophies,String username, Integer romanWins, Integer persisWins, Integer egyptWins, Integer spartanWins, Integer cardsBackRoman,Integer cardsBackPersis,Integer cardsBackSpartans,Integer cardsBackEgypt, String status) {

        this.email = email;
        this.password = password;
        this.trophies = trophies;
        this.maxTrophies = maxTrophies;
        this.username = username;
        this.romanWins = romanWins;
        this.persisWins = persisWins;
        this.egyptWins = egyptWins;
        this.spartanWins = spartanWins;
        this.cardsBackRoman = cardsBackRoman;
        this.cardsBackPersis = cardsBackPersis;
        this.cardsBackSpartans = cardsBackSpartans;
        this.cardsBackEgypt = cardsBackEgypt;
        this.status = status;
    }

    public User(String username, int trophies) {
        this.username=username;
        this.trophies =trophies;
    }


    public String getUsername() {
        return username;
    }

    public Integer getTrophies() {
        return trophies;
    }

    public Integer getMaxTrophies() {
        return maxTrophies;
    }

    public Integer getRomanWins() {
        return romanWins;
    }

    public Integer getPersisWins() {
        return persisWins;
    }

    public Integer getEgyptWins() {
        return egyptWins;
    }

    public Integer getSpartanWins() {
        return spartanWins;
    }

    public Integer getCardsBackRoman(){ return cardsBackRoman;}

    public Integer getCardsBackPersis(){ return cardsBackPersis;}

    public Integer getCardsBackSpartans(){ return cardsBackSpartans;}

    public Integer getCardsBackEgypt(){ return cardsBackEgypt;}

    public void setStatusOn(){
        status = "online";
    }

    public void setStatusOff(){
        status = "offline";
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", actualTrophies=" + trophies +
                ", maxTrophies=" + maxTrophies +
                ", romanWins=" + romanWins +
                ", persisWins=" + persisWins +
                ", egyptWins=" + egyptWins +
                ", spartanWins=" + spartanWins +
                ", cardsBackRoman=" + cardsBackRoman +
                ", cardsBackPersis=" + cardsBackPersis +
                ", cardsBackSpartans=" + cardsBackSpartans +
                ", cardsBackEgypt=" + cardsBackEgypt +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int compareTo(User o) {
        return Integer.compare(o.trophies, this.trophies);
    }

    public void setTrophies(Integer trophies) {
        this.trophies = trophies;
    }

    public void setMaxTrophies(Integer maxTrophies) {
        this.maxTrophies = maxTrophies;
    }

    public void setEgyptBack(int backId) {
        this.cardsBackEgypt=backId;
    }

    public void setRomeBack(int backId) {
        this.cardsBackRoman=backId;
    }

    public void setSpartaBack(int backId) {
        this.cardsBackSpartans=backId;
    }

    public void setPersisBack(int backId) {
        this.cardsBackPersis=backId;
    }

    public void incrementEgyptWins() {

        this.egyptWins++;


    }

    public void incrementRomeWins() {

        this.romanWins++;
    }

    public void incrementPersisWins() {

        this.persisWins++;
    }

    public void incrementSpartansWins() {

        this.spartanWins++;
    }
}
