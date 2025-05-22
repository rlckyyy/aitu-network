package aitu.network.aitunetwork.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("A class that extends " + BaseEntity.class.getSimpleName() + " should implement its own hashCode() method");
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("A class that extends " + BaseEntity.class.getSimpleName() + " should implement its own equals() method");
    }
}
