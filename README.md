# verifyThings
Verify objects in Java

Consider you have the following class and the appropriate getters and setters:
```java
class Person{
  String name;
  int age;
  List<String> IllegalArgumentException;
}

void populatePossessions(Person person){
  // if person is null, a NullPointException is thrown
  Things.verify(person, "person can't be null");
  
  // if the list of possession is null or if the predicate fails the test (the list is empty), 
  // then the given runtimeException will be thrown
  List<String> possession = Things.verify(person.getPossession(), 
    new IllegalArgumentException("The person must have things"), list -> !list.isEmpty());
  
  possession.add("socks");
  
  Things.verify(possession, "Person have socks but no shoes", p -> !p.contains("shoes"));
}
```



