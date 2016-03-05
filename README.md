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
  // if person is null, a NullPointException is thrown
  Things.verify(person);
  
  // if the list of possession is null or if the predicate fails the test (the list is empty), 
  // then the given runtimeException will be thrown
  List<String> possession = Things.verify(person.getPossession(), 
    new IllegalArgumentException("The person must have things"), list -> !list.isEmpty());
  
  possession.add("socks");
  
  Things.verify(possession, "Person have socks but no shoes", p -> !p.contains("shoes"));
  
  Person evilPerson = Things.verify(person, Person::isEvil);
  
  boolean canPilfer = Things(evilPerson.getAge() > 18 && "TakeAllMyStuff".equals(evilPerson.getName()) && possession.contains("million dollar"), "This is not the right person");
  
  
}
```



