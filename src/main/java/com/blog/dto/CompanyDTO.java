/**
 * 
 */
package com.blog.dto;

/**
 * @author reyos
 *
 */
public class CompanyDTO {
	private String id;
	private String companyName;

	public CompanyDTO() {
	}

	public CompanyDTO(String id, String companyName) {
		this.id = id;
		this.companyName = companyName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
