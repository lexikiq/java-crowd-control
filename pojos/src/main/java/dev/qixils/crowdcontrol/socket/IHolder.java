package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A holder of {@link Field}s.
 *
 * @since 4.0.0
 */
public interface IHolder {

	/**
	 * Attempts to get a field from this object.
	 * May return empty if the field is {@link Field#supports(ObjectType) unsupported}
	 * or undefined.
	 *
	 * @param field the field to get
	 * @param <T>   the type of the field
	 * @return the deserialized field
	 * @since 4.0.0
	 */
	<T> @NotNull Optional<T> get(@NotNull Field<T> field);

	/**
	 * Attempts to get a field from this object.
	 * Returns the default if the field is {@link Field#supports(ObjectType) unsupported}
	 * or undefined.
	 *
	 * @param field the field to get
	 * @param <T>   the type of the field
	 * @return the deserialized field
	 * @since 4.0.0
	 */
	@Contract(value = "_, !null -> !null", pure = true)
	default <T> @Nullable T getOrDef(@NotNull Field<T> field, @Nullable T def) {
		return get(field).orElse(def);
	}

	/**
	 * Attempts to get a field from this object.
	 * Returns the default if the field is {@link Field#supports(ObjectType) unsupported}
	 * or undefined.
	 *
	 * @param field the field to get
	 * @param <T>   the type of the field
	 * @return the deserialized field
	 * @since 4.0.0
	 */
	@UnknownNullability("May be null if the supplier produces a null value")
	default <T> T getOrSupply(@NotNull Field<T> field, @NotNull Supplier<@Nullable T> def) {
		return get(field).orElseGet(def);
	}

	/**
	 * Attempts to get a field from this object.
	 * Returns null if the field is {@link Field#supports(ObjectType) unsupported}
	 * or undefined.
	 *
	 * @param field the field to get
	 * @param <T>   the type of the field
	 * @return the deserialized field
	 * @since 4.0.0
	 */
	default <T> @Nullable T getOrNull(@NotNull Field<T> field) {
		return getOrDef(field, null);
	}
}
