package json_schema_validation_example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tiger extends Animal {

    @JsonProperty("stripeCount")
    private int stripeCount;

    protected Tiger() {
    	super("tiger");
    }

    public Tiger(final int pStripeCount) {
        super("tiger");
        this.stripeCount = pStripeCount;
    }

    public int getStripeCount() { return stripeCount; }
}