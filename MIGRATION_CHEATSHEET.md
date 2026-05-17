# ⚡ QUICK REFERENCE CARD - ProductEntry Migration

## 🚀 Deploy in 3 Steps

### Step 1: COPY
```bash
copy src\main\webApp\WEB-INF\flows\v2-productEntry-clean.xml ^
     src\main\webApp\WEB-INF\flows\v2-productEntry.xml
```

### Step 2: BUILD
```bash
mvn clean install
```

### Step 3: TEST
```
http://localhost:8080/belex/flow/arrival
```

---

## 📋 What Changed

| File | Change |
|------|--------|
| `v2-productEntry.xml` | Complete WebFlow 1 → 2.5.1 migration |
| `selectArrivalDate.jsp` | Forms now use `${flowExecutionUrl}` |
| `warningPendingDetected.jsp` | Form action fixed + JS updated |

---

## ✅ Success Looks Like

✓ Page loads without 404  
✓ Calendar picker works  
✓ Suppliers list appears  
✓ Forms submit correctly  
✓ No errors in logs  

---

## ❌ Failure Looks Like

✗ 404 on /belex/flow/arrival  
✗ Form goes wrong place  
✗ "Unknown bean" errors  
✗ Null pointer exceptions  

---

## 🔍 Quick Debug

**Check form action:**
```bash
grep "action=" src\main\webApp\WEB-INF\jsp\productEntry\selectArrivalDate.jsp
# Should show: action="${flowExecutionUrl}"
```

**Check logs:**
```bash
tail -f apache-tomcat-9.0.115\logs\catalina.out | grep -i "arrival\|flow\|error"
```

---

## 📚 Documentation Map

| Need | Read | Time |
|------|------|------|
| Overview | DELIVERY_SUMMARY.md | 5 min |
| Start | INDEX.md | 5 min |
| Understand | WEBFLOW_MIGRATION_GUIDE.md | 15 min |
| Test | PRODUCTENTRY_MIGRATION_TEST.md | 2 min |
| Details | PRODUCTENTRY_MIGRATION_SUMMARY.md | 10 min |

---

## 🎯 Flow Structure

```
selectArrivalDateView
  ├─ searchPlannedSupplier → reload suppliers
  └─ showSuppliers → checkPendingArrivalAct
     → showSuppliersView → fillPlannedSupplierAct
        ├─ isClosed → closedListView
        └─ isNotClosed → getPendingArrivalAct
           ├─ true → warningPendingDetectedView
           └─ false → supplierDocumentFormView
```

---

## 🔑 Three WebFlow 2.5.1 Patterns

```xml
<!-- Pattern 1: Action State -->
<action-state id="myAction">
    <evaluate expression="bean.method(data)" result="flowScope.data" />
    <transition on="event" to="nextState" />
</action-state>

<!-- Pattern 2: View State -->
<view-state id="myView" view="path/view" model="beanName">
    <transition on="event" to="nextState" />
</view-state>
```

```jsp
<!-- Pattern 3: Form -->
<form action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
    <input type="submit" name="_eventId_eventName" value="Next" />
</form>
```

---

## 🛠️ Common Fixes

| Error | Fix |
|-------|-----|
| Form posts wrong place | Use `${flowExecutionUrl}` |
| Unknown event | Check `_eventId_eventName` naming |
| Null properties | Add `model="beanName"` to view-state |
| 404 on flow | Check flow-servlet.xml registration |

---

## ⚡ Command Reference

```bash

# Deploy
mvn clean install
copy target\belex.war apache-tomcat-9.0.115\webapps\

# Test
curl -i http://localhost:8080/belex/flow/arrival

# Monitor
tail -f apache-tomcat-9.0.115\logs\catalina.out
```

---

## ✨ Key Rules

1. **Always use `${flowExecutionUrl}`** - Never hardcode flow URLs
2. **`model="beanName"`** - Auto-binds form properties
3. **`_eventId_eventName`** - Button name triggers transitions
4. **No Java changes** - XML/JSP only
5. **3 patterns cover everything** - Action state, View state, Form

---

## 📊 Deliverables

✅ 1 new XML flow (v2-productEntry-clean.xml)  
✅ 2 updated JSP files  
✅ 5 comprehensive documentation files  
✅ 0 Java changes  
✅ 0 breaking changes  

**Time to deploy:** 30 minutes  
**Risk level:** LOW  
**Status:** ✅ READY

---

**Start:** Open `INDEX.md`  
**Deploy:** Follow `PRODUCTENTRY_MIGRATION_TEST.md`  
**Reference:** Check `WEBFLOW_MIGRATION_GUIDE.md`

🚀 Ready to go! Next step: `INDEX.md`

