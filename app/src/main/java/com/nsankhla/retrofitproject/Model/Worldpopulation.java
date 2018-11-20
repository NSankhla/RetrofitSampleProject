package com.nsankhla.retrofitproject.Model;

public class Worldpopulation {

    public int rank;
    public String country;
    public String population;
    public String flag;

    public Worldpopulation(int rank, String country, String population, String flag) {
        this.rank = rank;
        this.country = country;
        this.population = population;
        this.flag = flag;
    }

    public Worldpopulation() {
    }
}
