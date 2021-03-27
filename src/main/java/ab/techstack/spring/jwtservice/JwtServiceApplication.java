package ab.techstack.spring.jwtservice;

import ab.techstack.spring.jwtservice.entity.User;
import ab.techstack.spring.jwtservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableEurekaClient
public class JwtServiceApplication {

	Logger logger = LoggerFactory.getLogger(JwtServiceApplication.class);
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JwtServiceApplication.class, args);
	}

	@PostConstruct
	public void initUsers(){
		logger.info("Post Construct :");
		List<User> userList = Stream.of(
				new User(1, "amol1", "pass1", "a1@a.com"),
				new User(2, "amol2", "pass2", "a2@a.com"),
				new User(3, "amol3", "pass3", "a3@a.com"),
				new User(4, "amol4", "pass4", "a4@a.com"),
				new User(5, "amol5", "pass5", "a5@a.com")
		).collect(Collectors.toList());

		logger.info("List: "+userList);
		userRepository.saveAll(userList);
	}
}
