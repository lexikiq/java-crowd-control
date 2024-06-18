package dev.qixils.crowdcontrol.socket;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.qixils.crowdcontrol.socket.JsonHolder.GSON;

public class JsonHolderBuilder<H extends JsonHolder<H>> {

	final @NotNull ObjectType<H> objectType;

	final @NotNull JsonObject serializedFields;
	final @NotNull Map<String, Object> deserializedFields;

	public JsonHolderBuilder(@NotNull ObjectType<H> objectType) {
		this.objectType = objectType;
		this.serializedFields = new JsonObject();
		this.deserializedFields = new HashMap<>();
	}

	public JsonHolderBuilder(@NotNull JsonHolder<H> holder) {
		this.objectType = holder.objectType;
		this.serializedFields = holder.serializedFields.deepCopy();
		this.deserializedFields = new HashMap<>(holder.deserializedFields);
	}

	/**
	 * Attempts to get a field from this object.
	 * May return empty if the field is {@link Field#supports(ObjectType) unsupported}
	 * or undefined.
	 *
	 * @param field the field to get
	 * @param <F>   the type of the field
	 * @return the deserialized field
	 * @since 4.0.0
	 */
	public <F> @NotNull Optional<F> get(@NotNull Field<F> field) {
		if (!field.supports(objectType)) return Optional.empty();
		// no point in checking serializedFields, the two are perfectly synced here (no lazy loading)
		// TODO: not necessarily true due to JsonHolder.toBuilder ?
		//noinspection unchecked
		return Optional.ofNullable((F) deserializedFields.get(field.id()));
	}

	/**
	 * Attempts to set a field on this object.
	 *
	 * @param field the field to set
	 * @param obj   the value to set
	 * @param <F>   the type of the field
	 * @since 4.0.0
	 */
	public <F> JsonHolderBuilder<H> set(@NotNull Field<F> field, @Nullable F obj) {
		if (!field.supports(objectType)) return this;

		if (obj == null) {
			serializedFields.remove(field.id());
			deserializedFields.remove(field.id());
			return this;
		}

		serializedFields.add(field.id(), GSON.toJsonTree(field.id(), field.type().getType()));
		deserializedFields.put(field.id(), obj);

		return this;
	}

	/**
	 * Attempts to unset a field on this object.
	 *
	 * @param field the field to unset
	 * @since 4.0.0
	 */
	public JsonHolderBuilder<H> unset(@NotNull Field<?> field) {
		if (!field.supports(objectType)) return this;

		serializedFields.remove(field.id());
		deserializedFields.remove(field.id());

		return this;
	}

	public @NotNull H build() {
		return objectType.factory().create(this);
	}
}
