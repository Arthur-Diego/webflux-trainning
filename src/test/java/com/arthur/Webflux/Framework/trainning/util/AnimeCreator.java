package com.arthur.webflux.framework.trainning.util;

import com.arthur.webflux.framework.trainning.model.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .nome("Sensei Shitara Slime Datta")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1)
                .nome("Sensei Shitara Slime Datta")
                .build();
    }

    public static Anime createValideUpdatedAnime(){
        return Anime.builder()
                .id(1)
                .nome("Sensei Shitara Slime Datta 2")
                .build();
    }
}
