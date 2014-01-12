package json_schema_validation_example;

import json_schema_validation_example.resources.*;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.assets.AssetsBundle;

public class JsonSchemaValidationExampleService
    extends Service<JsonSchemaValidationExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new JsonSchemaValidationExampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap<JsonSchemaValidationExampleConfiguration> bootstrap) {
        bootstrap.setName("json_schema_validation_example");
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(JsonSchemaValidationExampleConfiguration configuration,
                    Environment environment) {
        environment.addProvider(new CustomJacksonJsonProvider());
        environment.addResource(new AnimalResource());
    }

}