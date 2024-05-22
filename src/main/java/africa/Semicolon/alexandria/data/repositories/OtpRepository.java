package africa.Semicolon.alexandria.data.repositories;

import africa.Semicolon.alexandria.data.models.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findByUsername(String username);
    void deleteByUsername(String username);
}
