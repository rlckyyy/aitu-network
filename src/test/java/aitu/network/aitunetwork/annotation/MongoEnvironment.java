package aitu.network.aitunetwork.annotation;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestConstructor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@TestConstructor(autowireMode = ALL)
@DataMongoTest
public @interface MongoEnvironment {
}
