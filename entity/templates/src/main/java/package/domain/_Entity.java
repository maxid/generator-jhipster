package <%=packageName%>.domain;
<% if (databaseType == 'cassandra') { %>
import com.datastax.driver.mapping.annotations.*;<% } %><% if (relationships.length > 0  && (fieldsContainOwnerManyToMany == false || fieldsContainOneToMany == true)) { %>
import com.fasterxml.jackson.annotation.JsonIgnore;<% } %><% if (fieldsContainCustomTime == true) { %>
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;<% } %><% if (fieldsContainLocalDate == true) { %>
import <%=packageName%>.domain.util.CustomLocalDateSerializer;
import <%=packageName%>.domain.util.ISO8601LocalDateDeserializer;<% } %><% if (fieldsContainDateTime == true) { %>
import <%=packageName%>.domain.util.CustomDateTimeDeserializer;
import <%=packageName%>.domain.util.CustomDateTimeSerializer;<% } %><% if (hibernateCache != 'no') { %>
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;<% } %><% if (fieldsContainCustomTime == true && databaseType == 'sql') { %>
import org.hibernate.annotations.Type;<% } %><% if (fieldsContainLocalDate == true) { %>
import org.joda.time.LocalDate;<% } %><% if (fieldsContainDateTime == true) { %>
import org.joda.time.DateTime;<% } %><% if (databaseType == 'mongodb') { %>
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;<% } %>
<% if (databaseType == 'sql') { %>
import javax.persistence.*;<% } %><% if (validation) { %>
import javax.validation.constraints.*;<% } %>
import java.io.Serializable;<% if (fieldsContainBigDecimal == true) { %>
import java.math.BigDecimal;<% } %><% if (fieldsContainDate == true) { %>
import java.util.Date;<% } %><% if (relationships.length > 0) { %>
import java.util.HashSet;
import java.util.Set;<% } %>
import java.util.Objects;<% if (databaseType == 'cassandra') { %>
import java.util.UUID;<% } %>

/**
 * A <%= entityClass %>.
 */<% if (databaseType == 'sql') { %>
@Entity
@Table(name = "T_<%= name.toUpperCase() %>")<% if (hibernateCache != 'no') { %>
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %><% } %><% if (databaseType == 'mongodb') { %>
@Document(collection = "T_<%= name.toUpperCase() %>")<% } %><% if (databaseType == 'cassandra') { %>
@Table(name = "<%= entityInstance %>")<% } %>
public class <%= entityClass %> implements Serializable {
<% if (databaseType == 'sql') { %>
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;<% } %><% if (databaseType == 'mongodb') { %>
    @Id
    private String id;<% } %><% if (databaseType == 'cassandra') { %>
    @PartitionKey
    private UUID id;<% } %>
<% for (fieldId in fields) { %><% if (fields[fieldId].fieldValidate == true) {
    if (fields[fieldId].fieldValidateRules.indexOf('required') != -1) { %>
    @NotNull<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('minlength') != -1 && fields[fieldId].fieldValidateRules.indexOf('maxlength') == -1) { %>
    @Size(min = <%= fields[fieldId].fieldValidateRulesMinlength %>)<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('maxlength') != -1 && fields[fieldId].fieldValidateRules.indexOf('minlength') == -1) { %>
    @Size(max = <%= fields[fieldId].fieldValidateRulesMaxlength %>)<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('minlength') != -1 && fields[fieldId].fieldValidateRules.indexOf('maxlength') != -1) { %>
    @Size(min = <%= fields[fieldId].fieldValidateRulesMinlength %>, max = <%= fields[fieldId].fieldValidateRulesMaxlength %>)<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('min') != -1) { %>
    @Min(value = <%= fields[fieldId].fieldValidateRulesMin %>)<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('max') != -1) { %>
    @Max(value = <%= fields[fieldId].fieldValidateRulesMax %>)<% } %><% if (fields[fieldId].fieldValidateRules.indexOf('pattern') != -1) { %>
    @Pattern(regexp = "<%= fields[fieldId].fieldValidateRulesPattern %>")<% } } %><% if (databaseType == 'sql') { %><% if (fields[fieldId].fieldType == 'DateTime') { %>
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "<%=fields[fieldId].fieldNameUnderscored %>", nullable = false)<% } else if (fields[fieldId].fieldType == 'LocalDate') { %>
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "<%=fields[fieldId].fieldNameUnderscored %>", nullable = false)<% } else if (fields[fieldId].fieldType == 'BigDecimal') { %>
    @Column(name = "<%=fields[fieldId].fieldNameUnderscored %>", precision=10, scale=2)<% } else { %>
    @Column(name = "<%=fields[fieldId].fieldNameUnderscored %>"<% if (fields[fieldId].fieldValidate == true) { %><% if (fields[fieldId].fieldValidateRules.indexOf('maxlength') != -1) { %>, length = <%= fields[fieldId].fieldValidateRulesMaxlength %><% } %><%if (fields[fieldId].fieldValidateRules.indexOf('required') != -1) { %>, nullable = false<% } %><% } %>)<% } } %><% if (databaseType == 'mongodb') { %><% if (fields[fieldId].fieldType == 'DateTime') { %>
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)<% } else if (fields[fieldId].fieldType == 'LocalDate') { %>
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)<% } %>
    @Field("<%=fields[fieldId].fieldNameUnderscored %>")<% } %>
    private <%= fields[fieldId].fieldType %> <%= fields[fieldId].fieldName %>;
<% } %><% for (relationshipId in relationships) { %><% if (relationships[relationshipId].relationshipType == 'one-to-many') { %>
    @OneToMany(mappedBy = "<%= entityInstance %>")
    @JsonIgnore<% if (hibernateCache != 'no') { %>
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %>
    private Set<<%= relationships[relationshipId].otherEntityNameCapitalized %>> <%= relationships[relationshipId].relationshipFieldName %>s = new HashSet<>();<% } else if (relationships[relationshipId].relationshipType == 'many-to-one') { %>
    @ManyToOne
    private <%= relationships[relationshipId].otherEntityNameCapitalized %> <%= relationships[relationshipId].relationshipFieldName %>;<% } else if (relationships[relationshipId].relationshipType == 'many-to-many') { %>
    @ManyToMany<% if (relationships[relationshipId].ownerSide == false) { %>(mappedBy = "<%= entityInstance %>s")
    @JsonIgnore<% } %><% if (hibernateCache != 'no') { %>
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %><% if (relationships[relationshipId].ownerSide == true) { %>
    @JoinTable(name = "T_<%= name.toUpperCase() + '_' + relationships[relationshipId].relationshipName.toUpperCase() %>",
               joinColumns = @JoinColumn(name="<%= name %>s_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="<%= relationships[relationshipId].relationshipName %>s_id", referencedColumnName="ID"))<% } %>
    private Set<<%= relationships[relationshipId].otherEntityNameCapitalized %>> <%= relationships[relationshipId].relationshipFieldName %>s = new HashSet<>();<% } else { %>
    @OneToOne<% if (relationships[relationshipId].ownerSide == false) { %>(mappedBy = "<%= entityInstance %>")<% } %>
    private <%= relationships[relationshipId].otherEntityNameCapitalized %> <%= relationships[relationshipId].relationshipFieldName %>;<% } %>
<% } %>
    public <% if (databaseType == 'sql') { %>Long<% } %><% if (databaseType == 'mongodb') { %>String<% } %><% if (databaseType == 'cassandra') { %>UUID<% } %> getId() {
        return id;
    }

    public void setId(<% if (databaseType == 'sql') { %>Long<% } %><% if (databaseType == 'mongodb') { %>String<% } %><% if (databaseType == 'cassandra') { %>UUID<% } %> id) {
        this.id = id;
    }
<% for (fieldId in fields) { %>
    public <%= fields[fieldId].fieldType %> get<%= fields[fieldId].fieldNameCapitalized %>() {
        return <%= fields[fieldId].fieldName %>;
    }

    public void set<%= fields[fieldId].fieldNameCapitalized %>(<%= fields[fieldId].fieldType %> <%= fields[fieldId].fieldName %>) {
        this.<%= fields[fieldId].fieldName %> = <%= fields[fieldId].fieldName %>;
    }
<% } %><% for (relationshipId in relationships) { %><% if (relationships[relationshipId].relationshipType == 'one-to-many' || relationships[relationshipId].relationshipType == 'many-to-many') { %>
    public Set<<%= relationships[relationshipId].otherEntityNameCapitalized %>> get<%= relationships[relationshipId].relationshipNameCapitalized %>s() {
        return <%= relationships[relationshipId].relationshipFieldName %>s;
    }

    public void set<%= relationships[relationshipId].relationshipNameCapitalized %>s(Set<<%= relationships[relationshipId].otherEntityNameCapitalized %>> <%= relationships[relationshipId].otherEntityName %>s) {
        this.<%= relationships[relationshipId].relationshipFieldName %>s = <%= relationships[relationshipId].otherEntityName %>s;
    }<% } else { %>
    public <%= relationships[relationshipId].otherEntityNameCapitalized %> get<%= relationships[relationshipId].relationshipNameCapitalized %>() {
        return <%= relationships[relationshipId].relationshipFieldName %>;
    }

    public void set<%= relationships[relationshipId].relationshipNameCapitalized %>(<%= relationships[relationshipId].otherEntityNameCapitalized %> <%= relationships[relationshipId].otherEntityName %>) {
        this.<%= relationships[relationshipId].relationshipFieldName %> = <%= relationships[relationshipId].otherEntityName %>;
    }<% } %>
<% } %>
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        <%= entityClass %> <%= entityInstance %> = (<%= entityClass %>) o;

        if ( ! Objects.equals(id, <%= entityInstance %>.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "<%= entityClass %>{" +
                "id=" + id +<% for (fieldId in fields) { %>
                ", <%= fields[fieldId].fieldName %>='" + <%= fields[fieldId].fieldName %> + "'" +<% } %>
                '}';
    }
}
