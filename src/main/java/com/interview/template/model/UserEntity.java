package com.interview.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
	@SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
	private Long id;

	@Size(max = 40)
	@NotEmpty(message = "username cannot be null or empty")
	@Column(name = "username")
	private String username;

	@Size(max = 80)
	@NotEmpty(message = "email cannot be null or empty")
	@Column(name = "email")
	@Email(message = "email address format is not valid")
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 255)
	@NotEmpty(message = "password cannot be null or empty")
	@Column(name = "password")
	private String password;
}
