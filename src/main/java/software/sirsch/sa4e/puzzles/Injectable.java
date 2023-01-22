package software.sirsch.sa4e.puzzles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Diese Annotation zeigt an, dass ein Feld für Injection verwendet werden darf.
 *
 * @author sirsch
 * @since 22.01.2023
 *
 * @see Injector
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
}
