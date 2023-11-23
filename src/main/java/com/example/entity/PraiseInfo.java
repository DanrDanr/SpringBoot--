package com.example.entity;

import javax.persistence.*;

/**
 * 点赞信息
 */
@Table(name = "praise_info")
public class PraiseInfo  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "name")
	private String name;
	@Column(name = "time")
	private String time;
	@Column(name = "notesId")
	private Long notesId;
	@Column(name = "foodsId")
	private Long foodsId;
	@Column(name = "userId")
	private Long userId;
	private Integer level;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getNotesId() {
		return notesId;
	}
	public void setNotesId(Long notesId) {
		this.notesId = notesId;
	}


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }

	public Long getFoodsId() {
		return foodsId;
	}

	public void setFoodsId(Long foodsId) {
		this.foodsId = foodsId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
