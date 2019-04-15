package die.mass.repositories;

import die.mass.models.Card;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardsRepositoryJdbcImpl implements CardsRepository {

    private Connection connection;

    //language=SQL
    private final String SQL_INSERT_USER = "insert into " +
            "card (question, answer, period, date, pack) values (?, ?, ?, ?, ?);";
    //language=SQL
    private final String SQL_UPDATE_USER = "update card set " +
            "(question, answer, period, date, pack) = (?, ?, ?, ?, ?) where id = ?;";

    public CardsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Card> userRowMapper = row -> {
        Long id = row.getLong("id");
        String question = row.getString("question");
        String answer = row.getString("answer");
        Integer period = row.getInt("period");
        Instant instant = row.getTimestamp("date").toInstant();
        Integer pack = row.getInt("pack");
        return new Card(id,question,answer,period,instant,pack);
    };

    @Override
    public void save(Card card) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, card.getQuestion());
            statement.setString(2, card.getAnswer());
            statement.setInt(3, card.getPeriod());
            statement.setTimestamp(4, Timestamp.from(card.getInstant()));
            statement.setInt(5, card.getPack());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException();
            }

            ResultSet generatesKeys = statement.getGeneratedKeys();

            if (generatesKeys.next()) {
                card.setId(generatesKeys.getLong("id"));
            } else {
                throw new SQLException();
            }
            statement.close();
            generatesKeys.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(Card card) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, card.getQuestion());
            statement.setString(2, card.getAnswer());
            statement.setInt(3, card.getPeriod());
            statement.setTimestamp(4, Timestamp.from(card.getInstant()));
            statement.setInt(5, card.getPack());
            statement.setLong(6,card.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(Long id) {
//        if (!find(id).isPresent()) {
//            System.err.println("Элемента уже нема ало");
//        }
        try {
            Statement statement = connection.createStatement();
            statement.execute("delete from card where id = " + id + ";");
            System.out.println("Deleted is comleted");
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Card> find(Long id) {
        Card card = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from card where id = " + id + ";");

            if (resultSet.next()) {
                card = userRowMapper.mapRow(resultSet);
            }
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return Optional.ofNullable(card);
    }

    @Override
    public List<Card> findAll() {
        List<Card> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from card");

            while (resultSet.next()) {
                Card card = userRowMapper.mapRow(resultSet);
                result.add(card);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public Optional<Card> findOneByQuestion(String firstName) {
        // см. CardsRepository
        return Optional.empty();
    }
}
