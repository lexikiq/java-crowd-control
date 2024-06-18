package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.NotNull;

public final class ObjectType<T extends JsonHolder<T>> {
	private final String objectType;
	private final @NotNull JsonHolderFactory<T> factory;

	private ObjectType(String objectType, @NotNull JsonHolderFactory<T> factory) {
		this.objectType = objectType;
		this.factory = factory;
	}

	static <T extends JsonHolder<T>> @NotNull ObjectType<T> create(String objectType, @NotNull JsonHolderFactory<T> factory) {
		return new ObjectType<>(objectType, factory);
	}

	public String id() {
		return objectType;
	}

	public @NotNull JsonHolderFactory<T> factory() {
		return factory;
	}
}
