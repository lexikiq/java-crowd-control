package dev.qixils.crowdcontrol.socket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ObjectTypes {
	private ObjectTypes() {
	}

	private static final Map<String, ObjectType<?>> REGISTRY = new HashMap<>();

	public static <T extends JsonHolder<T>> ObjectType<T> register(@NotNull ObjectType<T> objectType) {
		if (REGISTRY.containsKey(objectType.id())) {
			throw new IllegalStateException("Object type '" + objectType + "' already registered");
		}

		REGISTRY.put(objectType.id(), objectType);
		return objectType;
	}

	public static <T extends JsonHolder<T>> ObjectType<T> register(String objectType, @NotNull JsonHolderFactory<T> factory) {
		return register(ObjectType.create(objectType, factory));
	}

	public static @Nullable ObjectType<?> factory(@NotNull String objectType) {
		return REGISTRY.get(objectType);
	}

	public static final @NotNull ObjectType<RequestNew> REQUEST = register("CrowdControl/Request", RequestNew::new);
	public static final @NotNull ObjectType<ResponseNew> RESPONSE = register("CrowdControl/Response", ResponseNew::new);
	public static final @NotNull ObjectType<RequestTarget> TARGET = register("CrowdControl/RequestTarget", RequestTarget::new);
	public static final @NotNull ObjectType<RequestSource> SOURCE = register("CrowdControl/RequestSource", RequestSource::new);
}
