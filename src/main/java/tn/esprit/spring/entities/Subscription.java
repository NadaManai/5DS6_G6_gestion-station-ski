package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Subscription implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numSub;
	@NotNull(message = "start date cannot be null")
	LocalDate startDate;
	@NotNull(message = "End date cannot be null")
	LocalDate endDate;
	@NotNull(message = "price date cannot be null")
	Float price;
//	@Enumerated(EnumType.STRING)
	TypeSubscription typeSub;

}
