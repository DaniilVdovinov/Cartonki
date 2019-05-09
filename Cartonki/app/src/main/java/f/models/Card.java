package f.models;

import java.time.Instant;

public class Card {
    private Long id;
    private String question;
    private String answer;
    private Integer pack;
    private Boolean done;

    public Card() {}

    public Card(Long id, String question, String answer, Boolean done, Integer pack) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.done = done;
        this.pack = pack;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPack() {
        return pack;
    }

    public void setPack(Integer pack) {
        this.pack = pack;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", pack=" + pack +
                '}';
    }
}
