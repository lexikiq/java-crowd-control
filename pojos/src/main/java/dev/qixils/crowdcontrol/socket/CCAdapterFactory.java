package dev.qixils.crowdcontrol.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.qixils.crowdcontrol.util.PostProcessable;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;

@ApiStatus.Internal
public class CCAdapterFactory implements TypeAdapterFactory {
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
		final TypeAdapter<JsonObject> objectDelegate = gson.getDelegateAdapter(this, new TypeToken<JsonObject>(){});

		return new TypeAdapter<T>() {
			public void write(JsonWriter out, T value) throws IOException {
				delegate.write(out, value);
			}

			public T read(JsonReader in) throws IOException {
				final T obj;

				// handle
				if (TypeToken.get(JsonHolder.class).isAssignableFrom(type)) {
					try {
						JsonObject jsonObject = objectDelegate.read(in);
						obj = (T) type.getRawType().getConstructor(JsonObject.class).newInstance(jsonObject);
					} catch (Exception e) {
						throw new IllegalStateException("Failed to instantiate JsonHolder", e);
					}
				} else {
					obj = delegate.read(in);
				}

				if (obj instanceof PostProcessable) {
					((PostProcessable)obj).postProcess();
				}

				return obj;
			}
		};
	}
}
