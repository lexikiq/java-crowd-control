package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.NotNull;

/**
 * A creator for a JsonHolder.
 *
 * @param <T> the type of JsonHolder
 * @since 4.0.0
 */
@FunctionalInterface
public interface JsonHolderFactory<T extends JsonHolder<T>> {

	/**
	 * Creates a JsonHolder from a builder.
	 *
	 * @param builder the builder
	 * @return the created JsonHolder
	 * @since 4.0.0
	 */
	@NotNull T create(@NotNull JsonHolderBuilder<T> builder);
}
