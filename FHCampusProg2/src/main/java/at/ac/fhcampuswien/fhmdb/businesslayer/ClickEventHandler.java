package at.ac.fhcampuswien.fhmdb.businesslayer;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}
