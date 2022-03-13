


/**
 * Class which holds relevant information about a document
 */
public class DocumentInfo {
    private float rank;
    private int maxLength;
    private int maxLengthOccurrences;

    public DocumentInfo() {

    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxLengthOccurrences(int maxLengthOccurrences) {
        this.maxLengthOccurrences = maxLengthOccurrences;
    }

    public float getRank() {
        return rank;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMaxLengthOccurrences() {
        return maxLengthOccurrences;
    }
}
