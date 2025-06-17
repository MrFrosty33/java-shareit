package ru.practicum.server.utilities;

public interface DataEnricher<D, E> {
    D getDto(E entity);

    E getEntity(D dto);
}
