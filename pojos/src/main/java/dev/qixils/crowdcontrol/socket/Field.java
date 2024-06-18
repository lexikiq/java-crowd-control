package dev.qixils.crowdcontrol.socket;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Field<T> {

	private final List<String> ids;
	private final boolean isTransient;
	private final TypeToken<T> type;
	private final Set<String> supports;

	private Field(Collection<String> ids, Collection<String> supports, TypeToken<T> type, boolean isTransient) {
		this.ids = Collections.unmodifiableList(new ArrayList<>(new LinkedHashSet<>(ids)));
		this.supports = Collections.unmodifiableSet(new HashSet<>(supports));
		this.type = type;
		this.isTransient = isTransient;

		assert !this.ids.isEmpty();
		assert !this.supports.isEmpty();
	}

	public String id() {
		return ids.get(0);
	}

	public List<String> ids() {
		return ids;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public TypeToken<T> type() {
		return type;
	}

	public boolean supports(String objectType) {
		return supports.contains(objectType);
	}

	public boolean supports(ObjectType<?> objectType) {
		return supports(objectType.id());
	}

	public static final class Builder<T> {
		// TODO: this feels counter to the point of JsonHolder but i think making a dynamic builder here might be too meta, or maybe not IDK i guess i could detach dynamic building from json
		private final List<String> ids = new ArrayList<>();
		private boolean isTransient;
		private final TypeToken<T> type;
		private final Set<String> supports = new HashSet<>();

		public Builder(TypeToken<T> type) {
			this.type = type;
		}

		public Builder(Class<T> clazz) {
			this.type = TypeToken.get(clazz);
		}

		public Builder<T> addId(@NotNull String id) {
			this.ids.add(id);
			return this;
		}

		public Builder<T> addIds(@NotNull String @NotNull ... ids) {
			this.ids.addAll(Arrays.asList(ids));
			return this;
		}

		public Builder<T> setId(@NotNull String id) {
			this.ids.clear();
			return addId(id);
		}

		public Builder<T> setIds(@NotNull String @NotNull ... ids) {
			this.ids.clear();
			return addIds(ids);
		}

		public Builder<T> isTransient(boolean isTransient) {
			this.isTransient = isTransient;
			return this;
		}

		public Builder<T> setTransient() {
			return isTransient(true);
		}

		public Builder<T> addSupport(String objectType) {
			this.supports.add(objectType);
			return this;
		}

		public Builder<T> addSupports(String... objectTypes) {
			this.supports.addAll(Arrays.asList(objectTypes));
			return this;
		}

		public Builder<T> setSupport(String objectType) {
			this.supports.clear();
			return addSupport(objectType);
		}

		public Builder<T> setSupport(String... objectTypes) {
			this.supports.clear();
			return addSupports(objectTypes);
		}

		public Field<T> build() {
			return new Field<>(ids, supports, type, isTransient);
		}
	}

}
