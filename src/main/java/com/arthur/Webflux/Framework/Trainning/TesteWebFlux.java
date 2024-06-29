package com.arthur.Webflux.Framework.Trainning;

import reactor.core.publisher.Flux;

public class TesteWebFlux {

    public static void main(String args[]){
        Flux.just(1, 2, 0, 5, 67)
                .map(i -> 10 / i)
                .onErrorResume(e -> Flux.just(-1, 4 ,2 , 2))
                .subscribe(System.out::println);
    }
}
