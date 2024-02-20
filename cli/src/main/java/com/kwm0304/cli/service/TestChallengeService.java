import com.kwm0304.cli.repository.TestChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TestChallengeService {

    private final TestChallengeRepository testChallengeRepository;

    @Autowired
    public TestChallengeService(TestChallengeRepository testChallengeRepository) {
        this.testChallengeRepository = testChallengeRepository;
    }

    public List<TestChallenge> findAll() {
        return testChallengeRepository.findAll();
    }

    public Optional<TestChallenge> findById(Long id) {
        return testChallengeRepository.findById(id);
    }

    public TestChallenge save(TestChallenge testChallenge) {
        return testChallengeRepository.save(testChallenge);
    }

    public TestChallenge update(Long id, TestChallenge testChallengeDetails) {
        if (!testChallengeRepository.existsById(id)) {
            throw new RuntimeException("TestChallenge not found with id " + id);
        }
        testChallengeDetails.setId(id);
        return testChallengeRepository.save(testChallengeDetails);
    }

    public void deleteById(Long id) {
        if (!testChallengeRepository.existsById(id)) {
            throw new RuntimeException("TestChallenge not found with id " + id);
        }
        testChallengeRepository.deleteById(id);
    }
}
