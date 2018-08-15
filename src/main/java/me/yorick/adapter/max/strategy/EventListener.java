package me.yorick.adapter.max.strategy;

public interface EventListener<T> {

	public void onEvent(int id, T event);

	public void removeEvent(int id);

}
