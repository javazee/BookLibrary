package main.mapper;

import main.mapper.dto.AuthorDto;
import main.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(Author author);
}
