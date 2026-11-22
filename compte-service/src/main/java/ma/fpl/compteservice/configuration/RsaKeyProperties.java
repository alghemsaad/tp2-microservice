package ma.fpl.compteservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(Resource publicKey) {
}
