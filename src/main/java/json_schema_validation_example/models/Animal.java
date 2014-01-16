package json_schema_validation_example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;  
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_t",
    visible = false)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Lion.class, name = "lion"),
    @JsonSubTypes.Type(value = Tiger.class, name = "tiger")})
public abstract class Animal {

    //@JsonProperty not required as JsonTypeInfo will serialize it
    private String type;

    public Animal(final String pName) {
        this.type = pName;
    }
}
