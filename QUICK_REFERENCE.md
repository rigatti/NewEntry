# ⚡ QUICK REFERENCE - productEntry.xml Migration

## BUILD & DEPLOY (Copy-Paste Ready)

```powershell
# 1. CLEAN BUILD
cd "C:\Users\miche\Downloads\temp belex\belexGit\NewEntry"
mvn clean package -DskipTests

# 2. COPY WAR TO TOMCAT
Copy-Item -Path "target\belex.war" -Destination "C:\Users\miche\Downloads\apache-tomcat-9.0.115\webapps\" -Force

# 3. RESTART TOMCAT
# Option A: Manual
C:\Users\miche\Downloads\apache-tomcat-9.0.115\bin\shutdown.bat
C:\Users\miche\Downloads\apache-tomcat-9.0.115\bin\startup.bat

# Option B: Service (if installed)
net stop Tomcat9
net start Tomcat9

# 4. VERIFY DEPLOYMENT
# Browser: http://localhost:8080/belex/flow/arrival

# 5. CHECK LOGS
Get-Content -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\catalina.log" -Wait
```

---

## KEY FILES AT A GLANCE

```
✅ v2-productEntry.xml
   └─ 324 lines | 37 states | WebFlow 2.5.1 ready

📋 MIGRATION_PRODUCTENTRY.md
   └─ Technical details of all changes

📋 MIGRATION_SUMMARY.md
   └─ Executive summary + attention points

📋 TEST_CHECKLIST_PRODUCTENTRY.md
   └─ 15 complete test cases

📋 DEPLOYMENT_DEBUGGING_GUIDE.md
   └─ Deployment & debugging instructions
```

---

## TROUBLESHOOTING QUICK FIXES

### Problem: `NullPointerException` on flowRequestContext
```java
// FIX: Change method signature
// From:
public void bindAndValidate() { }

// To:
public void bindAndValidate(RequestContext context) {
    MutableAttributeMap flowScope = context.getFlowScope();
}
```

### Problem: Method not found exception
```xml
<!-- Check that method is public and spelling is correct -->
<evaluate expression="arrivalFormAction.bindAndValidate(flowRequestContext)" />
<!-- Verify: public void bindAndValidate(RequestContext ctx) -->
```

### Problem: Transition to state not found
```xml
<!-- Verify the state ID exists -->
<view-state id="closedListView" view="productEntry/closed/closedList">
    <!-- State exists, safe to transition to it -->
</view-state>
```

### Problem: JSP view not found
```
Check: /WEB-INF/jsp/productEntry/selectArrivalDate.jsp exists
Verify: flow-servlet.xml has correct prefix/suffix:
  prefix="/WEB-INF/jsp/"
  suffix=".jsp"
```

---

## ESSENTIAL MIGRATION PATTERNS

### Pattern 1: Bean Action to Evaluate
```xml
<!-- WebFlow 1.0 -->
<bean-action bean="arrivalBusiness" method="getPlannedSuppliers">
    <method-arguments>
        <argument expression="flowScope.arrival"/>
    </method-arguments>
    <method-result name="arrival" scope="flow"/>
</bean-action>

<!-- WebFlow 2.5.1 -->
<evaluate expression="arrivalBusiness.getPlannedSuppliers(flowScope.arrival)" 
          result="flowScope.arrival"/>
```

### Pattern 2: Transition with Action
```xml
<!-- WebFlow 1.0 -->
<transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct">
    <action bean="arrivalFormAction" method="bindAndValidate"/>
</transition>

<!-- WebFlow 2.5.1 -->
<transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct">
    <evaluate expression="arrivalFormAction.bindAndValidate(flowRequestContext)" />
</transition>
```

### Pattern 3: Action State
```xml
<!-- WebFlow 1.0 -->
<action-state id="checkPendingArrivalAct">
    <bean-action bean="arrivalBusiness" method="checkPending">
        <method-arguments>
            <argument expression="flowScope.arrival"/>
        </method-arguments>
    </bean-action>
    <transition to="showSuppliersView" />
</action-state>

<!-- WebFlow 2.5.1 -->
<action-state id="checkPendingArrivalAct">
    <evaluate expression="arrivalBusiness.checkPending(flowScope.arrival)"/>
    <transition to="showSuppliersView" />
</action-state>
```

### Pattern 4: Boolean Return with Transitions
```xml
<!-- WebFlow 2.5.1 -->
<action-state id="getPendingArrivalAct">
    <evaluate expression="arrivalBusiness.getPending(flowScope.arrival)"/>
    <transition on="true" to="warningPendingDetectedView" />
    <transition on="false" to="supplierDocumentFormView" />
</action-state>
```

---

## MIGRATION VALIDATION CHECKLIST

```
□ XSD changed to spring-webflow.xsd (not spring-webflow-1.0.xsd)
□ <flow> tag has start-state attribute (not separate <start-state> element)
□ All <bean-action> converted to <evaluate expression="...">
□ All <method-arguments> embedded in expression
□ All <method-result> converted to result attribute
□ All <action> in transitions converted to <evaluate>
□ All parameters include flowRequestContext where needed
□ All state IDs in transitions point to existing states
□ All JSP view paths exist in /WEB-INF/jsp/
□ XML is well-formed (valid indentation, closed tags)
□ Maven compile succeeds
□ Maven package creates target/belex.war
```

---

## TESTING QUICK COMMANDS

```powershell
# Run compile only (fast)
mvn compile -DskipTests

# Run build (takes time)
mvn clean package -DskipTests

# Clear cache and rebuild
mvn clean install -DskipTests -DskipTests

# Run with specific profile (if defined)
mvn clean package -P production

# Skip tests and run build
mvn package -DskipTests -T 1C  # -T 1C uses 1 core

# Generate WAR only (no compile)
mvn war:war

# Verify without building
mvn verify -DskipTests
```

---

## FLOWSCOPE VARIABLES TO INITIALIZE

These must exist before methods accessing them:

```xml
<on-start>
    <!-- Main entry object -->
    <set name="flowScope.arrival" value="new org.belex.arrival.Arrival()" />
    
    <!-- Products list (populated by completeProductsAct) -->
    <!-- <set name="flowScope.products" value="new java.util.ArrayList()" /> -->
    
    <!-- Request parameters (for modification flow) -->
    <!-- <set name="flowScope.requestParams" value="new org.belex.requestparams.RequestParams()" /> -->
    
    <!-- Product (for new product form) -->
    <!-- <set name="flowScope.product" value="new db.product.Product()" /> -->
</on-start>
```

---

## BEAN SIGNATURES THAT NEED FLOWREQUESTCONTEXT

These methods are called with flowRequestContext parameter:

```java
public void bindAndValidate(RequestContext context)
public void checkProductsFound(RequestContext context)
public void validateCompleteEntry(RequestContext context)

// These might not need it:
public void saveTempEntry()
public void updateTempEntry()
public void resetNewEntry()

// If they do, adapt to:
public void saveTempEntry(RequestContext context) { }
```

---

## IMPORTANT STATES MAP

```
Entry flow START:
  searchPlannedSuppliersAct 
    ↓
  selectArrivalDateView 
    ↓ (showSuppliers)
  checkPendingArrivalAct 
    ↓
  showSuppliersView 
    ↓ (searchEntry)
  fillPlannedSupplierAct 
    ↓ (isNotClosed)
  getPendingArrivalAct 
    ↓ (false)
  supplierDocumentFormView 
    ↓ (next)
  entryFormView 
    ↓ (submit)
  completeProductsAct 
    ↓ (single)
  selectBasketAct 
    ↓
  completeEntryFormView 
    ↓ (submit)
  confirmEntryAct 
    ↓ (success)
  saveTempEntryAct 
    ↓
  storeEntryAct 
    ↓ (success)
  confirmEntryView 
    ↓ (submit)
  clearNewEntryAct 
    ↓
  entryFormView (back to main)

Closed path: fillPlannedSupplierAct (isClosed) → closedListView → ...
Error path: Any state → fatalErrorView
```

---

## PERFORMANCE TIPS

```
# Build faster with parallel compilation
mvn clean package -DskipTests -T 1C

# Skip all unnecessary processing
mvn clean package -DskipTests -Dcheckstyle.skip=true

# Use offline mode if dependencies cached
mvn clean package -DskipTests -o

# Skip logging and reduce output
mvn clean package -DskipTests -q
```

---

## QUICK LOG ENABLE

Add to src/main/resources/log4j.xml:

```xml
<logger name="org.springframework.webflow" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>

<logger name="org.belex" additivity="false">
    <level value="DEBUG" />
    <appender-ref ref="CONSOLE" />
</logger>
```

Then rebuild: `mvn clean package -DskipTests`

---

## CRITICAL URLS TO TEST

```
http://localhost:8080/belex/flow/arrival
  ↓ (initial page load)
  
http://localhost:8080/belex/flow/arrival?_flowExecutionKey=e1s2&_eventId=showSuppliers
  ↓ (simulated button click)

http://localhost:8080/belex/
  ↓ (home page)

http://localhost:8080/belex/index.jsp
  ↓ (fallback)
```

---

## GIT OPERATIONS

```powershell
# View changes made
git diff src/main/webApp/WEB-INF/flows/v2-productEntry.xml

# Stage migration files
git add MIGRATION_*.md
git add v2-productEntry.xml

# Commit migration
git commit -m "Migration: WebFlow productEntry.xml 1.0 -> 2.5.1"

# If rollback needed
git checkout HEAD -- v2-productEntry.xml
```

---

**Last Updated: 2026-04-13**  
**Migration Status: COMPLETE ✅**  
**Ready for: Testing & Deployment**

