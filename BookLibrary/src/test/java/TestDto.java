import main.mapper.AuthorMapper;
import main.mapper.dto.AuthorDto;
import main.model.Author;
import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class TestDto {

    @Test
    public void testAuthorToAuthorDto(){
        AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);
        Author author = new Author();
        author.setId(1);
        author.setFirstName("Alexander");
        author.setLastName("Pushkin");
        AuthorDto authorDto = authorMapper.toDto(author);
        Assert.assertEquals(author.getId(), authorDto.getId());
        Assert.assertEquals(author.getFirstName(), authorDto.getFirstName());
        Assert.assertEquals(author.getLastName(), authorDto.getLastName());
    }
}
