package json_schema_validation_example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lion extends Animal {

    @JsonProperty("maneColor")
    private String maneColor;

    protected Lion() {
    	super("lion");
    }

    public Lion(final String pManeColor) {
        super("lion");
        this.maneColor = pManeColor;
    }

    public String getManeColor() { return maneColor; }
}
