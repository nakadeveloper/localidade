package com.fnaka.localidade;

import com.fnaka.localidade.domain.pais.PaisID;
import com.github.javafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static final class Pais {

        public static String nome() {
            return FAKER.country().name();
        }
    }


}
