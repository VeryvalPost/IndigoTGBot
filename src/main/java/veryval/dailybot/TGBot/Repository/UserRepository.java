package veryval.dailybot.TGBot.Repository;

import org.springframework.data.repository.CrudRepository;
import veryval.dailybot.TGBot.Entity.User;

@org.springframework.stereotype.Repository
public interface UserRepository extends CrudRepository<User,Long> {
}

