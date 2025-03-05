package aitu.network.aitunetwork.controller.priv;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/keys")
public class ChatPrivateController {
    private final Map<String, String> publicKeys = new ConcurrentHashMap<>();

    @PostMapping()
    public ResponseEntity<Map<String, String>> savePublicKey(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody Map<String, String> request
    ) {
        String publicKey = request.get("publicKey");
        publicKeys.put(userDetails.getUser().getEmail(), publicKey);
        return ResponseEntity.ok(
                Map.of(
                        "userId",
                        userDetails.getUser().getEmail()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getPublicKey(@PathVariable String id) {
        if (!publicKeys.containsKey(id)) {
            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.ok(Map.of("publicKey", publicKeys.get(id)));
    }
}
