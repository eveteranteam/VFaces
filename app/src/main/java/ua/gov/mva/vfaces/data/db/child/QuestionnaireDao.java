package ua.gov.mva.vfaces.data.db.child;

import java.util.List;

/**
 * Simple Java model class.
 * This class is required by {@link com.google.firebase.database.DataSnapshot#getValue(Class)}.
 */
public class QuestionnaireDao {

    private String id;
    private String name;
    private String number;
    private String settlement;
    private int progress;
    private long lastEditTime;
    private List<BlockDao> blocks;

    public QuestionnaireDao(String id, String name, String number, String settlement, int progress,
                            long lastEditTime, List<BlockDao> blocks) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.settlement = settlement;
        this.progress = progress;
        this.lastEditTime = lastEditTime;
        this.blocks = blocks;
    }

    /**
     * Empty constructor required for calls to {@link com.google.firebase.database.DataSnapshot#getValue(Class)}
     */
    public QuestionnaireDao() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getSettlement() {
        return settlement;
    }

    public int getProgress() {
        return progress;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public List<BlockDao> getBlocks() {
        return blocks;
    }
}