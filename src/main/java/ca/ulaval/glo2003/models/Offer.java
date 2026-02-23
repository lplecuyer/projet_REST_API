package ca.ulaval.glo2003.models;

public class Offer {
    private float amount;
    private String message;

    public Offer(float amount, String message){
        this.amount =  amount;
        this.message = message;
    }

    public float getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }
}
