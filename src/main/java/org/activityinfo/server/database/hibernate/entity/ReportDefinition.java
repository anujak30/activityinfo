/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.activityinfo.shared.report.model.EmailDelivery;

/**
 * Defines a Report and its subscriptions.
 *
 * @see org.activityinfo.shared.report.model.Report
 *
 */
@Entity
@Table(name="ReportTemplate")
@org.hibernate.annotations.Filters(
		{@org.hibernate.annotations.Filter(
				name="userVisible",
				condition="(:currentUserId = OwnerUserId or " + 
						  "(Visibility = 1 and (DatabaseId is null or " +
						  ":currentUserId in (select p.UserId from userpermission p " +
						  						"where p.AllowView and p.UserId=:currentUserId and p.DatabaseId=DatabaseId))))"
		),
		
		@org.hibernate.annotations.Filter(
			name="hideDeleted",
			condition="DateDeleted is null"
		)}
)
public class ReportDefinition implements Serializable {

	private int id;
	private User owner;
	private UserDatabase database;
	private int visibility;
	private String xml;
    private Date dateDeleted;
    private String title;
    private String description;
    private EmailDelivery frequency;
    private Integer day;
    private Set<ReportSubscription> subscriptions = new HashSet<ReportSubscription>(0);
    private String json;
    
	public ReportDefinition(){
		
	}

    @Id
	@Column(name="ReportTemplateId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OwnerUserId", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DatabaseId", nullable = true, updatable = false)
	public UserDatabase getDatabase() {
		return database;
	}

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

    @Lob
	@Column(nullable=false)
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

    @OneToMany(mappedBy = "template", fetch = FetchType.LAZY)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public Set<ReportSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<ReportSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Column(nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return dateDeleted;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}
	
	@Lob
	@Column(nullable=true)
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
