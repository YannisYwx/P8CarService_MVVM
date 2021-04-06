package com.p8.common.binding.command;

/**
 * @author : WX.Y
 * date : 2021/3/24 14:47
 * description : Represents a function with zero arguments.
 * @param <T> the result type
 */
public interface BindingFunction<T> {
    T call();
}
