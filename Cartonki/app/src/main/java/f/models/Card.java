package f.models;

import java.time.Instant;

public class Card {
    private Long id;
    private String question;
    private String answer;
    private Integer period;
    private Instant instant;
    private Integer pack;

    public Card(Long id, String question, String answer,
                Integer period, Instant instant, Integer pack) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.period = period;
        this.instant = instant;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Integer getPack() {
        return pack;
    }

    public void setPack(Integer pack) {
        this.pack = pack;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", period=" + period +
                ", instant=" + instant +
                ", pack=" + pack +
                '}';
    }
}
