package com.mattress;

public class ProductCalculations implements IProductCalculations {

    public static final double VAT_RATE = 0.15;
    public static final double COMMISSION_RATE = 0.05;

    private final double price;

    public ProductCalculations(double price) {
        this.price = price;
    }

    /** VAT = 15% of the product price. */
    @Override
    public double GetVat() {
        return round(price * VAT_RATE);
    }

    /** Commission = 5% of the product price. */
    @Override
    public double GetCommission() {
        return round(price * COMMISSION_RATE);
    }

    /** Total = price + VAT + commission. */
    @Override
    public double GetTotal() {
        return round(price + GetVat() + GetCommission());
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
