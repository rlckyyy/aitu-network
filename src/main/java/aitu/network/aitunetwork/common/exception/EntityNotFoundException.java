package aitu.network.aitunetwork.common.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> clazz, Object id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> clazz, Object field, Object id) {
        super(String.format("Entity %s with %s %s not found", clazz.getSimpleName(), field, id));
    }

    public EntityNotFoundException(String desc) {
        super(desc);
    }

}
