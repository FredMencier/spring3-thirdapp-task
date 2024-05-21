package ch.heg.task.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    Long id;

    @Column(name = "description")
    String description;

    @Column(name = "date_execution")
    Date dateExecution;

    @Column(name = "date_creation")
    Date dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    Statut statut;

    @Column(name = "email_responsable")
    String emailResponsable;

    @ManyToOne
    @JoinColumn(name="person_id")
    Person person;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "audit_id")
    Set<Audit> auditList = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Date dateExecution) {
        this.dateExecution = dateExecution;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getEmailResponsable() {
        return emailResponsable;
    }

    public void setEmailResponsable(String emailResponsable) {
        this.emailResponsable = emailResponsable;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Audit> getAuditList() {
        return auditList;
    }

    public void setAuditList(Set<Audit> auditList) {
        this.auditList = auditList;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateExecution=" + dateExecution +
                ", dateCreation=" + dateCreation +
                ", statut=" + statut +
                ", emailResponsable='" + emailResponsable + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!Objects.equals(id, task.id)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (!Objects.equals(dateExecution, task.dateExecution))
            return false;
        if (!Objects.equals(dateCreation, task.dateCreation)) return false;
        if (statut != task.statut) return false;
        return Objects.equals(emailResponsable, task.emailResponsable);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateExecution != null ? dateExecution.hashCode() : 0);
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
        result = 31 * result + (statut != null ? statut.hashCode() : 0);
        result = 31 * result + (emailResponsable != null ? emailResponsable.hashCode() : 0);
        return result;
    }
}
