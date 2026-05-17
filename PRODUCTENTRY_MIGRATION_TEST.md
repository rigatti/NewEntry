# IMMEDIATE ACTION: ProductEntry Flow Migration Test

## What You Need to Do RIGHT NOW

### Step 1: Replace the XML Flow File
Replace the flow definition with the clean version:

```bash
# BACKUP the old file first
copy src\main\webApp\WEB-INF\flows\v2-productEntry.xml src\main\webApp\WEB-INF\flows\v2-productEntry-backup.xml

# Use the new clean file
copy src\main\webApp\WEB-INF\flows\v2-productEntry-clean.xml src\main\webApp\WEB-INF\flows\v2-productEntry.xml
```

**OR manually:**
1. Open `src/main/webApp/WEB-INF/flows/v2-productEntry-clean.xml`
2. Copy entire content
3. Paste into `src/main/webApp/WEB-INF/flows/v2-productEntry.xml`
4. Save

### Step 2: Verify JSP Files

Check these JSP files are updated (they should be already if you ran earlier commands):

```bash
# These should have been updated
src/main/webApp/WEB-INF/jsp/productEntry/selectArrivalDate.jsp
src/main/webApp/WEB-INF/jsp/productEntry/warningPendingDetected.jsp
```

**Quick verification in selectArrivalDate.jsp:**
- Line ~73: Should have `action="${flowExecutionUrl}"` (NOT `/flow/arrival`)
- Line ~116: Should NOT have the word "bla"

**Quick verification in warningPendingDetected.jsp:**
- Line ~31: Should have `action="${flowExecutionUrl}"` (NOT `flowController.htm`)
- Lines ~6-18: Should have `sendNextFrm(eventName)` function

### Step 3: Rebuild and Deploy

```bash
# From project root
mvn clean install

# Copy to Tomcat
copy target\belex.war apache-tomcat-9.0.115\webapps\

# Restart Tomcat (or let it auto-redeploy)
```

### Step 4: Enable Debug Logging

Edit `src/main/resources/log4j.xml` and add/update:

```xml
<!-- Add this logger to see WebFlow state transitions -->
<logger name="org.springframework.webflow" level="debug" additivity="false">
    <appender-ref ref="FILE" />
    <appender-ref ref="CONSOLE" />
</logger>

<!-- Add this to see Hibernate SQL and parameters -->
<logger name="org.hibernate.SQL" level="debug" />
<logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="trace" />

<!-- Add this to see business logic debug -->
<logger name="org.belex.arrival" level="debug" />
<logger name="db" level="debug" />
```

Rebuild after log4j changes:
```bash
mvn clean install
```

---

## Quick Test Flow (2 minutes)

### Open Browser
1. Go to: `http://localhost:8080/belex/flow/arrival`

### Expected: Page with date picker appears
- Should display calendar widget
- "Arrivage de marchandises - Sélection du fournisseur" heading
- Message: "Aucun fournisseur n'est planifié pour la date sélectionnée" (No suppliers for today is OK)

### Test Date Change
1. Click calendar to pick **yesterday's date**
2. Click "Search" or let onchange trigger
3. Watch logs for: `DEBUG ArrivalBusinessImpl - DEBUG checkPending: Found N suppliers`

### If suppliers appear for that date
4. Click "View Suppliers" button
5. Should navigate to showSuppliers.jsp showing supplier dropdown
6. Select a supplier
7. Click "Continue"
8. You should see either:
   - ERROR page (if something broke) - CHECK LOGS
   - Warning page (if pending entries exist) - CLICK DELETE or CONTINUE
   - Supplier document form - GOOD!

---

## Troubleshooting

### Issue: 404 on `/belex/flow/arrival`
**Solution:**
- Check flow-servlet.xml line 35 exists:
  ```xml
  <flow:flow-location id="arrival" path="/WEB-INF/flows/v2-productEntry.xml"/>
  ```
- Verify file exists: `target/belex/WEB-INF/flows/v2-productEntry.xml`
- Restart Tomcat

### Issue: Page loads but no suppliers appear
**Expected if no suppliers planned for today - Pick yesterday's date instead**

If yesterday has suppliers but still nothing:
- Check logs for errors
- SQL might be failing - check PlanningDAO logs

### Issue: "Cannot resolve symbol" errors in JSP
- Check encoding on selectArrivalDate.jsp (line 1 should state UTF-8)
- This is a compilation issue, rebuild: `mvn clean install`

### Issue: Date picker doesn't work
- Check `/pic/dlcalendar/` directory exists with calendar images
- This is NOT a WebFlow issue, it's a legacy JavaScript library

### Issue: Form submission goes to wrong page
**Most likely: flowExecutionUrl not being set**
- Check JSP has `<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>`
- Check flow-servlet.xml has ViewResolver configured
- Check JSP doesn't override variables

---

## Expected Log Output (Good Migration)

```
DEBUG [Flow Navigation] 
  FlowHandlerMapping - Mapping request with URI '/belex/flow/arrival' to flow with id 'arrival'
  
DEBUG [Flow Execution]
  FlowExecutionImpl - Starting in org.springframework.webflow.mvc.servlet.MvcExternalContext@xyz
  ActionState - Entering state 'searchPlannedSuppliersAct' of flow 'arrival'
  
DEBUG [Business Logic]
  ArrivalBusinessImpl - DEBUG checkPending: searchSupplierDate=20260412, date=20260412
  PlanningDAO - Finding Planning by date: 20260412 and supplier null
  SQL - select planning0_... from PlanningRéceptions planning0_ ... [HQL output]
  ArrivalBusinessImpl - DEBUG checkPending: Found 3 suppliers
  
DEBUG [View Rendering]
  ViewState - Rendering + [ServletMvcView@xyz view = JstlView: name 'productEntry/selectArrivalDate']
  AbstractMvcView - Rendering MVC with model map [...arrival=Arrival@xyz...]
```

If you DON'T see these, check that:
1. Log level is DEBUG (not INFO or ERROR)
2. Classes have `@Slf4j` annotation
3. Logger names match package names

---

## Files Changed This Session

✅ **Created:**
- `WEBFLOW_MIGRATION_GUIDE.md` - Comprehensive migration guide
- `PRODUCTENTRY_MIGRATION_SUMMARY.md` - What was changed
- `v2-productEntry-clean.xml` - New clean flow definition
- `PRODUCTENTRY_MIGRATION_TEST.md` - This file

🔄 **Updated:**
- `selectArrivalDate.jsp` - Fixed form actions
- `warningPendingDetected.jsp` - Fixed form submission

✅ **No changes needed:**
- `flow-servlet.xml` - Already configured correctly
- `showSuppliers.jsp` - Already correct
- All Java classes - No changes needed

---

## Success Criteria

✅ Test is successful if:
1. Flow loads without 404 errors
2. You can pick a date
3. Suppliers list loads for that date
4. You can select and proceed
5. No WebFlow exceptions in logs
6. Form submissions use POST (check browser Network tab)

❌ Test failed if:
1. 404 errors appear
2. Forms go to broken URLs
3. Parameters aren't passed between pages
4. Java exceptions in logs
5. Form seems to reload instead of navigate

---

## Questions Before Starting?

Before you run the test:
- Do you want to test JUST the date selection first? (Stop after seeing suppliers)
- Or go all the way to product entry?
- Do you have a date with planned suppliers in the DB?

Check DB:
```sql
SELECT DISTINCT DateRéception 
FROM PlanningRéceptions 
ORDER BY DateRéception DESC
LIMIT 5;
```

Pick one of these dates for testing!

