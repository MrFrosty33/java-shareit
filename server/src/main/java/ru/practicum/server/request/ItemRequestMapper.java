package ru.practicum.server.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.models.request.CreateItemRequestDto;
import ru.practicum.models.request.ItemRequestAnswer;
import ru.practicum.models.request.ItemRequestDto;
import ru.practicum.server.item.Item;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "requesterId", source = "itemRequest.requester.id")
    @Mapping(target = "items", ignore = true)
    public ItemRequestDto toDto(ItemRequest itemRequest);

    @Mapping(target = "requester", ignore = true)
    public ItemRequest toEntity(ItemRequestDto itemRequestDto);

    @Mapping(target = "ownerId", source = "item.owner.id")
    public ItemRequestAnswer mapAnswerFromItemEntity(Item item);

    @Mapping(target = "requester", ignore = true)
    public ItemRequest mapEntityFromCreateRequest(CreateItemRequestDto createRequest);

    @Mapping(target = "items", ignore = true)
    public ItemRequestDto mapDtoFromCreateRequest(CreateItemRequestDto createRequest);

    @Mapping(target = "requesterId", source = "itemRequest.requester.id")
    public CreateItemRequestDto mapCreateRequestFromEntity(ItemRequest itemRequest);
}
