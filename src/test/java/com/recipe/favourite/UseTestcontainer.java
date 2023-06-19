package com.recipe.favourite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@ExtendWith(TestcontainerExtension.class)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
public @interface UseTestcontainer {
}
