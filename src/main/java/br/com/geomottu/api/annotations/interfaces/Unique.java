package br.com.geomottu.api.annotations.interfaces;

import br.com.geomottu.api.annotations.object.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {
    String message() default "Valor já existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String fieldName();       // Campo a ser validado
    Class<?> domainClass();   // Classe da entidade
}
