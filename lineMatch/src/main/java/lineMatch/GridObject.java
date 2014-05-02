package lineMatch;

/**
 * Created by JimmyLi on 2014/5/1.
 */
public class GridObject {

    private int gridColor;
    private int direction;
    private boolean answer;
    private boolean question;
    private String answerString;

    public int getGridColor() {
        return gridColor;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean isQuestion() {
        return question;
    }

    public void setQuestion(boolean question) {
        this.question = question;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public GridObject() {
    }

    public GridObject(int direction,int GridColor,boolean answer,boolean question,String answerString) {

        this.direction=direction;
        this.answer=answer;
        this.answerString=answerString;
        this.gridColor=GridColor;
        this.question=question;

    }
}
