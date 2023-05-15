/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bjm.bc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STATE")
public class State {
	
	@Id
	@Column(name="CODE")
	private String code;
	@Column(name="NAME")
	private String name;
	@Column(name="POST_CODE_PREFIX")
	private String postCodePrefix;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostCodePrefix() {
		return postCodePrefix;
	}
	public void setPostCodePrefix(String postCodePrefix) {
		this.postCodePrefix = postCodePrefix;
	}
	
}
