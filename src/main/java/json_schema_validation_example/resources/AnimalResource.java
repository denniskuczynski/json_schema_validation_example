package json_schema_validation_example.resources;

import json_schema_validation_example.annotations.ValidateWithSchema;
import json_schema_validation_example.models.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import java.io.IOException;

@Path("/animals")
@Produces(MediaType.APPLICATION_JSON)
public class AnimalResource {

    @Context
    protected Providers providers;

    public AnimalResource() {
    }

    /* Example of processing the Resource body as a String */
    @POST
    @Path("/suggest_manual")
    @Consumes(MediaType.APPLICATION_JSON)
    public Animal suggestAnimal_Manual(String jsonBody)
        throws IOException, ProcessingException {
        ContextResolver<ObjectMapper> resolver =
            providers.getContextResolver(ObjectMapper.class,
                                         MediaType.APPLICATION_JSON_TYPE);
        ObjectMapper mapper = resolver.getContext(Object.class);

        final JsonNode fstabSchema = JsonLoader.fromResource("/assets/schemas/animal.json");
        final JsonNode pass = JsonLoader.fromString(jsonBody);
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(fstabSchema);
        ProcessingReport report = schema.validate(pass);
        
        if (report.isSuccess()) {
            final Animal response = mapper.readValue(jsonBody, Animal.class);
            return response;
        } else {
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST).
                    entity("{ \"additionalInfo\": false }").build());
        }
    }

    /* Example of processing the Resource body with a Schema Validation Filter
     * and Jackson object mapping */
    @POST
    @Path("/suggest")
    @Consumes()
    @ValidateWithSchema(name="animal.json")
    public Animal suggestAnimal_WithFilter(Animal animal) {
        return animal;
    }
}
