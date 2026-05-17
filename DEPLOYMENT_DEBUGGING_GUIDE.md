# 🚀 Guide de Déploiement & Debugging - productEntry.xml

## 1. Build & Packaging

### Clean Build
```bash
cd C:\Users\miche\Downloads\temp belex\belexGit\NewEntry

# Compilation + Packaging
mvn clean package -DskipTests

# Résultat
# Output: target/belex.war
```

### Build sans packaging (pour tests unitaires)
```bash
mvn clean compile -DskipTests
```

### Rebuild rapide (sans clean)
```bash
mvn install -DskipTests
```

---

## 2. Déploiement du WAR

### Option 1 : Tomcat (local)

#### Copier le WAR
```powershell
# Copier belex.war vers répertoire Tomcat
Copy-Item -Path "target\belex.war" -Destination "C:\Users\miche\Downloads\apache-tomcat-9.0.115\webapps\" -Force
```

#### Démarrer Tomcat
```powershell
# Windows
C:\Users\miche\Downloads\apache-tomcat-9.0.115\bin\catalina.bat run

# Ou en tant que service
net start Tomcat9
```

#### Accéder à l'application
```
http://localhost:8080/belex/flow/arrival
```

### Option 2 : Déploiement Maven (si serveur distant configuré)
```bash
mvn deploy

# Ou via plugin Tomcat
mvn tomcat7:deploy

# Stop/Start
mvn tomcat7:stop
mvn tomcat7:start
```

### Option 3 : Via IDE (IntelliJ IDEA)
```
1. Run → Edit Configurations
2. Tomcat Server → Add New Configuration
3. Déployer artifact : belex:war exploded
4. Clic "Run"
```

---

## 3. Vérification du déploiement

### Logs Tomcat
```bash
# Windows
type C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\catalina.log

# Ou en temps réel
Get-Content -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\catalina.log" -Wait
```

### Vérifier que le flux est chargé
```
[INFO] Loading WebFlow configuration...
[INFO] Registering flow 'arrival' with path '/WEB-INF/flows/v2-productEntry.xml'
```

### Accès direct
```
curl http://localhost:8080/belex/flow/arrival
```

---

## 4. Debugging Spring WebFlow

### Activer les logs DEBUG

#### src/main/resources/log4j.xml
```xml
<logger name="org.springframework.webflow" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>

<logger name="org.springframework.binding" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>

<logger name="db" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>

<logger name="org.belex" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>
```

### Relancer l'application
```bash
mvn clean install -DskipTests
# Redéployer le WAR
```

### Logs attendus
```
[DEBUG] org.springframework.webflow.executor.FlowExecutorImpl - Starting new flow execution
[DEBUG] org.springframework.webflow.engine.FlowImpl - Starting flow 'arrival'
[DEBUG] org.springframework.webflow.engine.support.StateImpl - Entering state 'searchPlannedSuppliersAct'
[DEBUG] org.springframework.webflow.engine.support.ActionStateImpl - Executing action in state 'searchPlannedSuppliersAct'
```

### Debugging Hibernate
#### src/main/resources/hibernate.cfg.xml
```xml
<!-- Avant -->
<property name="show_sql">false</property>

<!-- Après (debugging) -->
<property name="show_sql">true</property>
```

Logs Hibernate :
```
[DEBUG] select ... from arrival where supplier_code = ?
[DEBUG] insert into supplier_entry (id, ...) values (?)
```

---

## 5. Tester une transition spécifique

### Cas d'usage : Tester checkPendingArrivalAct

#### Via navigateur
```
1. Accéder à http://localhost:8080/belex/flow/arrival
2. Observer le formulaire affiché
3. Cliquer bouton "showSuppliers"
4. Observer transition vers checkPendingArrivalAct
5. Vérifier les logs DEBUG
```

#### Via curl (si API REST disponible)
```powershell
$flowExecutionKey = "e1s2" # À extraire du formulaire

curl -X POST "http://localhost:8080/belex/flow/arrival?_flowExecutionKey=$flowExecutionKey&_eventId=showSuppliers" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "arrivalDate=2026-04-13"
```

---

## 6. Debugging les expressions SpEL

### Vérifier une expression
```java
// Dans votre IDE, évaluer directement l'expression
// Ou utiliser la console Groovy

arrivalBusiness.getPlannedSuppliers(flowScope.arrival)
// Doit retourner : Arrival object
```

### Logs avec expressions
```xml
<!-- Ajouter un log dans la transition -->
<transition to="selectArrivalDateView">
    <evaluate expression="T(org.slf4j.LoggerFactory).getLogger('DEBUG').info('Arrival: ' + flowScope.arrival)" />
</transition>
```

---

## 7. Inspecter le flowScope

### Méthode 1 : Ajouter un breakpoint JSP
```jsp
<!-- Dans productEntry/selectArrivalDate.jsp -->
<%@ page import="org.springframework.webflow.execution.RequestContext" %>
<%
    RequestContext ctx = (RequestContext) pageContext.getAttribute("flowRequestContext");
    Object arrival = ctx.getFlowScope().get("arrival");
    System.out.println("DEBUG: arrival = " + arrival);
%>
```

### Méthode 2 : Log dans la transition
```xml
<on-start>
    <set name="flowScope.arrival" value="new org.belex.arrival.Arrival()" />
    <evaluate expression="T(java.lang.System).out.println('Arrival initialized: ' + flowScope.arrival)" />
</on-start>
```

### Méthode 3 : Breakpoint dans la classe métier
```java
@Transactional
public Arrival getPlannedSuppliers(Arrival arrival) {
    log.debug("getPlannedSuppliers called with: " + arrival); // ← Breakpoint ici
    // ...
    return arrival;
}
```

---

## 8. Erreurs courantes et solutions

### Erreur 1 : `NullPointerException` sur flowRequestContext

**Symptôme** :
```
java.lang.NullPointerException: flowRequestContext is null
    at org.belex.arrival.ArrivalFormAction.bindAndValidate(ArrivalFormAction.java:45)
```

**Solution** :
```xml
<!-- Avant -->
<evaluate expression="arrivalFormAction.bindAndValidate(flowRequestContext)" />

<!-- Si flowRequestContext n'existe pas, utiliser un paramètre sans -->
<evaluate expression="arrivalFormAction.bindAndValidate()" />

<!-- Ou modifier la méthode pour accepter RequestContext -->
public void bindAndValidate(RequestContext context) {
    // context.getFlowScope().get("arrival")
}
```

### Erreur 2 : `Method not found` pour bindAndValidate

**Symptôme** :
```
org.springframework.expression.spel.SpelException: EL1006E: Function or method 'bindAndValidate' could not be found on type 'org.belex.arrival.ArrivalFormAction'
```

**Solution** :
1. Vérifier que la méthode existe dans ArrivalFormAction
2. Vérifier la signature : `public void bindAndValidate(RequestContext context)`
3. Vérifier que le bean `arrivalFormAction` est correctement injecté
4. Redémarrer l'application

### Erreur 3 : `Transition on event not found`

**Symptôme** :
```
No transition found on event 'searchPlannedSupplier' in state 'selectArrivalDateView'
```

**Solution** :
```xml
<!-- Vérifier que l'événement est déclaré -->
<view-state id="selectArrivalDateView" view="productEntry/selectArrivalDate">
    <transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct">
        <!-- ↑ Doit correspondre à l'événement du formulaire -->
    </transition>
</view-state>

<!-- Dans le JSP -->
<input type="submit" name="_eventId_searchPlannedSupplier" value="Chercher" />
```

### Erreur 4 : `Flow state not found`

**Symptôme** :
```
State 'closedListView' not found in flow 'arrival'
```

**Solution** :
```xml
<!-- Vérifier que closedListView existe -->
<view-state id="closedListView" view="productEntry/closed/closedList">
    <!-- ... -->
</view-state>

<!-- Ou corriger l'ID de destination -->
<transition to="closedListView" />
<!-- Au lieu de -->
<transition to="closedList" />
```

### Erreur 5 : `JSP view not found`

**Symptôme** :
```
View 'productEntry/selectArrivalDate' could not be resolved
```

**Solution** :
1. Vérifier le fichier JSP existe : `/WEB-INF/jsp/productEntry/selectArrivalDate.jsp`
2. Vérifier les permissions de lecture
3. Vérifier le chemin dans le viewResolver de flow-servlet.xml :
```xml
<property name="prefix" value="/WEB-INF/jsp/"/>
<property name="suffix" value=".jsp"/>
```

---

## 9. Tester en isolation (Unit Test simulation)

### Créer une classe de test
```java
package org.belex.arrival;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/flow-servlet.xml"})
public class ArrivalFlowTest {
    
    @Autowired
    private FlowExecutionFactory flowExecutionFactory;
    
    @Test
    public void testSearchPlannedSuppliersAction() {
        Arrival arrival = new Arrival();
        Arrival result = arrivalBusiness.getPlannedSuppliers(arrival);
        
        assertNotNull("Result should not be null", result);
        assertTrue("Should have suppliers", result.getSuppliers().size() > 0);
    }
}
```

---

## 10. Monitoring en production

### Vérifier les performances
```
# Temps de réponse pour chaque transition
curl -w "\n%{time_total}s\n" http://localhost:8080/belex/flow/arrival
```

### Vérifier la mémoire
```powershell
# Via JMX
$javaProcess = Get-Process java | Where-Object { $_.ProcessName -eq "java" }
$javaProcess | Select-Object WorkingSet64, VirtualMemorySize64
```

### Vérifier les sessions WebFlow
```
# Dans tomcat logs
[INFO] FlowExecutionListener: Flow 'arrival' execution created
[INFO] FlowExecutionListener: Flow 'arrival' execution ended
```

---

## 11. Rollback en cas de problème

### Si la migration cause des erreurs
```bash

# Updater la configuration
# flow-servlet.xml doit pointer vers productEntry.xml au lieu de v2-productEntry-clean.xml

# Rebuild
mvn clean package -DskipTests
```

---

## 12. Vérifier la configuration de v2-productEntry

### Fichier critique : flow-servlet.xml
```xml
<flow:flow-registry id="flowRegistry"
                    flow-builder-services="flowBuilderServices">
    <flow:flow-location id="test" path="/WEB-INF/flows/test-flow.xml"/>
    <flow:flow-location id="arrival" path="/WEB-INF/flows/v2-productEntry-clean.xml"/>
    <!-- ↑ Doit pointer vers v2-productEntry-clean.xml -->
</flow:flow-registry>
```

### Vérifier que v2-productEntry.xml est bien formé
```bash
# Validation XML
mvn clean compile

# Si erreur XML, le build échouera lors de la création du bean
# Vérifier l'indentation et la fermeture de toutes les balises
```

---

## 13. Support et escalade

Si vous rencontrez une erreur non documentée :

1. **Consulter les logs**
   ```
   tail -f target/catalina.log
   tail -f target/classes/log4j.log
   ```

2. **Activer debug mode**
   ```xml
   <logger name="org.springframework.webflow" additivity="false">
       <level value="TRACE" />
   </logger>
   ```

3. **Consulter la documentation**
   - Spring WebFlow 2.5.1 : https://docs.spring.io/spring-webflow/docs/current/
   - SpEL : https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions

4. **Contacter le support**
   - Fourni avec : MIGRATION_SUMMARY.md, TEST_CHECKLIST_PRODUCTENTRY.md, logs complets

---

## 14. Checklist de déploiement

- [ ] mvn clean package réussi
- [ ] target/belex.war créé
- [ ] WAR copié dans webapps/
- [ ] Tomcat redémarré
- [ ] Application accessible à http://localhost:8080/belex/
- [ ] Logs sans erreur Spring WebFlow
- [ ] Flux productEntry démarrable via /flow/arrival
- [ ] Test 1-3 du TEST_CHECKLIST_PRODUCTENTRY.md réussis
- [ ] Aucune régressions observées

---

**Document généré pour la migration productEntry.xml WebFlow 1.0 → 2.5.1**

