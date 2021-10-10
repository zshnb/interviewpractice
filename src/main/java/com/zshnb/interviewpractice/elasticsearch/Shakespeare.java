package com.zshnb.interviewpractice.elasticsearch;

import javax.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "shakespeare", createIndex = false)
public class Shakespeare {
    @Id
    private Integer id;
    @Field(name = "line_id")
    private long lineId;
    @Field(name = "line_number", type = FieldType.Text)
    private String lineNumber;
    @Field(name = "play_name", type = FieldType.Text)
    private String playName;
    @Field(name = "speaker", type = FieldType.Text)
    private String speaker;
    @Field(name = "speech_number")
    private long speechNumber;
    @Field(name = "text_entry", type = FieldType.Text)
    private String textEntry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public long getSpeechNumber() {
        return speechNumber;
    }

    public void setSpeechNumber(long speechNumber) {
        this.speechNumber = speechNumber;
    }

    public String getTextEntry() {
        return textEntry;
    }

    public void setTextEntry(String textEntry) {
        this.textEntry = textEntry;
    }
}
