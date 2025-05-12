package aitu.network.aitunetwork.model.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Data
@FieldNameConstants
public class UserStatusDetails {
    private Boolean connected;
    private Boolean showConnectionDetails;
    private Instant leftOn;
    private Instant connectedOn;
}
