package json_schema_validation_example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ContextResolver;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CustomJacksonJsonProvider extends JacksonJaxbJsonProvider
     implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper().
        disable(MapperFeature.AUTO_DETECT_CREATORS).
        disable(MapperFeature.AUTO_DETECT_FIELDS).
        disable(MapperFeature.AUTO_DETECT_IS_GETTERS).
        disable(MapperFeature.AUTO_DETECT_FIELDS);

    public CustomJacksonJsonProvider() {
        super();
        setMapper(mapper);
    }

     public ObjectMapper getContext(java.lang.Class<?> type) {
        return mapper;
     }
}
