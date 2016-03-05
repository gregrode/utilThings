# verifyThings
Verify objects in Java

Consider you have the following class and the appropriate getters and setters:
```java

	class Person{
	  String name;
	  int age;
	  List<String> possession;
	  boolean evil;
	}

	void test(Person person){
		// if the person object is null, a NullPointException is thrown
		Person p1 = Things.verify(person);
		  
		// if the person object is null or the predicate fails the test, then a NullPointerException is thrown.
		Person p2 = Things.verify(person, Person::isEvil);
		
		// if the person's list of possessions is null, then a NullPointerException with the given message is thrown.
		List<String> possession = Things.verify(person.getPossession(), "The person must have things");
		
		// Throws an instance of the RunTimeException ( IllegalArgumentException) if the list is either null or empty   
		possession = Things.verify(person.getPossession(), new IllegalArgumentException("The person must have things"), list -> !list.isEmpty());
	
		// throws a NPE if the given boolean argument evaluates to false
		boolean canPilfer = Things.verify((evilPerson.getAge() > 18 && "TakeAllMyStuff".equals(evilPerson.getName()) && possession.contains("million dollar")), "This is not the right person"); 
	}
```



