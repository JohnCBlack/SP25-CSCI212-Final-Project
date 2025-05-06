package com.csci212.finalproject;

public class StockData {
    private String symbol;
    private float currentPrice;
    private float previousClose;
    private float change;
    private float high;
    private float low;
    private float open;
    private float percentChange;

    public StockData(String symbol, float currentPrice, float previousClose, float change, float high, float low, float open, float percentChange) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.change = change;
        this.high = high;
        this.low = low;
        this.open = open;
        this.percentChange = percentChange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(float previousClose) {
        this.previousClose = previousClose;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

}
