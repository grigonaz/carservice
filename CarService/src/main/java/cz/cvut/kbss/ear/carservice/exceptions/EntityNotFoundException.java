package cz.cvut.kbss.ear.carservice.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class clazz, String parameter, Object value) {
        super (String.format("Entity of class [%s] with parameter [%s] and value [%s] was not found", clazz.getSimpleName(), parameter, value.toString()));

    }
}
