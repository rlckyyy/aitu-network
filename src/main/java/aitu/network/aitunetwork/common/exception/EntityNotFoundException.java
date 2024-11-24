package aitu.network.aitunetwork.common.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> clazz, Object id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
    }
}
