package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest { //TODO: Il y a rien a tester pour l instant
    private User userTest;
    @BeforeEach
    void init(){
        String pseudo = "Zanilia";
        String password = "fei";
        userTest = new User(pseudo, password,false);
    }
}