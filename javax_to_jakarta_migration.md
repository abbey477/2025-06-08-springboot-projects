# javax.* to jakarta.* Maven Dependencies Migration Guide

## Complete javax.* to jakarta.* Migration Table

| **API Type** | **OLD (javax.*)** | **NEW (jakarta.*)** | **Import Changes** | **Notes** |
|--------------|-------------------|---------------------|-------------------|-----------|
| **JMS** | `javax.jms:jms-api:2.0` | `jakarta.jms:jakarta.jms-api:3.1.0` | `javax.jms.*` → `jakarta.jms.*` | Your current issue |
| **Servlet** | `javax.servlet:javax.servlet-api:4.0.1` | `jakarta.servlet:jakarta.servlet-api:6.0.0` | `javax.servlet.*` → `jakarta.servlet.*` | Web applications |
| **JPA/Persistence** | `javax.persistence:persistence-api:2.2` | `jakarta.persistence:jakarta.persistence-api:3.1.0` | `javax.persistence.*` → `jakarta.persistence.*` | Database ORM |
| **Bean Validation** | `javax.validation:validation-api:2.0.1.Final` | `jakarta.validation:jakarta.validation-api:3.0.2` | `javax.validation.*` → `jakarta.validation.*` | Bean validation |
| **Common Annotations** | `javax.annotation:javax.annotation-api:1.3.2` | `jakarta.annotation:jakarta.annotation-api:2.1.1` | `javax.annotation.*` → `jakarta.annotation.*` | @PostConstruct, etc. |
| **JTA Transactions** | `javax.transaction:jta:1.1` | `jakarta.transaction:jakarta.transaction-api:2.0.1` | `javax.transaction.*` → `jakarta.transaction.*` | JTA transactions |
| **Expression Language** | `javax.el:javax.el-api:3.0.0` | `jakarta.el:jakarta.el-api:5.0.0` | `javax.el.*` → `jakarta.el.*` | JSP/JSF expressions |
| **WebSocket** | `javax.websocket:javax.websocket-api:1.1` | `jakarta.websocket:jakarta.websocket-api:2.1.0` | `javax.websocket.*` → `jakarta.websocket.*` | WebSocket support |
| **Mail** | `javax.mail:mail:1.4.7` | `jakarta.mail:jakarta.mail-api:2.1.0` | `javax.mail.*` → `jakarta.mail.*` | Email functionality |
| **JAXB (XML Binding)** | `javax.xml.bind:jaxb-api:2.3.1` | `jakarta.xml.bind:jakarta.xml.bind-api:4.0.0` | `javax.xml.bind.*` → `jakarta.xml.bind.*` | XML binding |
| **JAX-WS (Web Services)** | `javax.xml.ws:jaxws-api:2.3.1` | `jakarta.xml.ws:jakarta.xml.ws-api:4.0.0` | `javax.xml.ws.*` → `jakarta.xml.ws.*` | SOAP web services |
| **SOAP** | `javax.xml.soap:javax.xml.soap-api:1.4.0` | `jakarta.xml.soap:jakarta.xml.soap-api:3.0.0` | `javax.xml.soap.*` → `jakarta.xml.soap.*` | SOAP messaging |
| **JAX-RS (REST)** | `javax.ws.rs:jax-rs-api:2.1.1` | `jakarta.ws.rs:jakarta.ws.rs-api:3.1.0` | `javax.ws.rs.*` → `jakarta.ws.rs.*` | RESTful web services |
| **Activation** | `javax.activation:activation:1.1.1` | `jakarta.activation:jakarta.activation-api:2.1.0` | `javax.activation.*` → `jakarta.activation.*` | Required by JAX-WS |

## Implementation Dependencies (Runtime)

| **Technology** | **OLD Implementation** | **NEW Implementation** | **Notes** |
|----------------|------------------------|------------------------|-----------|
| **JAXB Runtime** | `com.sun.xml.bind:jaxb-impl:2.3.1` | `org.glassfish.jaxb:jaxb-runtime:4.0.3` | Implementation changed |
| **JAX-WS Runtime** | `com.sun.xml.ws:jaxws-rt:2.3.1` | `com.sun.xml.ws:jaxws-rt:4.0.0` | Same groupId, new version |
| **Hibernate** | `org.hibernate:hibernate-core:5.x` | `org.hibernate.orm:hibernate-core:6.4.0` | New groupId for v6+ |

## Maven Configuration Examples

### Your Specific JMS Fix

**REMOVE:**
```xml
<dependency>
    <groupId>javax.jms</groupId>
    <artifactId>jms-api</artifactId>
    <version>2.0</version>
</dependency>
```

**ADD:**
```xml
<dependency>
    <groupId>jakarta.jms</groupId>
    <artifactId>jakarta.jms-api</artifactId>
    <version>3.1.0</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jms</artifactId>
    <version>6.2.8</version>
</dependency>
```

### Common Migration Patterns

**Web Applications:**
```xml
<!-- OLD -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
</dependency>

<!-- NEW -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
</dependency>
```

**JPA/Hibernate:**
```xml
<!-- OLD -->
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>persistence-api</artifactId>
    <version>2.2</version>
</dependency>

<!-- NEW -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

**Bean Validation:**
```xml
<!-- OLD -->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>

<!-- NEW -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
```

## Spring Boot Starters (Recommended)

If using Spring Boot 3.x, prefer these starters which handle Jakarta dependencies automatically:

| **Functionality** | **Spring Boot Starter** |
|-------------------|-------------------------|
| **JMS** | `spring-boot-starter-activemq` or `spring-boot-starter-artemis` |
| **Web** | `spring-boot-starter-web` |
| **JPA** | `spring-boot-starter-data-jpa` |
| **Validation** | `spring-boot-starter-validation` |
| **Mail** | `spring-boot-starter-mail` |

## Migration Checklist

- [ ] Remove ALL `javax.*` JEE API dependencies
- [ ] Add corresponding `jakarta.*` versions
- [ ] Update import statements in Java code from `javax.*` to `jakarta.*`
- [ ] Verify Spring Boot version is 3.x+ for Spring Framework 6.x
- [ ] Test thoroughly - runtime errors like yours are common during migration
- [ ] Check third-party libraries for Jakarta EE compatibility

## Version Compatibility Matrix

| **Spring Framework** | **Spring Boot** | **JEE Standard** | **Namespace** |
|---------------------|-----------------|------------------|---------------|
| 5.x | 2.x | Java EE 8 | `javax.*` |
| 6.x | 3.x | Jakarta EE 9+ | `jakarta.*` |

## Warning Signs of Mixed Dependencies

- `NoClassDefFoundError` for `jakarta.*` classes (your current issue)
- `ClassNotFoundException` at runtime
- Multiple versions of similar APIs on classpath
- Compilation errors after Spring upgrade

**Bottom Line:** With Spring 6.2.8, you MUST use Jakarta EE APIs exclusively. No mixing allowed!