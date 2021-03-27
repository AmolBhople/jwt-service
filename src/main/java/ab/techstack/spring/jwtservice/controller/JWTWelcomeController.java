package ab.techstack.spring.jwtservice.controller;

import ab.techstack.spring.jwtservice.entity.AuthRequest;
import ab.techstack.spring.jwtservice.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTWelcomeController {

    Logger logger = LoggerFactory.getLogger(JWTWelcomeController.class);
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/")
    public String welcomeToJWT(){
        return "Welcome User";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {

        logger.info("authRequest=>  Its not good to log the pwd in logs :smile : "+authRequest.toString());


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
        );
        }catch (BadCredentialsException exception){
            throw new Exception("Invalid username/password");
        }

        String token = jwtUtil.generateToken(authRequest.getUserName());
        return ResponseEntity.ok(new String (token));
    }


}
