package ua.gov.mva.vfaces.data.db.child;

import java.util.List;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.database.DataSnapshot#getValue(Class)}.
 */
public class ItemDao {

    private String question;
    private List<String> answers;

    public ItemDao(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public ItemDao() {
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
