# AGENTS.md - BELEX Codebase Guide

## Project Overview
**BELEX** is a Java-based web application for managing supply chain operations (products, suppliers, customers, entries, preparation, allocation). Built on **Spring MVC + Spring WebFlow + Hibernate 5.6** (Java 17), packaged as WAR, deployed to servlet containers.

## Architecture & Key Components

### Core Layers
- **Presentation**: JSP pages in `/src/main/webApp/` + Spring WebFlow flows in `/WEB-INF/flows/` (e.g., `v2-productEntry.xml`)
- **Business Logic**: Service classes in `/src/main/java/org/belex/` (modules: `arrival`, `entry`, `product`, `supplier`, `customer`, `allocation`, etc.)
- **Data Access**: DAO classes in `/src/main/java/db/` (e.g., `ProductDAO`, `SupplierDAO`) + Hibernate entity mappings (`.hbm.xml` files in `/src/main/resources/db/`)

### Critical Files
- `/pom.xml` - Maven configuration (Java 17, Spring 5.3.34, Hibernate 5.6.15, c3p0 connection pooling)
- `/src/main/webApp/WEB-INF/web.xml` - Servlet mappings: `/flow/*` → Spring DispatcherServlet, `InitializerServlet` on startup
- `/src/main/webApp/WEB-INF/flow-servlet.xml` - Spring bean definitions, WebFlow registry, component scanning on `org.belex` and `db`
- `/src/main/resources/hibernate.cfg.xml` - Hibernate mapping declarations (21 mapped entity classes), SQLServer dialect default

### Data Access Pattern
1. **DAO layer** uses constructor-injected `SessionFactory` from Spring
2. Methods wrapped in `@Transactional` (read-only for queries)
3. Use `currentSession()` → `createQuery()` with HQL (not native SQL)
4. Try-catch blocks return `null` or empty `List.of()` on exception; log via `@Slf4j`
5. **No annotations on entities** - they're mapped via `.hbm.xml` files (Hibernate 3 DTD format)

Example pattern (from `ProductDAO`):
```java
@Repository @Slf4j
public class ProductDAO implements IProductDAO {
  private final SessionFactory sessionFactory;
  public ProductDAO(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }
  protected Session currentSession() { return sessionFactory.getCurrentSession(); }
  
  @Transactional(readOnly = true)
  public Product getProductByCode(String code, boolean logError) {
    try {
      return currentSession()
        .createQuery("from Product p where p.productCode = :code", Product.class)
        .setParameter("code", code)
        .uniqueResult();
    } catch (Exception e) {
      if (logError) log.error("Error while finding product by code: " + code, e);
      return null;
    }
  }
}
```

## Build & Deployment

### Maven Build (Primary)
```bash
mvn clean package  # Creates target/belex.war
```

### Legacy Ant Build (Reference)
`build/build.xml` is legacy; Maven is current standard. Ant references old package structure.

### Key Build Properties
- **Java version**: 17 (compiler target)
- **Encoding**: UTF-8
- **WAR output**: `target/belex.war`
- **InitializerServlet** cleans `/jsptmp` directory on app startup

## Code Conventions & Patterns

### Package Structure
- `org.belex.*` - Business logic modules (arrival, entry, product, supplier, customer, allocation, fly, cleaning, traceability, export, requestparams, util, application)
- `db.*` - Data access objects (DAOs) and Hibernate-mapped entity classes
- Module packages are **split**: DAOs in `db/<module>/`, business classes often in `org.belex/<module>/`

### Naming & Entity Patterns
- **DAOs**: `<Entity>DAO` + `I<Entity>DAO` interface (e.g., `ProductDAO implements IProductDAO`)
- **Entities**: Mapped via `.hbm.xml` (e.g., `db/product/Product.java` maps from `db/product/product.hbm.xml`)
- **Field names in entities**: CamelCase in Java, match DB column names defined in `.hbm.xml`
- Use **Lombok** (`@Slf4j`, `@Getter`, `@Setter`, etc.) for boilerplate

### Query Patterns
- HQL (not JPQL) - references mapped entity names, not @Entity classes
- Multi-entity joins use comma syntax: `FROM Conditioning c, Product p, EditionType t ...` (legacy style)
- Parameterized queries via `.setParameter(name, value)` (avoid string concatenation)
- Text search uses `upper()` + `LIKE` (e.g., `upper(p.description) LIKE :descr`)

## Important Integration Points

### Spring WebFlow Integration
- Flows defined in `/WEB-INF/flows/*.xml` (currently: `test-flow.xml`, `v2-productEntry.xml`)
- Flow IDs registered in `flow-servlet.xml` `<flow:flow-location>` elements
- URL pattern: `/flow/{flowId}` maps to WebFlow DispatcherServlet
- Views resolve to `/WEB-INF/jsp/*.jsp` (configured in `flow-servlet.xml` ViewResolver)

### Database Connection
- **JDBC Driver**: SQLServer (ojdbc11 for Oracle also available)
- **Connection Pool**: c3p0 (min 5, max 100 connections via `hibernate.cfg.xml`)
- **Dialect**: `SQLServerDialect` (can switch to custom `MSAccessDialect` in `/src/main/java/dialect/`)
- **OSIV Filter**: `OpenSessionInViewFilter` enabled for all requests (lazy-loading in views)

### Logging
- **SLF4J** over Log4j via `slf4j-log4j12` bridge
- Config: `/src/main/resources/log4j.xml`
- Use `@Slf4j` on DAO/service classes, then `log.debug()`, `log.error()` with error objects

## Workflow: Adding a New Feature

1. **Entity**: Create `.hbm.xml` mapping in `/src/main/resources/db/<module>/`, create Java POJO in `/src/main/java/db/<module>/`
2. **DAO**: Create `<Entity>DAO.java` + interface in `/src/main/java/db/<module>/`, inject `SessionFactory`
3. **Service**: Create business logic class in `/src/main/java/org/belex/<module>/` if needed, inject DAO via constructor
4. **WebFlow**: Define flow `.xml` in `/WEB-INF/flows/`, register in `flow-servlet.xml`, create corresponding JSP views in `/WEB-INF/jsp/`
5. **Build**: `mvn clean package` → test `target/belex.war`

## Common Pitfalls

- **Parameter name mismatch**: `.setParameter("supplier", supplierCode)` used but HQL references `:supplierCode` → wrong variable bound
- **Null handling**: DAOs return `null` instead of Optional/empty list; callers must null-check
- **HQL vs SQL**: Must use entity class names and field names from `.hbm.xml`, not DB table/column names
- **Transaction scope**: `@Transactional(readOnly = true)` for queries; omit `readOnly` for writes (persist/merge/remove)
- **c3p0 pool exhaustion**: Monitor connection usage in high concurrency; adjust `min_size`/`max_size` in `hibernate.cfg.xml`

## Testing & Debugging

- **No unit test framework** detected in pom.xml (add JUnit 5 + Mockito if tests needed)
- **Debug JSP**: Check `/WEB-INF/jsptmp/` for compiled JSP classes; InitializerServlet clears this on startup
- **Hibernate SQL logging**: Set `show_sql=true` in `hibernate.cfg.xml` to debug query generation
- **Spring context**: Verify bean scan includes `org.belex` and `db` packages in `flow-servlet.xml`

## References
- Hibernate 5.6 Docs: Entity mappings, HQL query language
- Spring 5.3 Docs: WebFlow, OSIV pattern, transaction management
- Project structure mirrors traditional layered architecture (presentation → service → DAO → database)

