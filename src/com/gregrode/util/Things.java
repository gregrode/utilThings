package com.gregrode.util;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The <code>Things</code> class provides functionality to create and verify
 * some of your favorite Java objects
 * 
 * @author Gregroy Dennis<br>
 *         Last updated: March 04, 2016<br>
 *         &copy; Gregroy Dennis 2016
 * 
 */
public final class Things
{

	private static final String EMPTY = "";

	private Things()
	{
	}

	/**
	 * Check whether the given object is null or not. if null, an
	 * <code>ErrorStatusException</code> is thrown.
	 * 
	 * @param obj
	 *            the object to check
	 * @return the object
	 */
	public static <T> T verify(T obj)
	{
		return verify(obj, new NullPointerException());
	}

	/**
	 * Check whether the given object is null or not. if null, an
	 * {@link NullPointerException} is thrown.
	 * 
	 * @param obj
	 *            the object to check
	 * @param message
	 *            the message within the exception
	 * @return obj
	 */
	public static <T> T verify(T obj, String message)
	{
		return verify(obj, new NullPointerException(message));
	}

	/**
	 * Check whether the given object is null or valid based on the given
	 * predicate. if the predicate fails, an {@link NullPointerException} is
	 * thrown.
	 * 
	 * @param obj
	 *            the object to check
	 * @param predicate
	 *            the predicate to test
	 * @return obj
	 */
	public static <T> T verify(T obj, Predicate<T> predicate)
	{
		return verify(obj, new NullPointerException(), predicate);
	}

	/**
	 * Check whether the given object is null or valid based on the given
	 * predicate. if the predicate fails, a {@link NullPointerException} is
	 * thrown.
	 * 
	 * @param obj
	 *            the object to check
	 * @param message
	 *            the message within the exception
	 * @param predicate
	 *            the predicate to test
	 * @return obj
	 */
	public static <T> T verify(T obj, String message, Predicate<T> predicate)
	{
		return verify(obj, new NullPointerException(message), predicate);
	}

	/**
	 * Check whether the given object is null or not. if null, an
	 * <code>RuntimeException</code> is thrown.
	 * 
	 * 
	 * @param obj
	 *            the object to check
	 * @param exception
	 *            the RuntimeException to throw
	 * @return object
	 */
	public static <T> T verify(T obj, RuntimeException exception)
	{
		return verify(obj, exception, o -> true);
	}

	/**
	 * 
	 * This method will throw an exception if one of the following cases occurs:
	 * <ol>
	 * <li>The object is null</li>
	 * <li>The object is an instance of a {@link Boolean} and the value is false
	 * </li>
	 * <li>The object is an instance of a {@link String} and the value is empty
	 * </li>
	 * <li>The object is an instance of a {@link Collection} and the value is
	 * empty</li>
	 * <li>The object is an instance of a {@link Map} the value is empty</li>
	 * <li>The result from the predicate is false</li>
	 * <li>The given exception is null</li>
	 * </ol>
	 * 
	 * 
	 * @param obj
	 *            the object to check
	 * @param exception
	 *            the RuntimeException to throw * @param predicate the predicate
	 *            to test
	 * @return object
	 */
	public static <T> T verify(T obj, RuntimeException exception, Predicate<T> predicate)
	{
		boolean bool = (obj != null);
		if (bool && (obj instanceof Boolean))
		{
			bool = ((Boolean) obj).booleanValue();
		}

		if (bool && (obj instanceof String))
		{
			bool = !((String) obj).isEmpty();
		}

		if (bool && (obj instanceof Collection))
		{
			bool = !((Collection<?>) obj).isEmpty();
		}

		if (bool && (obj instanceof Map))
		{
			bool = !((Map<?, ?>) obj).isEmpty();
		}

		if (!bool || ((predicate != null) && !predicate.test(obj)))
		{
			throw verify(exception, "Exception was not specified.");
		}
		return obj;
	}

	/**
	 * Get the first non null value with in the given varargs
	 * 
	 * @param things
	 *            the list of thing
	 * @return first non null thing
	 */
	@SafeVarargs
	public static <T> T either(T... things)
	{
		return Stream.of(things).filter(t -> t != null).findFirst().get();
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
	 * Create an implementation of the {@link Map} interface using the given
	 * {@Supplier} object and populate the map with the given list of
	 * {@link Entry}
	 * 
	 * @param mapSupplier
	 *            The implementation of the {@link Map} interface to user
	 * @param entries
	 *            the Key/value pair to populate the map with
	 * @return Map
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> toMap(Supplier<Map<K, V>> mapSupplier, Entry<K, V>... entries)
	{
		return Stream.of(verify(entries, "Entries not specified")).collect(
		        Collectors.toMap(Entry::getKey, Entry::getValue, (m, m2) -> m, verify(mapSupplier, "Implementation of Map was not specified. ")));
	}

	/**
	 * Create a HashMap from the given JSON object
	 *
	 * @param json
	 *            the JSON String
	 * @return Map
	 */
	public static Map<String, String> toMap(String json)
	{
		return toMap(HashMap::new, json);
	}

	/**
	 * Create an implementation of the {@link Map} interface using the given
	 * {@Supplier} object and populate the map with the given JSON
	 *
	 * @param json
	 *            the JSON String
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(Supplier<Map<K, V>> mapSupplier, String json)
	{
		try
		{
			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode jsonNode = mapper.readTree(verify(json, "Cannot transform null string in Map.").replaceAll("'", "\""));
			return mapper.treeToValue(jsonNode, mapSupplier.get().getClass());
		}
		catch (final IOException e)
		{
			return new HashMap<>();
		}
	}

	/**
	 * Create a {@link EnumMap} implementation of the {@link Map} interface
	 * using the enum constants as the keys and the result of the
	 * {@link Function#apply(Object)} as the value.
	 * 
	 * @param clazz
	 * @param function
	 * @return
	 */
	public static <K extends Enum<K>, R> Map<K, R> toMap(Class<K> clazz, Function<K, R> function)
	{
		final EnumMap<K, R> map = new EnumMap<K, R>(verify(clazz, "Enum class not specified."));
		Arrays.stream(clazz.getEnumConstants()).forEach(v -> {
			map.put(v, verify(function, "Function lambda not specified.").apply(v));
		});
		return map;
	}

	/**
	 * Create a {@link Entry} object using the {@link AbstractMap.SimpleEntry}
	 * implementation.
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            the value
	 * @return Entry
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

}
