package ru.practicum.shareit.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Уберу пока все проверки @NotNull, @Positive. Все это отсеивается ещё в DTO объекте
    // и в случае какого-то неподходящего значения, его дальше не даст вставить сама БД по ограничения полей таблицы

    @Column
    private String name;

    @Column
    private String description;

    // стоит ли их подтягивать сразу, или всё же, как понадобятся?
    // в целом, везде пока только в едином числе другие сущности, нагружать БД сильно не должно
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Только ли один запрос может быть у предмета?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Column(name = "availability")
    private Boolean available;
}
