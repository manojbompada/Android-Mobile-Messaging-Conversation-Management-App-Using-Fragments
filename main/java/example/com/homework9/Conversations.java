/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    Conversations.java
 */

package example.com.homework9;

/**
 * Created by Manoj on 4/26/2016.
 */
public class Conversations {

    String deletedBy="",participant1="",participant2="";
     Boolean isArchived_by_participant1 = false;
     Boolean isArchived_by_participant2 = false;
    private String key;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Conversations(){

    }
    public Conversations(String deletedBy, String participant1, String participant2, Boolean isArchived_by_participant1, Boolean isArchived_by_participant2) {
        this.deletedBy = deletedBy;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.isArchived_by_participant1 = isArchived_by_participant1;
        this.isArchived_by_participant2 = isArchived_by_participant2;
    }

    @Override
    public String toString() {
        return "Conversations{" +
                "deletedBy='" + deletedBy + '\'' +
                ", participant1='" + participant1 + '\'' +
                ", participant2='" + participant2 + '\'' +
                ", isArchived_by_participant1=" + isArchived_by_participant1 +
                ", isArchived_by_participant2=" + isArchived_by_participant2 +
                '}';
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getParticipant1() {
        return participant1;
    }

    public void setParticipant1(String participant1) {
        this.participant1 = participant1;
    }

    public String getParticipant2() {
        return participant2;
    }

    public void setParticipant2(String participant2) {
        this.participant2 = participant2;
    }

    public Boolean getIsArchived_by_participant1() {
        return isArchived_by_participant1;
    }

    public void setIsArchived_by_participant1(Boolean isArchived_by_participant1) {
        this.isArchived_by_participant1 = isArchived_by_participant1;
    }

    public Boolean getIsArchived_by_participant2() {
        return isArchived_by_participant2;
    }

    public void setIsArchived_by_participant2(Boolean isArchived_by_participant2) {
        this.isArchived_by_participant2 = isArchived_by_participant2;
    }
}
