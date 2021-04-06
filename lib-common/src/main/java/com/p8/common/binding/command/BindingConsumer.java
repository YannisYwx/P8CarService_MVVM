package com.p8.common.binding.command;

/**
 * @author : WX.Y
 * date : 2021/3/24 14:47
 * description :
 */
public interface BindingConsumer<T> {
    void call(T t);
}
