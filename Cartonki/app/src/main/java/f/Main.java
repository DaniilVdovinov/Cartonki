package f;

import f.models.Card;
import f.repositories.CardsRepository;
import f.repositories.CardsRepositoryJdbcImpl;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

//import java.time.Instant;
//import java.util.Optional;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Connection connection = addConnection();

        CardsRepository cardsRepository = new CardsRepositoryJdbcImpl(connection);
//        Optional<Card> cardCandidate = null;
//        cardCandidate = cardsRepository.find(45L);

//        if (cardCandidate.isPresent()) {
//            System.out.println(cardCandidate.get());
//        } else {
//            System.err.println("В бд такого элемента нема"); //TODO: сделать чтобы в андроиде норм робило
//        }


//        cardsRepository.findAll().forEach(element -> System.out.println(element));
//        cardsRepository.save(new Card(null,"","8",0,Instant.now(),2));
//        cardsRepository.update(new Card(1L,"2","3",0,Instant.now(),1));
        cardsRepository.delete(45L);
    }

    private static Connection addConnection() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String url = properties.getProperty("db.url");

        Connection connection;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
