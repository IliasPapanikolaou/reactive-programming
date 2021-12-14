package com.unipi.reactor.p3Operators;


import com.unipi.reactor.util.Util;
import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/*
* Transform is like a custom Operator, it packs a set
* of instructions that we can use repetitively in pipelines.
*/
public class Lec10Transform {

    public static void main(String[] args) {

        getPerson()
                .transform(applyFilterMap()) // Apply custom function
                .subscribe(Util.subscriber());
    }

    // Publisher method of a Person
    public static Flux<Person> getPerson()  {
        return Flux.range(1, 10)
                .map(i -> new Person());
    }

    /*
    * Java 8, Function is a functional interface;
    * it takes an argument (object of type T) and returns an object (object of type R).
    * The argument and output can be a different type.
    *
    * Example:
    * Function<String, Integer> func = x -> x.length();
    * Integer apply = func.apply("test");   // output: 4
    * System.out.println(apply);
    */
    public static Function<Flux<Person>, Flux<Person>> applyFilterMap() {
        return flux -> flux
                .filter(p -> p.getAge() > 18)
                .doOnNext(p -> p.setName(p.getName().toUpperCase()))
                .doOnDiscard(Person.class, p -> System.out.println("Not allowed: " + p));
    }
}

@Data
@ToString
class Person {

    private String name;
    private int age;

    public Person() {
        this.name = Util.faker().name().firstName();
        this.age = Util.faker().random().nextInt(1, 60);
    }
}
