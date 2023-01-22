package software.sirsch.sa4e.puzzles;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

/**
 * Diese Klasse stellt die Funktionalität zum Injizieren von Objekten Bereit.
 *
 * @author sirsch
 * @since 22.01.2023
 *
 * @param <T> der Typ des Zielobjekts
 * @param <V> der Typ des zu injizierenden Werts
 */
public class Injector<T, V> implements Function<V, Consumer<T>> {

	@Override
	public Consumer<T> apply(@CheckForNull final V value) {
		return instance -> this.inject(instance, value);
	}

	/**
	 * Diese Methode führt die Injektion durch.
	 *
	 * @param targetInstance das Zielobjekt, in das injiziert werden soll
	 * @param value der zu injizierende Wert
	 */
	public void inject(@Nonnull final T targetInstance, @CheckForNull final V value) {
		doWithFields(
				targetInstance.getClass(),
				field -> this.handleField(field, targetInstance, value));
	}

	/**
	 * Diese Methode behandelt ein Feld.
	 *
	 * <p>
	 *     Dabei werden zunächst Felder aussortiert, die nicht für Injektion geeignet sind. Dann
	 *     wird das Feld zugänglich gemacht, falls es nicht sichtbar ist. Zuletzt wird der Wert
	 *     unter Verwendung des Feldes im Zielobjekt gesetzt.
	 * </p>
	 *
	 * @param field das zu behandelnde Feld
	 * @param targetInstance das Zielobjekt, in das injiziert werden soll
	 * @param value der zu injizierende Wert
	 */
	private void handleField(
			@Nonnull final Field field,
			@Nonnull final T targetInstance,
			@CheckForNull final V value) {

		if (!field.isAnnotationPresent(Injectable.class)) {
			return;
		}

		if (value != null && !field.getType().isInstance(value)) {
			return;
		}

		makeAccessible(field);
		setField(field, targetInstance, value);
	}
}
