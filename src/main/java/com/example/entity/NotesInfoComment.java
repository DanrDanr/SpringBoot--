package com.example.entity;

import javax.persistence.*;

/**
 * 评论笔记信息
 */
@Table(name = "comment_notes_info")
public class NotesInfoComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "time")
    private String time;
    @Column(name = "name")
    private String name;
    @Column(name = "foreignId")
    private Long foreignId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }
}
