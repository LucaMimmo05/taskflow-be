package org.taskflow;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class NoJwtTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.smallrye-jwt.enabled", "false"
        );
    }
}
