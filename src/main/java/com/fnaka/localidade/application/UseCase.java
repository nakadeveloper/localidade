package com.fnaka.localidade.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}
