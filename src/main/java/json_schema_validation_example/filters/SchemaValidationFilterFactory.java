package json_schema_validation_example.filters;

import json_schema_validation_example.annotations.ValidateWithSchema;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class SchemaValidationFilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        ValidateWithSchema ann = am.getAnnotation(ValidateWithSchema.class);
        if (ann == null) {
            return Collections.<ResourceFilter>emptyList();
        } else {
            return Collections.<ResourceFilter>singletonList(new SchemaValidationFilter(ann.name()));
        }
    }

    private static class SchemaValidationFilter implements ResourceFilter, ContainerRequestFilter {

        final String schemaName;

        SchemaValidationFilter(String schemaName) {
            this.schemaName = schemaName;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return this;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }

        /* Attempt to proceess the request body against the JSON Schema document specified in the
         * the annotation */
        @Override
        public ContainerRequest filter(ContainerRequest request) {
            try {
                final String jsonBody = readBodyAndResetRequest(request);
                final JsonNode fstabSchema = JsonLoader.fromResource("/assets/schemas/"+schemaName);
                final JsonNode pass = JsonLoader.fromString(jsonBody);
                final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
                final JsonSchema schema = factory.getJsonSchema(fstabSchema);
                ProcessingReport report = schema.validate(pass);
                
                if (report.isSuccess()) {
                    return request;
                } else {
                    throw new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST).
                            entity("{ \"additionalInfo\": false }").build());
                }
            } catch (IOException ex) {
                throw new WebApplicationException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            } catch (ProcessingException ex) {
                throw new WebApplicationException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            }
        }

        /* Read the body of the request, then set it back on the request so it can be read again
         * downstream */
        private String readBodyAndResetRequest(ContainerRequest request) throws IOException {
            final String jsonBody;
            
            BufferedReader br = new BufferedReader(
                new InputStreamReader(request.getEntityInputStream()));
            
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null) {
                sb.append(line);
                line = br.readLine();
            }

            jsonBody = sb.toString();
            request.setEntityInputStream(new ByteArrayInputStream(jsonBody.getBytes()));
            return jsonBody;
        }
    }
}
