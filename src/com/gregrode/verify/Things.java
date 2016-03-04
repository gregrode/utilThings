package com.gregrode.verify;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The <code>Things</code> class provides functionality to verify if an object
 * is null or not.
 * 
 * @author Gregroy Dennis<br>
 *         Last updated: March 04, 2016<br>
 *         &copy; Gregroy Dennis 2016
 * 
 */
public final class Things
{

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
	 * <code>ErrorStatusException</code> is thrown.
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
	 * Check whether the given object is valid based on the given predicate. if
	 * the predicate fails, an <code>ErrorStatusException</code> is thrown.
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
	 * Check whether the given object is valid based on the given predicate. if
	 * the predicate fails, an <code>ErrorStatusException</code> is thrown.
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
	 * <li>The object is an instance of boolean and the value is false</li>
	 * <li>The result from the predicate is false</li>
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
		if ((obj == null) || ((obj instanceof Boolean) && !((Boolean) obj).booleanValue()) || ((predicate != null) && !predicate.test(obj)))
		{
			throw verify(exception, "Exception not specified.");
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

}
