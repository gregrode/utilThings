package com.gregrode.util;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The <code>Things</code> class provides functionality to create and verify some of your favorite Java objects
 *
 * @author Gregroy Dennis<br>
 *         Last updated: March 04, 2016<br>
 *         &copy; Gregroy Dennis 2016
 */
public final class Things
{

	private static final String EMPTY = "";

	private Things()
	{
	}

	/**
	 * Check whether the given object is null or not. if null, an {@link NullPointerException} is thrown.
	 *
	 * @param obj
	 *            the object to check
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t)
	{
		return verify(t, new NullPointerException());
	}

	/**
	 * Check whether the given object is null or not. if null, an {@link NullPointerException} is thrown.
	 *
	 * @param obj
	 *            the object to check
	 * @param message
	 *            the message within the exception
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t, String message)
	{
		return verify(t, new NullPointerException(message));
	}

	/**
	 * Check whether the given object is null or valid based on the given predicate. if the predicate fails, an
	 * {@link NullPointerException} is thrown.
	 *
	 * @param t
	 *            the object to check
	 * @param predicate
	 *            the predicate to test
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t, Predicate<T> predicate)
	{
		return verify(t, new NullPointerException(), predicate);
	}

	/**
	 * Check whether the given object is null or valid based on the given predicate. if the predicate fails, a
	 * {@link NullPointerException} is thrown.
	 *
	 * @param t
	 *            the object to check
	 * @param message
	 *            the message within the exception
	 * @param predicate
	 *            the predicate to test
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t, String message, Predicate<T> predicate)
	{
		return verify(t, new NullPointerException(message), predicate);
	}

	/**
	 * Check whether the given object is null or not. if null, an {@link RuntimeException} is thrown.
	 *
	 * @param t
	 *            the object to check
	 * @param exception
	 *            the {@link RuntimeException} to throw
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t, RuntimeException exception)
	{
		return verify(t, exception, o -> true);
	}

	/**
	 *
	 * This method will throw an instance of {@link RuntimeException} if one of the following cases occurs:
	 * <ol>
	 * <li>The object is null</li>
	 * <li>The object is an instance of a {@link Boolean} and the value is false</li>
	 * <li>The object is an instance of a {@link String} and the value is empty</li>
	 * <li>The object is an instance of a {@link Collection} and the value is empty</li>
	 * <li>The object is an instance of a {@link Map} the value is empty</li>
	 * <li>The result from the predicate is false</li>
	 * <li>The given exception is null</li>
	 * </ol>
	 *
	 *
	 * @param t
	 *            the object to check
	 * @param exception
	 *            the {@link RuntimeException} to throw
	 * @param predicate
	 *            the {@link Predicate} object to test
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	public static <T> T verify(T t, RuntimeException exception, Predicate<T> predicate)
	{
		boolean bool = (t != null);
		if (bool && (t instanceof Boolean))
		{
			bool = ((Boolean) t).booleanValue();
		}

		if (bool && (t instanceof String))
		{
			bool = !((String) t).isEmpty();
		}

		if (bool && (t instanceof Collection))
		{
			bool = !((Collection<?>) t).isEmpty();
		}

		if (bool && (t instanceof Map))
		{
			bool = !((Map<?, ?>) t).isEmpty();
		}

		if (!bool || ((predicate != null) && !predicate.test(t)))
		{
			throw verify(exception, "Exception was not specified.");
		}
		return t;
	}

	/**
	 * Get the first non null value with in the given varargs
	 *
	 * @param items
	 *            the list of items
	 * @param <T>
	 *            the type of object
	 * @return T
	 */
	@SafeVarargs
	public static <T> T either(T... items)
	{
		return Stream.of(verify(items, new IllegalArgumentException("Items not specified."))).filter(t -> t != null).findFirst()
			.get();
	}

	/**
	 * Get the first non zero integer within the given varargs
	 *
	 * @param things
	 *            the list of i
	 * @return first non zero integer
	 **/
	@SafeVarargs
	public static int either(int... things)
	{
		return Arrays.stream(things).filter(t -> t > 0).findFirst().getAsInt();
	}

	/**
	 * Create an implementation of the {@link Map} interface using the given {@link Supplier} object and populate the map with the
	 * given list of {@link Entry}
	 *
	 * @param mapSupplier
	 *            The implementation of the {@link Map} interface to user
	 * @param entries
	 *            the Key/value pair to populate the map with
	 * @param <K>
	 *            The type of object for the map key
	 * @param <V>
	 *            The type of object for the map value
	 * @param <M>
	 *            The type of object for the map
	 * @return Map
	 */
	@SafeVarargs
	public static <K, V, M extends Map<K, V>> M toMap(Supplier<M> mapSupplier, Entry<K, V>... entries)
	{
		return toMap(mapSupplier, Entry::getKey, Entry::getValue, entries);
	}

	/**
	 * Create an implementation of the {@link Map} interface using the given {@link Supplier} object and populate the map with the
	 * given Array of T objects
	 *
	 * @param mapSupplier
	 *            The implementation of the {@link Map} interface that will be used
	 * @param keyMapper
	 *            The keyMapper indicates how the key will be resolved
	 * @param valueMapper
	 *            the valueMapper indicates how the value will be resolved.
	 * @param items
	 *            an Array of T
	 *
	 * @param <K>
	 *            The type of object for the map key
	 * @param <V>
	 *            The type of object for the map value
	 * @param <M>
	 *            The type of object for the map
	 * @param <T>
	 *            the type of object in the Collection
	 * @return {@link Map}
	 */
	@SafeVarargs
	public static <K, V, M extends Map<K, V>, T> M toMap(Supplier<M> mapSupplier, Function<T, K> keyMapper,
			Function<T, V> valueMapper, T... items)
	{
		return toMap(mapSupplier, keyMapper, valueMapper, Arrays.asList(verify(items)));
	}

	/**
	 * Create a {@link HashMap} implementation of the {@link Map} interface using the given the {@link Function} objects to
	 * determine the key/value and populate the map with the given {@link Collection}.
	 *
	 * @param keyMapper
	 *            The keyMapper indicates how the key will be resolved
	 * @param valueMapper
	 *            the valueMapper indicates how the value will be resolved.
	 * @param items
	 *            the collection of items
	 *
	 * @param <K>
	 *            The type of object for the map key
	 * @param <V>
	 *            The type of object for the map value
	 * @param <T>
	 *            the type of object in the Collection
	 * @param <C>
	 *            The type of object for the Collection
	 * @return {@link Map}
	 */
	public static <K, V, T, C extends Collection<T>> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper,
			C items)
	{
		return toMap(HashMap::new, keyMapper, valueMapper, items);
	}

	/**
	 * Create an implementation of the {@link Map} interface using the given {@link Supplier} object, the {@link Function} objects
	 * to determine the key/value and populate the map with the given {@link Collection}.
	 *
	 * @param mapSupplier
	 *            The implementation of the {@link Map} interface that will be used
	 * @param keyMapper
	 *            The keyMapper indicates how the key will be resolved
	 * @param valueMapper
	 *            the valueMapper indicates how the value will be resolved.
	 * @param items
	 *            the collection of items
	 * @param <K>
	 *            The type of object for the map key
	 * @param <V>
	 *            The type of object for the map value
	 * @param <M>
	 *            The type of object for the map
	 * @param <T>
	 *            the type of object in the Collection
	 * @param <C>
	 *            The type of object for the Collection
	 * @return {@link Map}
	 */
	public static <K, V, M extends Map<K, V>, T, C extends Collection<T>> M toMap(Supplier<M> mapSupplier,
			Function<T, K> keyMapper, Function<T, V> valueMapper, C items)
	{
		return verify(items, new IllegalArgumentException("Collection not specified.")).stream()
			.collect(Collectors.toMap(verify(keyMapper, new IllegalArgumentException("Key mapper not specified.")),
				verify(valueMapper, new IllegalArgumentException("Value mapper not specified.")), (m, m2) -> m,
				verify(mapSupplier, new IllegalArgumentException("Implementation of Map was not specified."))));
	}

	/**
	 * Create a {@link HashMap} from the given JSON object
	 *
	 * @param json
	 *            the JSON String
	 * @return {@link Map}
	 */
	public static Map<String, String> toMap(String json)
	{
		return toMap(HashMap::new, json);
	}

	/**
	 * Create an implementation of the {@link Map} interface using the given {@link Supplier} object and populate the map with the
	 * given JSON
	 *
	 * @param json
	 *            the JSON String
	 * @param <K>
	 *            The type of object for the map key.
	 * @param <V>
	 *            The type of object for the map value
	 * @return {@link Map}
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, M extends Map<K, V>> Map<K, V> toMap(Supplier<M> mapSupplier, String json)
	{
		try
		{
			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode jsonNode = mapper.readTree(verify(json, "Cannot transform null string in Map.").replaceAll("'", "\""));
			return mapper.treeToValue(jsonNode, verify(mapSupplier).get().getClass());
		}
		catch (final IOException e)
		{
			return new HashMap<>();
		}
	}

	/**
	 * Create a {@link EnumMap} implementation of the {@link Map} interface using the enum constants as the keys and the result of
	 * the {@link Function#apply(Object)} as the value.
	 *
	 * @param clazz
	 *            the class of the enum
	 * @param function
	 *            the functional interface that indicates how the value will be resolved.
	 *
	 * @param <K>
	 *            The type of object for the map key. Note, in this case the key will be constant within the enum.
	 * @param <V>
	 *            The type of object for the map value
	 *
	 * @return {@link Map}
	 */
	public static <K extends Enum<K>, V> Map<K, V> toMap(Class<K> clazz, Function<K, V> function)
	{
		final EnumMap<K, V> map = new EnumMap<K, V>(verify(clazz, "Enum class not specified."));
		Arrays.stream(clazz.getEnumConstants()).forEach(v -> {
			map.put(v, verify(function, "Function lambda not specified.").apply(v));
		});
		return map;
	}

	/**
	 * Create a {@link Entry} object using the {@link AbstractMap.SimpleEntry} implementation.
	 *
	 * @param key
	 *            The key
	 * @param value
	 *            the value
	 *
	 * @param <K>
	 *            The type of object for the key.
	 * @param <V>
	 *            The type of object for the value.
	 * @return {@link Entry}
	 */
	public static <K, V> Entry<K, V> pair(K key, V value)
	{
		return new AbstractMap.SimpleEntry<>(key, value);
	}

	/**
	 * Convert the given object in the a valid JSON object.
	 *
	 * @param obj
	 *            The object to transformed into a JSON object.
	 * @return String
	 */
	public static String toJSON(Object obj)
	{
		try
		{
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(verify(obj, "Cannot transform null object in JSON."));
		}
		catch (final IOException e)
		{
			return EMPTY;
		}
	}

	/**
	 * Pluck value out of the given {@link Collection} based on the given {@link Function} object and return them as a Collection
	 *
	 * @param items
	 *            the collection of itmes
	 * @param function
	 *            the functional interface that determine what value will be plucked.
	 * @return {@link Collection}
	 */
	public static <T, R> Collection<R> pluck(Collection<T> items, Function<T, R> function)
	{
		return verify(items, new IllegalArgumentException("Collection not specified")).stream().collect(Collectors
			.mapping(verify(function, new IllegalArgumentException("function lambda not specified")), Collectors.toList()));
	}

	/**
	 * @param t
	 * @param consumer
	 * @return
	 */
	public static <T> T build(T t, Consumer<T> consumer)
	{
		consumer.accept(t);
		return t;
	}

}
