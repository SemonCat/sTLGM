package lineMatch;

/**
 * Created by JimmyLi on 2014/4/24.
 */
public class BoxObject {
    private int xIndex;
    private int yIndex;
    private String answer;

    public BoxObject(int xIndex, int yIndex,String answer) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.answer=answer;
    }

    public int getXIndex() {
        return xIndex;
    }

    public void setXIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public void setYIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoxObject)) return false;

        BoxObject boxObject = (BoxObject) o;

        if (answer != null ? !answer.equals(boxObject.answer) : boxObject.answer != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return answer != null ? answer.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BoxObject{" +
                "xIndex=" + xIndex +
                ", yIndex=" + yIndex +
                ", answer='" + answer + '\'' +
                '}';
    }
}
