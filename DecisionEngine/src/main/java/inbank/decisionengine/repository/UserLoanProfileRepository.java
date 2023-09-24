package inbank.decisionengine.repository;


import inbank.decisionengine.model.UserLoanProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoanProfileRepository extends JpaRepository<UserLoanProfile, Long> {

}
