package ru.practicum.shareit.utilities;

public interface DataEnricher<D, E> {
    D getDto(E entity);

    E getEntity(D dto);
}
