package dev.qixils.crowdcontrol.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An object that holds an arbitrary set of JSON-encoded {@link Field}s.
 * <p>
 * Implementations of this class should have a public constructor which takes in only a {@link JsonObject}.
 *
 * @since 4.0.0
 */
public abstract class JsonHolder<H extends JsonHolder<H>> {

	/**
	 * A GSON instance which can be used to (de)serialize JsonHolders.
	 *
	 * @since 4.0.0
	 */
	public static final Gson GSON = new GsonBuilder()
		.registerTypeAdapterFactory(new CCAdapterFactory())
		.registerTypeAdapter(Request.Type.class, new ByteAdapter<>(Request.Type::from))
		.registerTypeAdapter(Response.ResultType.class, new ByteAdapter<>(Response.ResultType::from))
		.registerTypeAdapter(Response.PacketType.class, new ByteAdapter<>(Response.PacketType::from))
		.registerTypeAdapter(IdType.class, new ByteAdapter<>(IdType::from))
		.registerTypeAdapter(Duration.class, new DurationAdapter())
		.create();

	/**
	 * The string that uniquely identifies this type of object.
	 * <p>
	 * It is recommended to use a string unique to your project.
	 * For example, if your project is named MyMinecraftAddon and your object is called PlayerData,
	 * you might use the string {@code MyMinecraftAddon:PlayerData}.
	 *
	 * @since 4.0.0
	 */
	protected final ObjectType<H> objectType;

	/**
	 * The serialized JSON object data wrapped by this holder.
	 *
	 * @since 4.0.0
	 */
	protected final JsonObject serializedFields;

	/**
	 * The deserialized JSON object data wrapped by this holder.
	 *
	 * @since 4.0.0
	 */
	protected final Map<String, Object> deserializedFields;

	protected final Logger logger;

	/**
	 * Creates a new JSON holder.
	 *
	 * @param objectType the {@link #objectType}
	 * @param data       the serialized JSON data
	 * @since 4.0.0
	 */
	protected JsonHolder(ObjectType<H> objectType, @NotNull JsonObject data) {
		this.objectType = objectType;
		this.serializedFields = data.deepCopy();
		this.deserializedFields = new HashMap<>();
		this.logger = LoggerFactory.getLogger(objectType.id());
	}

	/**
	 * Creates a new JSON holder from a builder.
	 *
	 * @param builder JSON holder builder
	 * @since 4.0.0
	 */
	protected JsonHolder(@NotNull JsonHolderBuilder<?> builder) {
		//noinspection unchecked
		this.objectType = (ObjectType<H>) builder.objectType;
		this.serializedFields = builder.serializedFields;
		this.deserializedFields = builder.deserializedFields;
		this.logger = LoggerFactory.getLogger(objectType.id());
	}

	/**
	 * Utility method for obtaining this holder's serialized data as a string.
	 * Equivalent to {@link Gson#toJson(Object)} on {@link #GSON}.
	 *
	 * @return JSON string
	 * @since 4.0.0
	 */
	public @NotNull String toJSON() {
		return GSON.toJson(serializedFields);
	}

	/**
	 * Gets the name that identifies which object this is.
	 *
	 * @return object name
	 * @since 4.0.0
	 */
	public ObjectType<H> objectName() {
		return objectType;
	}

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
	public <T> @NotNull Optional<T> get(@NotNull Field<T> field) {
		if (!field.supports(objectType)) return Optional.empty();

		if (deserializedFields.containsKey(field.id()))
			//noinspection unchecked
			return Optional.ofNullable((T) deserializedFields.get(field.id()));

		for (String id : field.ids()) {
			if (!serializedFields.has(id)) continue;

			JsonElement serial = serializedFields.get(id);
			T obj = GSON.fromJson(serial, field.type());
			deserializedFields.put(field.id(), obj);
			return Optional.of(obj);
		}

		return Optional.empty();
	}

	/**
	 * Attempts to set a field on this object.
	 * Intended for use by {@link dev.qixils.crowdcontrol.util.PostProcessable} objects.
	 *
	 * @param field the field to set
	 * @param obj   the value to set
	 * @param <F>   the type of the field
	 * @since 4.0.0
	 */
	protected <F> void set(@NotNull Field<F> field, @Nullable F obj) {
		if (!field.supports(objectType)) return;

		unset(field);

		if (obj == null) return;

		deserializedFields.put(field.id(), obj);

		if (!field.isTransient())
			serializedFields.add(field.id(), GSON.toJsonTree(field.id(), field.type().getType()));
	}

	/**
	 * Attempts to unset a field on this object.
	 * Intended for use by {@link dev.qixils.crowdcontrol.util.PostProcessable} objects.
	 *
	 * @param field the field to unset
	 * @since 4.0.0
	 */
	protected void unset(@NotNull Field<?> field) {
		if (!field.supports(objectType)) return;

		for (String id : field.ids())
			serializedFields.remove(id);
		deserializedFields.remove(field.id());
	}

	/**
	 * Creates a builder matching this object.
	 *
	 * @returns a new builder with a copy of this object's data
	 * @since 4.0.0
	 */
	public JsonHolderBuilder<H> toBuilder() {
		return new JsonHolderBuilder<>(this);
	}
}
