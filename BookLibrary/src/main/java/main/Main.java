package main;

import main.mapper.AuthorMapper;
import main.mapper.dto.AuthorDto;
import main.model.Author;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);
        Author author = new Author();
        author.setId(1);
        author.setFirstName("Alexander");
        author.setLastName("Pushkin");
        AuthorDto authorDto = authorMapper.toDto(author);
        System.out.println(authorDto.getId());
        System.out.println(authorDto.getFirstName());
        System.out.println(authorDto.getLastName());
        SpringApplication.run(Main.class, args);
    }
}
