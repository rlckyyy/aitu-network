package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusUpdate {
    private ConnectionStatus status;
}
