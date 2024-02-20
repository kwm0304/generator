import com.kwm0304.cli.repository.TestUserRepository;
import com.kwm0304.cli.test.TestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TestUserService {

    private final TestUserRepository testUserRepository;

    @Autowired
    public TestUserService(TestUserRepository testUserRepository) {
        this.testUserRepository = testUserRepository;
    }

    public List<TestUser> findAll() {
        return testUserRepository.findAll();
    }

    public Optional<TestUser> findById(Long id) {
        return testUserRepository.findById(id);
    }

    public TestUser save(TestUser testUser) {
        return testUserRepository.save(testUser);
    }

    public TestUser update(Long id, TestUser testUserDetails) {
        if (!testUserRepository.existsById(id)) {
            throw new RuntimeException("TestUser not found with id " + id);
        }
        testUserDetails.setId(id);
        return testUserRepository.save(testUserDetails);
    }

    public void deleteById(Long id) {
        if (!testUserRepository.existsById(id)) {
            throw new RuntimeException("TestUser not found with id " + id);
        }
        testUserRepository.deleteById(id);
    }
}
