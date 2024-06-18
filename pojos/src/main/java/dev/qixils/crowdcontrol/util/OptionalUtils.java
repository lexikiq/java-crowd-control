package dev.qixils.crowdcontrol.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalUtils {
	private OptionalUtils() {
	}

	public static <T> void ifPresentOrElse(Optional<T> optional, Consumer<T> ifPresent, Runnable orElse) {
		if (optional.isPresent())
			ifPresent.accept(optional.get());
		else
			orElse.run();
	}

	@SafeVarargs
	public static <T> Optional<T> or(Optional<T>... optionals) {
		for (Optional<T> optional : optionals) {
			if (optional.isPresent())
				return optional;
		}
		return Optional.empty();
	}

	@SafeVarargs
	public static <T> Optional<T> or(Supplier<Optional<T>>... optionals) {
		for (Supplier<Optional<T>> supplier : optionals) {
			Optional<T> optional = supplier.get();
			if (optional.isPresent())
				return optional;
		}
		return Optional.empty();
	}
}
