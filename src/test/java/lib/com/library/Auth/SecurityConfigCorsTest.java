package lib.com.library.Auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.mockito.Mockito.mock;

class SecurityConfigCorsTest {

    private final CorsConfigurationSource corsSource = new SecurityConfig(
            mock(JwtAuthFilter.class))
            .corsConfigurationSource(
                    "http://localhost:[*],http://127.0.0.1:[*],http://192.168.1.7:[*]");

    @Test
    void allowsJsonPreflightFromLocalFlutterWebOrigin() {
        CorsConfiguration configuration = corsSource.getCorsConfiguration(
                new MockHttpServletRequest("OPTIONS", "/auth/login"));

        assertNotNull(configuration);
        assertEquals("http://localhost:52134",
                configuration.checkOrigin("http://localhost:52134"));
        assertNotNull(configuration.checkHttpMethod(HttpMethod.POST));
        assertNotNull(configuration.checkHttpMethod(HttpMethod.OPTIONS));
        assertNotNull(configuration.checkHeaders(List.of("content-type")));
    }

    @Test
    void rejectsOriginsOutsideTheDevelopmentAllowlist() {
        CorsConfiguration configuration = corsSource.getCorsConfiguration(
                new MockHttpServletRequest("OPTIONS", "/auth/login"));

        assertNotNull(configuration);
        assertNull(configuration.checkOrigin("https://untrusted.example"));
    }
}
