package f.models;

public class Card {
    private Long id;
    private String question;
    private String answer;
    private Long pack;
    private Integer done;

    public Card() {}

    public Card(Long id, String question, String answer, Integer done, Long pack) {
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

    public Long getPack() {
        return pack;
    }

    public void setPack(Long pack) {
        this.pack = pack;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
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
