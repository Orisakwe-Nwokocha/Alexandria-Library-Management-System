package africa.Semicolon.alexandria.data.repositories;

import africa.Semicolon.alexandria.data.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataMongoTest
public class UsersTest {
    @Autowired
    private Users users;

    @Test
    public void usersTest(){
        User newUser = new User();
        assertThat(users.count(), is(0L));
        var savedUser = users.save(newUser);
        assertThat(savedUser.getId(), notNullValue());
        assertThat(users.count(), is(1L));
    }

}